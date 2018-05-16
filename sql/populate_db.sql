--- Employee, Instructor, Janitor, Manager ---
-- Manager
INSERT INTO Employee VALUES (1, 'Evan Laflamme', '01-01-2018', 40);
INSERT INTO Manager VALUES (1, 'ev4N');

INSERT INTO Employee VALUES (2, 'Daniel Meland', '01-01-2018', 40);
INSERT INTO Manager VALUES (2, 'd4Niel');

INSERT INTO Employee VALUES (3, 'Jastaj Virdee', '01-01-2018', 40);
INSERT INTO Manager VALUES (3, 'j4Staj');

INSERT INTO Employee VALUES (4, 'Alec Parent', '01-01-2018', 40);
INSERT INTO Manager VALUES (4, 'al3C');

-- Janitor
INSERT INTO Employee VALUES (5, 'Argus Filch', '01-01-1960', 40);
INSERT INTO Janitor VALUES (5);

INSERT INTO Employee VALUES (6, 'Scruffy', '01-01-1970', 40);
INSERT INTO Janitor VALUES (6);

INSERT INTO Employee VALUES (7, 'Groundskeeper Willie', '01-01-1980', 40);
INSERT INTO Janitor VALUES (7);

INSERT INTO Employee VALUES (8, 'Charlie Kelly', '01-01-2010', 30);
INSERT INTO Janitor VALUES (8);

INSERT INTO Employee VALUES (9, 'Wall E', '01-01-2050', 60);
INSERT INTO Janitor VALUES (9);

-- Instructor
INSERT INTO Employee VALUES (10, 'Jeff Winger', '01-09-2010', 35);
INSERT INTO Instructor VALUES (10);

INSERT INTO Employee VALUES (11, 'Ian Duncan', '01-09-2006', 35);
INSERT INTO Instructor VALUES (11);

INSERT INTO Employee VALUES (12, 'Ben Chang', '01-01-2008', 35);
INSERT INTO Instructor VALUES (12);

INSERT INTO Employee VALUES (13, 'Elroy Patashnik', '01-01-1990', 40);
INSERT INTO Instructor VALUES (13);

INSERT INTO Employee VALUES (14, 'Craig Pelton', '01-01-2000', 50);
INSERT INTO Instructor VALUES (14);


--- Facility ---
INSERT INTO Facility VALUES (100, 'Dance Studio 1', 30);
INSERT INTO Facility VALUES (101, 'Dance Studio 2', 50);
INSERT INTO Facility VALUES (200, 'Pool', 300);
INSERT INTO Facility VALUES (300, 'Gym', 100);
INSERT INTO Facility VALUES (400, 'Squash Courts', 20);
INSERT INTO Facility VALUES (500, 'Locker Rooms', 50);


--- Class ---
INSERT INTO Class VALUES (1, 'Spinning', '2018-01-01 10:00:00', 100);
INSERT INTO Class VALUES (2, 'Workout', '2018-01-01 16:00:00', 300);
INSERT INTO Class VALUES (3, 'Swimming', '2018-01-01 08:00:00', 200);
INSERT INTO Class VALUES (4, 'Yoga', '2018-01-01 09:00:00', 100);
INSERT INTO Class VALUES (5, 'Pilates', '2018-01-01 09:00:00', 101);


--- Equipment ---
INSERT INTO Equipment VALUES (1, 'Squash Racket', 'S100');
INSERT INTO Equipment VALUES (2, 'Squash Racket', 'S100');
INSERT INTO Equipment VALUES (3, 'Squash Racket', 'S100');
INSERT INTO Equipment VALUES (1, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (2, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (3, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (4, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (5, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (6, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (7, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (8, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (9, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (10, 'Swimming Goggles', 'SW100');
INSERT INTO Equipment VALUES (1, 'Bathing Cap', 'SW200');
INSERT INTO Equipment VALUES (2, 'Bathing Cap', 'SW200');
INSERT INTO Equipment VALUES (3, 'Bathing Cap', 'SW200');
INSERT INTO Equipment VALUES (1, 'Yoga Mat', 'Y100');
INSERT INTO Equipment VALUES (2, 'Yoga Mat', 'Y100');
INSERT INTO Equipment VALUES (3, 'Yoga Mat', 'Y100');
INSERT INTO Equipment VALUES (4, 'Yoga Mat', 'Y100');
INSERT INTO Equipment VALUES (5, 'Yoga Mat', 'Y100');
INSERT INTO Equipment VALUES (1, 'Yoga Ball', 'Y200');
INSERT INTO Equipment VALUES (2, 'Yoga Ball', 'Y200');
INSERT INTO Equipment VALUES (3, 'Yoga Ball', 'Y200');
INSERT INTO Equipment VALUES (4, 'Yoga Ball', 'Y200');
INSERT INTO Equipment VALUES (5, 'Yoga Ball', 'Y200');


--- Member ---
INSERT INTO Member VALUES (1, 'Morgan Freeman', 5550000, '1 Hollywood Lane');
INSERT INTO Member VALUES (2, 'Tom Cruise', 5551234, '10 Coolsville Road');
INSERT INTO Member VALUES (3, 'John Doe', 5554321, '123 Road Street');
INSERT INTO Member VALUES (4, 'Jane Doe', 5554321, '123 Road Street');
INSERT INTO Member VALUES (5, 'Burt Macklin', 5551111, '99 Pawnee Avenue');
INSERT INTO Member VALUES (6, 'Philip Fry', 5559876, '0 New New York Street');
INSERT INTO Member VALUES (7, 'Sterling Archer', 5551235, '33 Secret Road');
INSERT INTO Member VALUES (8, 'Ted Mosby', 5552222, '115 10th Avenue');
INSERT INTO Member VALUES (9, 'Abed Nadir', 5559999, '100 Greendale Avenue');
INSERT INTO Member VALUES (10, 'Troy Barnes', 5558888, '100 Greendale Avenue');


--- Teaches ---
INSERT INTO Teaches VALUES (1, 10);
INSERT INTO Teaches VALUES (1, 11);
INSERT INTO Teaches VALUES (2, 13);
INSERT INTO Teaches VALUES (3, 12);
INSERT INTO Teaches VALUES (4, 14);
INSERT INTO Teaches VALUES (5, 14);


--- Maintains ---
INSERT INTO Maintains VALUES (100, 5);
INSERT INTO Maintains VALUES (101, 6);
INSERT INTO Maintains VALUES (200, 7);
INSERT INTO Maintains VALUES (200, 9);
INSERT INTO Maintains VALUES (300, 6);
INSERT INTO Maintains VALUES (300, 9);
INSERT INTO Maintains VALUES (400, 8);
INSERT INTO Maintains VALUES (500, 9);


--- Purchases ---
INSERT INTO Purchases VALUES (2, 'Squash Racket', 1);
INSERT INTO Purchases VALUES (3, 'Squash Racket', 1);
INSERT INTO Purchases VALUES (1, 'Bathing Cap', 2);
INSERT INTO Purchases VALUES (2, 'Bathing Cap', 2);
INSERT INTO Purchases VALUES (3, 'Bathing Cap', 2);
INSERT INTO Purchases VALUES (5, 'Yoga Ball', 3);


--- Upkeeps ---
INSERT INTO Upkeeps VALUES (100, 2);
INSERT INTO Upkeeps VALUES (101, 2);
INSERT INTO Upkeeps VALUES (200, 1);
INSERT INTO Upkeeps VALUES (300, 3);
INSERT INTO Upkeeps VALUES (400, 4);
INSERT INTO Upkeeps VALUES (500, 4);


--- Pays ---
INSERT INTO Pays VALUES (1, 5, 22.0);
INSERT INTO Pays VALUES (2, 6, 30.5);
INSERT INTO Pays VALUES (3, 7, 26.5);
INSERT INTO Pays VALUES (4, 8, 5.5);
INSERT INTO Pays VALUES (1, 9, 1.0);
INSERT INTO Pays VALUES (2, 10, 30.0);
INSERT INTO Pays VALUES (3, 11, 30.0);
INSERT INTO Pays VALUES (4, 12, 20.0);
INSERT INTO Pays VALUES (1, 13, 40.0);
INSERT INTO Pays VALUES (2, 14, 45.0);


--- Signs_up_for ---
INSERT INTO Signs_up_for VALUES (1, 1);
INSERT INTO Signs_up_for VALUES (2, 2);
INSERT INTO Signs_up_for VALUES (3, 4);
INSERT INTO Signs_up_for VALUES (4, 4);
INSERT INTO Signs_up_for VALUES (5, 2);
INSERT INTO Signs_up_for VALUES (5, 3);
INSERT INTO Signs_up_for VALUES (7, 4);
INSERT INTO Signs_up_for VALUES (8, 1);
INSERT INTO Signs_up_for VALUES (8, 2);
INSERT INTO Signs_up_for VALUES (8, 3);
INSERT INTO Signs_up_for VALUES (8, 4);
INSERT INTO Signs_up_for VALUES (8, 5);
INSERT INTO Signs_up_for VALUES (9, 3);
INSERT INTO Signs_up_for VALUES (10, 3);
INSERT INTO Signs_up_for VALUES (9, 1);
INSERT INTO Signs_up_for VALUES (10, 2);


--- Buys ---
INSERT INTO Buys VALUES (1, 1, 'Squash Racket');
INSERT INTO Buys VALUES (5, 1, 'Swimming Goggles');
INSERT INTO Buys VALUES (5, 2, 'Swimming Goggles');
INSERT INTO Buys VALUES (3, 1, 'Yoga Mat');
INSERT INTO Buys VALUES (3, 2, 'Yoga Mat');
INSERT INTO Buys VALUES (5, 1, 'Bathing Cap');
INSERT INTO Buys VALUES (7, 1, 'Yoga Ball');


--- Rents ---
INSERT INTO Rents VALUES (1, 100, '2018-01-01 15:00:00');
INSERT INTO Rents VALUES (3, 200, '2018-01-01 10:00:00');
INSERT INTO Rents VALUES (4, 200, '2018-01-01 19:00:00');
INSERT INTO Rents VALUES (8, 300, '2018-01-01 08:00:00');
INSERT INTO Rents VALUES (9, 400, '2018-01-01 10:00:00');
INSERT INTO Rents VALUES (10, 400, '2018-01-01 12:00:00');


--- Expense ---
INSERT INTO Expense VALUES (1, 'Salary', 880.0, 5, NULL, NULL, NULL);
INSERT INTO Expense VALUES (2, 'Salary', 1220.0, 6, NULL, NULL, NULL);
INSERT INTO Expense VALUES (3, 'Salary', 1060.0, 7, NULL, NULL, NULL);
INSERT INTO Expense VALUES (4, 'Salary', 165.0, 8, NULL, NULL, NULL);
INSERT INTO Expense VALUES (5, 'Salary', 60.0, 9, NULL, NULL, NULL);
INSERT INTO Expense VALUES (6, 'Salary', 1050.0, 10, NULL, NULL, NULL);
INSERT INTO Expense VALUES (7, 'Salary', 1050.0, 11, NULL, NULL, NULL);
INSERT INTO Expense VALUES (8, 'Salary', 700.0, 12, NULL, NULL, NULL);
INSERT INTO Expense VALUES (9, 'Salary', 1600.0, 13, NULL, NULL, NULL);
INSERT INTO Expense VALUES (10, 'Salary', 2250.0, 14, NULL, NULL, NULL);
INSERT INTO Expense VALUES (11, 'Upkeep', 50.0, NULL, 100, NULL, NULL);
INSERT INTO Expense VALUES (12, 'Upkeep', 75.0, NULL, 101, NULL, NULL);
INSERT INTO Expense VALUES (13, 'Upkeep', 500.0, NULL, 200, NULL, NULL);
INSERT INTO Expense VALUES (14, 'Upkeep', 300.0, NULL, 300, NULL, NULL);
INSERT INTO Expense VALUES (15, 'Upkeep', 50.0, NULL, 400, NULL, NULL);
INSERT INTO Expense VALUES (16, 'Upkeep', 100.0, NULL, 500, NULL, NULL);
INSERT INTO Expense VALUES (17, 'Purchase', 20.0, NULL, NULL, 2, 'Squash Racket');
INSERT INTO Expense VALUES (18, 'Purchase', 20.0, NULL, NULL, 3, 'Squash Racket');
INSERT INTO Expense VALUES (19, 'Purchase', 5.0, NULL, NULL, 1, 'Bathing Cap');
INSERT INTO Expense VALUES (20, 'Purchase', 5.0, NULL, NULL, 2, 'Bathing Cap');
INSERT INTO Expense VALUES (21, 'Purchase', 5.0, NULL, NULL, 3, 'Bathing Cap');
INSERT INTO Expense VALUES (22, 'Purchase', 15.0, NULL, NULL, 5, 'Yoga Ball');


--- Payment ---
INSERT INTO Payment VALUES (1, 'Sign Up', 50.0, 1, 1, NULL, NULL, NULL);
INSERT INTO Payment VALUES (2, 'Sign Up', 100.0, 2, 2, NULL, NULL, NULL);
INSERT INTO Payment VALUES (3, 'Sign Up', 75.0, 3, 4, NULL, NULL, NULL);
INSERT INTO Payment VALUES (4, 'Sign Up', 75.0, 4, 4, NULL, NULL, NULL);
INSERT INTO Payment VALUES (5, 'Sign Up', 100.0, 5, 2, NULL, NULL, NULL);
INSERT INTO Payment VALUES (6, 'Sign Up', 100.0, 5, 3, NULL, NULL, NULL);
INSERT INTO Payment VALUES (7, 'Sign Up', 75.0, 7, 4, NULL, NULL, NULL);
INSERT INTO Payment VALUES (8, 'Sign Up', 50.0, 8, 1, NULL, NULL, NULL);
INSERT INTO Payment VALUES (9, 'Sign Up', 100.0, 8, 2, NULL, NULL, NULL);
INSERT INTO Payment VALUES (10, 'Sign Up', 100.0, 8, 3, NULL, NULL, NULL);
INSERT INTO Payment VALUES (11, 'Sign Up', 75.0, 8, 4, NULL, NULL, NULL);
INSERT INTO Payment VALUES (12, 'Sign Up', 80.0, 8, 5, NULL, NULL, NULL);
INSERT INTO Payment VALUES (13, 'Sign Up', 100.0, 9, 3, NULL, NULL, NULL);
INSERT INTO Payment VALUES (14, 'Sign Up', 100.0, 10, 3, NULL, NULL, NULL);
INSERT INTO Payment VALUES (15, 'Sign Up', 50.0, 9, 1, NULL, NULL, NULL);
INSERT INTO Payment VALUES (16, 'Sign Up', 100.0, 10, 2, NULL, NULL, NULL);
INSERT INTO Payment VALUES (17, 'Buy', 27.50, 1, NULL, NULL, 1, 'Squash Racket');
INSERT INTO Payment VALUES (18, 'Buy', 10.50, 5, NULL, NULL, 1, 'Swimming Goggles');
INSERT INTO Payment VALUES (19, 'Buy', 10.50, 5, NULL, NULL, 2, 'Swimming Goggles');
INSERT INTO Payment VALUES (20, 'Buy', 16.50, 3, NULL, NULL, 1, 'Yoga Mat');
INSERT INTO Payment VALUES (21, 'Buy', 16.50, 3, NULL, NULL, 2, 'Yoga Mat');
INSERT INTO Payment VALUES (22, 'Buy', 6.00, 5, NULL, NULL, 1, 'Bathing Cap');
INSERT INTO Payment VALUES (23, 'Buy', 16.50, 7, NULL, NULL, 1, 'Yoga Ball');
INSERT INTO Payment VALUES (24, 'Rent', 60, 1, NULL, 100, NULL, NULL);
INSERT INTO Payment VALUES (25, 'Rent', 100, 3, NULL, 200, NULL, NULL);
INSERT INTO Payment VALUES (26, 'Rent', 100, 4, NULL, 200, NULL, NULL);
INSERT INTO Payment VALUES (27, 'Rent', 250, 8, NULL, 300, NULL, NULL);
INSERT INTO Payment VALUES (28, 'Rent', 50, 9, NULL, 400, NULL, NULL);
INSERT INTO Payment VALUES (29, 'Rent', 50, 10, NULL, 400, NULL, NULL);