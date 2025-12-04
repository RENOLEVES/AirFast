-- Customers (membershipNumber is integer)
INSERT INTO person (dtype, email, password, first_name, last_name, membership_number, points, time_in_flight)
VALUES
    ('Customer', 'alice.chen@example.com', 'pass123', 'Alice', 'Chen', 10001, 15000, 480),
    ('Customer', 'bob.williams@example.com', 'pass123', 'Bob', 'Williams', 10002, 8500, 320),
    ('Customer', 'carol.kim@example.com', 'pass123', 'Carol', 'Kim', 10003, 2200, 90),
    ('Customer', 'david.patel@example.com', 'pass123', 'David', 'Patel', 10004, 450, 60),
    ('Customer', 'emma.brown@example.com', 'pass123', 'Emma', 'Brown', 10005, 12000, 410);

-- Employees
INSERT INTO person (dtype, email, password, first_name, last_name, is_active, salary, title)
VALUES
    ('Manager', 'john@airline.com', 'pass123', 'John', 'Stevens', true, 95000, 'Manager'),
    ('Pilot', 'robert@airline.com', 'pass123', 'Robert', 'Smith', true, 125000, 'Pilot'),
    ('FlightAttendant', 'lisa@airline.com', 'pass123', 'Lisa', 'Anderson', true, 52000, 'Flight Attendant');

-- Flights (status: 0=ONTIME, 1=DELAYED, 2=CANCELLED)
INSERT INTO flight (capacity, seats_remaining, delay_hours, depart_time, arrival_time, expected_depart_time, depart_location, arrival_location, flight_number, flight_time, is_recurring, is_active, status)
VALUES
    (180, 145, 0, '2025-12-10 08:30:00', '2025-12-10 09:45:00', '2025-12-10 08:30:00', 'Montreal', 'Toronto', 'AC101', 75, true, true, 0),
    (220, 180, 0, '2025-12-11 07:00:00', '2025-12-11 11:30:00', '2025-12-11 07:00:00', 'Montreal', 'Vancouver', 'AC201', 270, false, true, 0),
    (200, 140, 0, '2025-12-12 10:00:00', '2025-12-12 14:00:00', '2025-12-12 10:00:00', 'Toronto', 'Vancouver', 'AC301', 240, false, true, 0);

-- Seats (seat_class: 0=BUSINESS, 1=ECONOMY; seat_status: 0=TOBEDETERMINED, 1=AVAILABLE)
INSERT INTO seat (seat_class, price, seat_number, seat_status, flight_id)
VALUES
    (0, 850.00, '1A', 1, 1),
    (1, 420.00, '10A', 1, 1),
    (1, 180.00, '25A', 1, 1),
    (0, 920.00, '2A', 1, 2),
    (1, 450.00, '11A', 1, 2),
    (1, 195.00, '26A', 1, 2),
    (0, 1850.00, '1C', 1, 3),
    (1, 780.00, '12A', 1, 3);

-- Bookings (payment_status: 0=NOTPAID, 1=PAID; booking_status: 0=CONFIRMED, 1=WAITLISTED, 2=CANCELLED_BY_CUSTOMER, 3=CANCELLED_BY_AIRLINE)
INSERT INTO booking (customer_id, seat_id, booking_date, payment_status, booking_status)
VALUES
    (1, 1, '2025-11-28 10:30:00', 1, 0),
    (2, 2, '2025-11-29 14:20:00', 1, 0),
    (3, 3, '2025-11-30 09:15:00', 1, 0),
    (4, 4, '2025-12-01 16:45:00', 1, 0),
    (5, 5, '2025-12-01 11:00:00', 1, 0);
