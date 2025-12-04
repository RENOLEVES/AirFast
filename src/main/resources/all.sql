
INSERT INTO person (email, password, first_name, last_name, dtype, total_revenue, title)
VALUES
  ('owner@example.com', '123456', 'Mike', 'Thompson', 'Owner',0,'Owner');


-- Customers (15 diverse customers)
INSERT INTO person (email, password, first_name, last_name, dtype, membership_number, points, time_in_flight)
VALUES
    ('alice.chen@example.com', 'pass123', 'Alice', 'Chen', 'Customer', 1, 15000, 480),
    ('bob.williams@example.com', 'pass123', 'Bob', 'Williams', 'Customer', 2, 8500, 320),
    ('carol.kim@example.com', 'pass123', 'Carol', 'Kim', 'Customer', 3, 2200, 90),
    ('david.patel@example.com', 'pass123', 'David', 'Patel', 'Customer', 4, 450, 60),
    ('emma.brown@example.com', 'pass123', 'Emma', 'Brown', 'Customer', 5, 12000, 410),
    ('frank.russo@example.com', 'pass123', 'Frank', 'Russo', 'Customer', 6, 6700, 240),
    ('grace.lee@example.com', 'pass123', 'Grace', 'Lee', 'Customer', 7, 900, 75),
    ('henry.nguyen@example.com', 'pass123', 'Henry', 'Nguyen', 'Customer', 8, 18500, 620),
    ('iris.garcia@example.com', 'pass123', 'Iris', 'Garcia', 'Customer', 9, 3400, 140),
    ('aaa@example.com', '123456', 'Jack', 'Martin', 'Customer', 10, 550, 45);

-- Managers (3)
INSERT INTO person (email, password, first_name, last_name, dtype, is_Active, salary, title)
VALUES
    ('john.manager@airline.com', 'pass123', 'John', 'Stevens', 'Manager', true, 95000, 'Manager'),
    ('sarah.ops@airline.com', 'pass123', 'Sarah', 'Johnson', 'Manager', true, 88000, 'Manager'),
    ('bbb@example.com', '123456', 'Mike', 'Thompson', 'Manager', true, 92000, 'Manager');
-- Pilots (5)
INSERT INTO person (email, password, first_name, last_name, dtype, is_Active, salary, title)
VALUES
    ('captain.smith@airline.com', 'pass123', 'Robert', 'Smith', 'Pilot', true, 125000, 'Pilot'),
    ('captain.jones@airline.com', 'pass123', 'Jennifer', 'Jones', 'Pilot', true, 118000, 'Pilot'),
    ('captain.davis@airline.com', 'pass123', 'Michael', 'Davis', 'Pilot', true, 122000, 'Pilot'),
    ('fo.wilson@airline.com', 'pass123', 'Emily', 'Wilson', 'Pilot', true, 85000, 'Pilot'),
    ('fo.taylor@airline.com', 'pass123', 'James', 'Taylor', 'Pilot', true, 82000, 'Pilot');

-- Flight Attendants (6)
INSERT INTO person (email, password, first_name, last_name, dtype, is_Active, salary, title)
VALUES
    ('fa.anderson@airline.com', 'pass123', 'Lisa', 'Anderson', 'FlightAttendant', true, 52000, 'Flight Attendant'),
    ('fa.thomas@airline.com', 'pass123', 'Kevin', 'Thomas', 'FlightAttendant', true, 48000, 'Flight Attendant'),
    ('fa.jackson@airline.com', 'pass123', 'Maria', 'Jackson', 'FlightAttendant', true, 51000, 'Flight Attendant'),
    ('fa.white@airline.com', 'pass123', 'Daniel', 'White', 'FlightAttendant', true, 49000, 'Flight Attendant'),
    ('fa.harris@airline.com', 'pass123', 'Sophie', 'Harris', 'FlightAttendant', true, 53000, 'Flight Attendant'),
    ('fa.clark@airline.com', 'pass123', 'Ryan', 'Clark', 'FlightAttendant', true, 47000, 'Flight Attendant');

-- Flights (15 varied routes and times)
INSERT INTO flight (capacity, seats_remaining, delay_hours, depart_time, arrival_time, expected_depart_time, depart_location, arrival_location, flight_number, flight_time, is_recurring, is_active, status)
VALUES
    -- Montreal <-> Toronto
    (180, 145, 0, '2025-12-10 08:30:00', '2025-12-10 09:45:00', '2025-12-10 08:30:00', 'Montreal', 'Toronto', 'AC101', 75, true, true, 0),
    (180, 120, 0, '2025-12-10 14:00:00', '2025-12-10 15:15:00', '2025-12-10 14:00:00', 'Montreal', 'Toronto', 'AC102', 75, true, true, 0),
    (180, 160, 0, '2025-12-10 19:30:00', '2025-12-10 20:45:00', '2025-12-10 19:30:00', 'Toronto', 'Montreal', 'AC103', 75, true, true, 0),
    
    -- Montreal <-> Vancouver
    (220, 180, 0, '2025-12-11 07:00:00', '2025-12-11 11:30:00', '2025-12-11 07:00:00', 'Montreal', 'Vancouver', 'AC201', 270, false, true, 0),
    (220, 150, 1, '2025-12-11 16:00:00', '2025-12-11 20:30:00', '2025-12-11 15:00:00', 'Vancouver', 'Montreal', 'AC202', 270, false, true, 0),
    
    -- Toronto <-> Vancouver
    (200, 140, 0, '2025-12-12 10:00:00', '2025-12-12 14:00:00', '2025-12-12 10:00:00', 'Toronto', 'Vancouver', 'AC301', 240, false, true, 0),
    (200, 170, 0, '2025-12-12 17:00:00', '2025-12-12 21:00:00', '2025-12-12 17:00:00', 'Vancouver', 'Toronto', 'AC302', 240, false, true, 0),
    
    -- Montreal <-> Calgary
    (160, 100, 0, '2025-12-13 09:00:00', '2025-12-13 13:30:00', '2025-12-13 09:00:00', 'Montreal', 'Calgary', 'AC401', 270, false, true, 0),
    (160, 130, 0, '2025-12-13 15:00:00', '2025-12-13 19:30:00', '2025-12-13 15:00:00', 'Calgary', 'Montreal', 'AC402', 270, false, true, 0),
    
    -- Montreal <-> Ottawa
    (120, 85, 0, '2025-12-14 07:30:00', '2025-12-14 08:15:00', '2025-12-14 07:30:00', 'Montreal', 'Ottawa', 'AC501', 45, true, true, 0),
    (120, 95, 0, '2025-12-14 12:00:00', '2025-12-14 12:45:00', '2025-12-14 12:00:00', 'Ottawa', 'Montreal', 'AC502', 45, true, true, 0),
    (120, 110, 0, '2025-12-14 18:00:00', '2025-12-14 18:45:00', '2025-12-14 18:00:00', 'Montreal', 'Ottawa', 'AC503', 45, true, true, 0),
    
    -- Completed/Cancelled flights for demo
    (180, 0, 0, '2025-11-30 08:00:00', '2025-11-30 09:15:00', '2025-11-30 08:00:00', 'Montreal', 'Toronto', 'AC104', 75, false, false, 0),
    (200, 200, 0, '2025-12-05 14:00:00', '2025-12-05 18:00:00', '2025-12-05 14:00:00', 'Toronto', 'Vancouver', 'AC303', 240, false, false, 0),
    (180, 155, 0, '2025-12-15 06:00:00', '2025-12-15 07:15:00', '2025-12-15 06:00:00', 'Montreal', 'Toronto', 'AC105', 75, true, true, 0);

-- Seats (varied classes and prices per flight)
-- Flight 1 (AC101)
INSERT INTO seat (seat_class, price, seat_number, seat_status, flight_id) VALUES
    (1, 850.00, '1A', 0, 1), (1, 850.00, '1B', 1, 1), (1, 850.00, '2A', 1, 1),
    (1, 420.00, '10A', 0, 1), (1, 420.00, '10B', 0, 1), (1, 420.00, '11A', 1, 1),
    (0, 180.00, '25A', 0, 1), (0, 180.00, '25B', 0, 1), (0, 180.00, '26A', 0, 1);

-- Flight 2 (AC102)
INSERT INTO seat (seat_class, price, seat_number, seat_status, flight_id) VALUES
    (1, 920.00, '1A', 1, 2), (1, 920.00, '1B', 1, 2),
    (1, 450.00, '10A', 0, 2), (1, 450.00, '10B', 1, 2),
    (0, 195.00, '25A', 0, 2), (0, 195.00, '25B', 0, 2);

-- Flight 4 (AC201 - Long haul)
INSERT INTO seat (seat_class, price, seat_number, seat_status, flight_id) VALUES
    (1, 1850.00, '1A', 1, 4), (1, 1850.00, '2A', 0, 4),
    (1, 780.00, '10A', 1, 4), (1, 780.00, '11A', 0, 4),
    (0, 380.00, '30A', 0, 4), (0, 380.00, '31A', 0, 4);

-- Bookings (mix of confirmed, pending, cancelled)
INSERT INTO booking (customer_id, seat_id, booking_date, payment_status, booking_status) VALUES
    (1, 5, '2025-12-01 10:30:00', 1, 2),  -- Alice, confirmed
    (1, 10, '2025-12-02 14:20:00', 1, 2), -- Alice, confirmed
    (2, 8, '2025-12-01 11:45:00', 1, 2),  -- Bob, confirmed
    (5, 6, '2025-12-02 09:15:00', 1, 2),  -- Emma, confirmed
    (8, 15, '2025-12-03 16:30:00', 1, 2), -- Henry, confirmed
    (3, 7, '2025-12-04 08:00:00', 0, 0),  -- Carol, pending payment
    (4, 20, '2025-12-04 12:00:00', 1, 1), -- David, cancelled
    (6, 14, '2025-12-03 13:45:00', 1, 2); -- Frank, confirmed