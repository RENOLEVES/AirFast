@Service
public class FlightServiceImpl {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public FlightServiceImpl(FlightRepository flightRepository,
                             BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    // ---------- Create ----------
    @Transactional
    public FlightResponseDTO createFlight(FlightRequestDTO d) {
        require(d.getDepartLocation(), "departLocation");
        require(d.getArrivalLocation(), "arrivalLocation");
        Objects.requireNonNull(d.getExpectedDepartTime(), "expectedDepartTime is required");
        if (d.getCapacity() <= 0) throw new IllegalArgumentException("capacity must be > 0");

        Flight f = new Flight();
        f.setCapacity(d.getCapacity());
        f.setExpectedDepartTime(d.getExpectedDepartTime());
        f.setDepartTime(d.getExpectedDepartTime());
        f.setArrivalTime(d.getArrivalTime());      
        f.setDepartLocation(d.getDepartLocation());
        f.setArrivalLocation(d.getArrivalLocation());
        f.setFlightNumber(d.getFlightNumber());
        f.setFlightTime(d.getFlightTime());
        f.setRecurring(d.isRecurring());
        f.setActive(true);
        f.setSeatsRemaining(d.getCapacity());
        f.setDelayInHours(0);
        f.setStatus(FlightStatus.SCHEDULED);

        return toResponse(flightRepository.save(f));
    }

    // ---------- Read ----------
    public FlightResponseDTO getFlightById(long id) {
        Flight f = flightRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Flight " + id + " not found."));
        return toResponse(f);
    }

    public List<FlightResponseDTO> listAllFlights() {
        return flightRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<FlightResponseDTO> searchFlights(String from, String to, LocalDate date) {
        return flightRepository.findAll().stream()
            .filter(f -> from.equals(f.getDepartLocation()))
            .filter(f -> to.equals(f.getArrivalLocation()))
            .filter(f -> f.getDepartTime() != null && f.getDepartTime().toLocalDate().equals(date))
            .map(this::toResponse)
            .toList();
    }

    // ---------- Update ----------
    @Transactional
    public FlightResponseDTO updateFlight(long id, FlightRequestDTO d) {
        Flight f = flightRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Flight " + id + " not found."));

        if (d.getCapacity() > 0 && d.getCapacity() != f.getCapacity()) {
            int active = countActiveBookings(f);
            if (d.getCapacity() < active)
                throw new IllegalArgumentException("Cannot reduce capacity below current active bookings (" + active + ").");
            f.setCapacity(d.getCapacity());
        }

        if (d.getDepartLocation() != null) f.setDepartLocation(d.getDepartLocation());
        if (d.getArrivalLocation() != null) f.setArrivalLocation(d.getArrivalLocation());
        if (d.getExpectedDepartTime() != null) f.setExpectedDepartTime(d.getExpectedDepartTime());
        if (d.getDepartTime() != null) f.setDepartTime(d.getDepartTime());
        if (d.getArrivalTime() != null) f.setArrivalTime(d.getArrivalTime());
        if (d.getFlightNumber() != 0) f.setFlightNumber(d.getFlightNumber());
        if (d.getFlightTime() != 0) f.setFlightTime(d.getFlightTime());
        if (d.getIsActive() != null) f.setActive(d.getIsActive());
        f.setRecurring(d.isRecurring());

        return toResponse(f);
    }

    // ---------- Cancel ----------
    @Transactional
    public void cancelFlight(long id) {
        Flight f = flightRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Flight " + id + " not found."));
        f.setActive(false);
        f.setStatus(FlightStatus.CANCELLED);

        List<Booking> bs = bookingRepository.findByFlight(f);
        for (Booking b : bs) {
            if (b.getBookingStatus() != BookingStatus.CANCELLED_BY_CUSTOMER) {
                b.setBookingStatus(BookingStatus.CANCELLED_BY_AIRLINE);
            }
        }
    }

    // ---------- Delay action ----------
    @Transactional
    public FlightResponseDTO delayFlight(long id, int hours) {
        Flight f = flightRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Flight " + id + " not found."));
        if (hours == 0) return toResponse(f);

        f.setDelayInHours(f.getDelayInHours() + hours);
        if (f.getDepartTime() != null) f.setDepartTime(f.getDepartTime().plusHours(hours));
        if (f.getArrivalTime() != null) f.setArrivalTime(f.getArrivalTime().plusHours(hours));
        f.setStatus(FlightStatus.DELAYED);

        return toResponse(f);
        }

    // ---------- Helpers ----------
    private FlightResponseDTO toResponse(Flight f) {
        int seatsRemaining = computeSeatsRemainingFromBookings(f);
        return new FlightResponseDTO(
            f.getFlightId(),
            f.getCapacity(),
            seatsRemaining,
            f.getDelayInHours(),
            f.getDepartTime(),
            f.getArrivalTime(),
            f.getExpectedDepartTime(),
            f.getDepartLocation(),
            f.getArrivalLocation(),
            f.getFlightNumber(),
            f.getFlightTime(),
            f.isRecurring(),
            f.isActive(),
            f.getStatus()
        );
    }

    private int computeSeatsRemainingFromBookings(Flight f) {
        int confirmed = (int) bookingRepository.findByFlight(f).stream()
            .filter(b -> b.getBookingStatus() == BookingStatus.CONFIRMED)
            .count();
        return Math.max(0, f.getCapacity() - confirmed);
    }

    private int countActiveBookings(Flight f) {
        return (int) bookingRepository.findByFlight(f).stream()
            .filter(b -> b.getBookingStatus() == BookingStatus.CONFIRMED
                      || b.getBookingStatus() == BookingStatus.WAITLIST)
            .count();
    }

    private static void require(String s, String field) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(field + " is required");
    }
}

