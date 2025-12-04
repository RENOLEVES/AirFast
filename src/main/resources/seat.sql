INSERT INTO seat (seat_class, price, seat_number, seat_status, flight_id)
VALUES
    -- Flight 1 (AC101) - Montreal to Toronto
    (2, 850.00, '1A', 2, 1),    -- Business, Booked
    (2, 850.00, '1B', 2, 1),    -- Business, Booked
    (1, 420.00, '10A', 2, 1),   -- Premium Economy, Booked
    (1, 420.00, '10B', 2, 1),   -- Premium Economy, Booked
    (0, 180.00, '25A', 2, 1),   -- Economy, Booked
    (0, 180.00, '25B', 0, 1),   -- Economy, Available
    
    -- Flight 2 (AC102) - Montreal to Toronto
    (2, 920.00, '2A', 2, 2),    -- Business, Booked
    (1, 450.00, '11A', 2, 2),   -- Premium Economy, Booked
    (0, 195.00, '26A', 2, 2),   -- Economy, Booked
    (0, 195.00, '26B', 2, 2),   -- Economy, Booked
    
    -- Flight 4 (AC201) - Montreal to Vancouver (long haul)
    (2, 1850.00, '1C', 2, 4),   -- Business, Booked
    (1, 780.00, '12A', 2, 4),   -- Premium Economy, Booked
    (0, 380.00, '30A', 0, 4),   -- Economy, Available
    
    -- Flight 5 (AC104) - Toronto to Montreal
    (2, 900.00, '1D', 0, 5),    -- Business, Available
    (1, 460.00, '13A', 0, 5),   -- Premium Economy, Available
    (0, 200.00, '27A', 0, 5);   -- Economy, Available