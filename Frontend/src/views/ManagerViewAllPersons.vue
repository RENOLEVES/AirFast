<template>
  <div class="bg-white rounded-2xl h-full flex flex-col p-6">
    <!-- Header -->
    <header class="flex flex-col gap-2 md:flex-row md:items-center md:justify-between mb-4">
      <div>
        <h2 class="text-2xl font-extrabold text-slate-800">All Persons</h2>
        <p class="text-sm text-slate-500">
          {{ filteredPersons.length }} {{ filteredPersons.length === 1 ? 'person' : 'people' }} found
        </p>
      </div>

      <div class="flex gap-3 items-center">
        <!-- Role filter -->
        <select
          v-model="roleFilter"
          @change="resetPage"
          class="border rounded-lg px-3 py-2 text-sm text-slate-700 bg-white"
        >
          <option value="ALL">All roles</option>
          <option value="CUSTOMER">Customer</option>
          <option value="PILOT">Pilot</option>
          <option value="FLIGHT_ATTENDANT">Flight Attendant</option>
          <option value="MANAGER">Manager</option>
          <option value="OWNER">Owner</option>
        </select>

        <!-- Search box -->
        <div class="relative">
          <input
            v-model="search"
            @input="resetPage"
            type="text"
            placeholder="Search by name or email"
            class="border rounded-lg pl-9 pr-3 py-2 text-sm text-slate-700 w-56"
          />
          <i class="fas fa-search text-slate-400 text-xs absolute left-3 top-1/2 -translate-y-1/2"></i>
        </div>
      </div>
    </header>

    <!-- Table -->
    <section class="flex-1 overflow-hidden border rounded-xl">
      <div class="h-full overflow-auto">
        <table class="min-w-full text-sm">
          <thead class="bg-slate-50 text-slate-500 text-xs uppercase">
            <tr>
              <th class="px-5 py-3 text-left font-semibold">ID</th>
              <th class="px-5 py-3 text-left font-semibold">Name</th>
              <th class="px-5 py-3 text-left font-semibold">Email</th>
              <th class="px-5 py-3 text-left font-semibold">Role</th>
              <th class="px-5 py-3 text-left font-semibold">Identifier</th>
              <th class="px-5 py-3 text-right font-semibold">Activity</th>
            </tr>
          </thead>

          <tbody>
            <!-- Loading state -->
            <tr v-if="loading">
              <td colspan="6" class="px-5 py-6 text-center text-slate-400">
                Loading persons…
              </td>
            </tr>

            <!-- Error state -->
            <tr v-else-if="error">
              <td colspan="6" class="px-5 py-6 text-center text-red-500">
                {{ error }}
                <button
                  class="ml-3 px-3 py-1 rounded-lg bg-indigo-500 text-white text-xs font-medium hover:bg-indigo-600"
                  @click="fetchPersons"
                >
                  Try again
                </button>
              </td>
            </tr>

            <!-- Empty state -->
            <tr v-else-if="paginatedPersons.length === 0">
              <td colspan="6" class="px-5 py-6 text-center text-slate-400">
                No persons match your filters.
              </td>
            </tr>

            <!-- Data rows -->
            <tr
              v-for="person in paginatedPersons"
              :key="person.id"
              class="border-t hover:bg-slate-50 transition-colors"
            >
              <td class="px-5 py-3 text-slate-500 text-xs">
                {{ person.id }}
              </td>

              <td class="px-5 py-3">
                <div class="font-semibold text-slate-800">
                  {{ person.name }}
                </div>
              </td>

              <td class="px-5 py-3 text-slate-600">
                {{ person.email }}
              </td>

              <td class="px-5 py-3">
                <span
                  class="inline-flex items-center rounded-full px-3 py-1 text-xs font-medium"
                  :class="rolePillClass(person.role)"
                >
                  {{ prettyRole(person.role) }}
                </span>
              </td>

              <!-- Identifier could be passport, employeeNumber, etc. -->
              <td class="px-5 py-3 text-slate-500 text-xs">
                {{ person.identifier || '—' }}
              </td>

              <td class="px-5 py-3 text-right">
                <button
                  class="px-3 py-1 rounded-lg border text-xs font-medium text-slate-600 hover:bg-slate-100"
                  @click="$emit('view-details', person)"
                >
                  View
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <!-- Pagination footer -->
    <footer class="mt-4 flex items-center justify-between text-xs text-slate-500">
      <p>
        Showing
        <span class="font-semibold">
          {{ paginatedPersons.length ? firstItemIndex + 1 : 0 }}–{{ lastItemIndex }}
        </span>
        of
        <span class="font-semibold">{{ filteredPersons.length }}</span>
        people
      </p>

      <div class="flex gap-2">
        <button
          class="px-3 py-1 rounded-lg border text-xs font-medium hover:bg-slate-100 disabled:opacity-40 disabled:cursor-not-allowed"
          :disabled="page === 1"
          @click="page--"
        >
          Prev
        </button>
        <button
          class="px-3 py-1 rounded-lg border text-xs font-medium hover:bg-slate-100 disabled:opacity-40 disabled:cursor-not-allowed"
          :disabled="page === maxPage"
          @click="page++"
        >
          Next
        </button>
      </div>
    </footer>
  </div>
</template>

<script>
// If you already have an axios wrapper, import that instead:
import axios from "axios";

export default {
  name: "ManagerViewAllPersons",
  data() {
    return {
      persons: [],
      loading: false,
      error: null,

      // UI state
      search: "",
      roleFilter: "ALL",
      page: 1,
      pageSize: 9, // 3 cards per row * 3 rows
    };
  },
  computed: {
    filteredPersons() {
      let list = this.persons;

      if (this.roleFilter !== "ALL") {
        list = list.filter((p) => p.role === this.roleFilter);
      }

      if (this.search.trim()) {
        const q = this.search.toLowerCase();
        list = list.filter(
          (p) =>
            p.name.toLowerCase().includes(q) ||
            p.email.toLowerCase().includes(q)
        );
      }

      return list;
    },
    maxPage() {
      return Math.max(1, Math.ceil(this.filteredPersons.length / this.pageSize));
    },
    paginatedPersons() {
      const start = (this.page - 1) * this.pageSize;
      return this.filteredPersons.slice(start, start + this.pageSize);
    },
    firstItemIndex() {
      return (this.page - 1) * this.pageSize;
    },
    lastItemIndex() {
      return Math.min(
        this.firstItemIndex + this.paginatedPersons.length,
        this.filteredPersons.length
      );
    },
  },
  created() {
    this.fetchPersons();
  },
  methods: {
    async fetchPersons() {
      this.loading = true;
      this.error = null;
      try {
        // 🔧 adjust URL to your backend endpoint
        const res = await axios.get("/api/persons"); // or /employees
        // Normalize data shape a bit
        this.persons = res.data.map((p) => ({
          id: p.id,
          name: p.name,
          email: p.email,
          role: p.role, // e.g. "PILOT"
          identifier: p.passportNumber || p.employeeNumber || null,
        }));
        this.page = 1;
      } catch (err) {
        console.error(err);
        this.error = "Error fetching data. Load failed.";
      } finally {
        this.loading = false;
      }
    },
    resetPage() {
      this.page = 1;
    },
    prettyRole(role) {
      if (!role) return "Unknown";
      return role
        .toString()
        .toLowerCase()
        .split("_")
        .map((s) => s.charAt(0).toUpperCase() + s.slice(1))
        .join(" ");
    },
    rolePillClass(role) {
      switch (role) {
        case "PILOT":
          return "bg-sky-100 text-sky-700";
        case "FLIGHT_ATTENDANT":
          return "bg-emerald-100 text-emerald-700";
        c
