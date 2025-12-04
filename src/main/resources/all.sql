INSERT INTO person (email, password, first_name, last_name, dtype, is_Active, salary)
VALUES
    ('ethan.hunt@example.com', 'hsh_ethan456', 'Ethan', 'Hunt', 'Manager',true,100),
    ('maya.singh@example.com', 'hsh_maya789', 'Maya', 'Singh', 'Manager',true,200);

INSERT INTO person (email, password, first_name, last_name, dtype, membership_number, points, time_in_flight)
VALUES
    ('ethan.hunt@example.com', 'hsh_ethan456', 'Ethan', 'Hunt', 'Customer',1,0,0),
    ('maya.singh@example.com', 'hsh_maya789', 'Maya', 'Singh', 'Customer',2,5000,155),
    ('aaa@gmail.com', '123456', 'aaa', 'aaa', 'Customer',123,99999,9999);

INSERT INTO person (email, password, first_name, last_name, dtype, is_Active, salary)
VALUES
    ('ethan.hunt@example.com', 'hsh_ethan456', 'Ethan', 'Hunt', 'Pilot',true,100),
    ('maya.singh@example.com', 'hsh_maya789', 'Maya', 'Singh', 'Pilot',true,100);

INSERT INTO person (email, password, first_name, last_name, dtype, is_Active, total_revenue)
VALUES
    ('owner@gmail.com', '123456', 'owner', 'owner', 'Owner',true, 0);


INSERT INTO person (email, password, first_name, last_name, dtype, is_Active, salary)
VALUES
    ('ethan.hunt@example.com', 'hsh_ethan456', 'Ethan', 'Hunt', 'FlightAttendant',true,100),
    ('maya.singh@example.com', 'hsh_maya789', 'Maya', 'Singh', 'FlightAttendant',true,100);

-- Insert test flights
INSERT INTO flight (flight_id, capacity, seats_remaining, delay_hours, depart_time, arrival_time, expected_depart_time, depart_location, arrival_location, flight_number, flight_time, is_recurring, is_active, status)
VALUES
    (1, 200, 150, 0, '2025-11-22 08:30:00', '2025-11-22 09:30:00', '2025-11-22 08:30:00', 'Montreal', 'Toronto', 'AC101', 60, false, true, null),
    (2, 180, 120, 0, '2025-11-22 14:45:00', '2025-11-22 15:45:00', '2025-11-22 14:45:00', 'Montreal', 'Toronto', 'AC102', 60, false, true, null),
    (3, 220, 180, 0, '2025-11-23 10:00:00', '2025-11-23 11:00:00', '2025-11-23 10:00:00', 'Montreal', 'Toronto', 'AC103', 60, false, true, null),
    (4, 200, 160, 0, '2025-11-25 07:30:00', '2025-11-25 11:45:00', '2025-11-25 07:30:00', 'Montreal', 'Vancouver', 'AC201', 255, false, true, null),
    (5, 190, 140, 2, '2025-11-26 16:00:00', '2025-11-26 17:00:00', '2025-11-26 14:00:00', 'Toronto', 'Montreal', 'AC104', 60, false, true, null),
    (6, 210, 190, 0, '2025-11-28 09:15:00', '2025-11-28 13:30:00', '2025-11-28 09:15:00', 'Toronto', 'Vancouver', 'AC202', 255, false, true, null),
    (7, 180, 150, 0, '2025-11-30 11:00:00', '2025-11-30 12:00:00', '2025-11-30 11:00:00', 'Montreal', 'Toronto', 'AC105', 60, false, true, null),
    (8, 200, 170, 0, '2025-12-01 15:30:00', '2025-12-01 19:45:00', '2025-12-01 15:30:00', 'Vancouver', 'Montreal', 'AC203', 255, false, true, null);


INSERT INTO seat (
    seat_class,
    price,
    seat_number,
    seat_status,
    flight_id
)
VALUES
    (0, 550.50, '1A', 1,1),
    (1, 180.00, '25C', 1,1),
    (1, 1200.00, '2D', 1,2),
    (0, 310.25, '12F', 1,2);


INSERT INTO booking (customer_id, seat_id,booking_date, payment_status, booking_status)
VALUES
    (3, 1 ,'2025-11-22 08:30:00', 1, 2),
    (4, 2,'2025-11-23 08:30:00', 1, 2),
    (3, 3,'2025-11-24 08:30:00', 1, 1);