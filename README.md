# ECSE 321 – Airline Management System

## 📌 Project Overview
This project is developed as part of **ECSE 321: Introduction to Software Engineering** at McGill University.  
The scope is to build a **web-based airline system** that allows:
- Customers to sign up, browse, and book flights (with 5% overbooking, rewards, cancellation/refunds).  
- Pilots and flight attendants to view their assigned flights.  
- Booking managers to create, edit, cancel flights and assign crew.  
- The owner to manage employees, promotions/policies, and review statistics.  

All stakeholders interact with the system through a web frontend; persistence is handled by PostgreSQL with JPA/Hibernate.

## Team Members

| Name        | Role                  | Deliverable 1 Effort (hrs) | Deliverable 2 Effort (hrs) | Deliverable 3 Effort (hrs) | Presentation Effort (hrs) | Total (hrs) |
|-------------|----------------------|----------------------------|----------------------------|----------------------------|----------------------------|-------------|
| Toufic | Project Manager and Scrum Master    | 8                          | 25                          | …                          | …                          | …           |
| Eric | Backend & Testing Lead	  | 8                          | 25                          | …                          | …                          | …           |
| Marshall    | UML & Architecture Lead        | 8                          | …                          | …                          | …                          | …           |
| Vincent    | Requirements & Documentation Lead       | 8                          | 20                          | …                          | …                          | …           |
| Lince    | Database & Integration Lead   | 8                          | 16-18                          | …                          | …                          | …           |

> Detailed per-meeting minutes and time logs are on the Wiki (see below).

## How We Work

- **Process:** Lightweight Scrum/Kanban on GitHub Projects  
- **Branches:** `main` (protected), `develop`, `feature/<scope>`  
- **Issues:** All work tracked as Issues with labels + milestones; every commit references an Issue ID.  
- **PRs:** Required for merging to `develop`/`main`; checks must pass.

## Tech Stack
- Backend: Java 21, Spring Boot, JPA/Hibernate  
- Database: PostgreSQL (via Docker/Testcontainers)  
- Frontend: React + TypeScript  
- CI/CD: GitHub Actions, Docker Compose  

## Deliverables & Links
- Deliverable 1: Requirements, Domain Model, Persistence + Tests  
- Deliverable 2: Backend APIs  
- Deliverable 3: Frontend  
- Final: Presentation & Demo

## Documentation
- Project Reports: each includes meeting minutes and design decisions per deliverable.
- Requirements Model
- Domain Model


