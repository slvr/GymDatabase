-- using PostgreSQL
-- IS-A Approach: subclass only has key of supperclass and it's own attributes

DROP TABLE Teaches CASCADE;
DROP TABLE Maintains CASCADE;
DROP TABLE Purchases CASCADE;
DROP TABLE Upkeeps CASCADE;
DROP TABLE Pays CASCADE;
DROP TABLE Signs_up_for CASCADE;
DROP TABLE Buys CASCADE;
DROP TABLE Rents CASCADE;
DROP TABLE Expense CASCADE;
DROP TABLE Payment CASCADE;
DROP TABLE Class CASCADE;
DROP TABLE Facility CASCADE;
DROP TABLE Equipment CASCADE;
DROP TABLE Member CASCADE;
DROP TABLE Instructor CASCADE;
DROP TABLE Janitor CASCADE;
DROP TABLE Manager CASCADE;
DROP TABLE Employee CASCADE;

-- *******************
--     Entity Sets
-- *******************

CREATE TABLE Employee
(
    eID INTEGER PRIMARY KEY,
    eName VARCHAR(30),
    eEntryDate DATE,
    eHoursPerWeek FLOAT CHECK (eHoursPerWeek >= 0)
);

CREATE TABLE Instructor -- Employee subclass
(
    eID INTEGER PRIMARY KEY,
    FOREIGN KEY (eID) REFERENCES Employee
);

CREATE TABLE Janitor -- Employee subclass
(
    eID INTEGER PRIMARY KEY,
    FOREIGN KEY (eID) REFERENCES Employee
);

CREATE TABLE Manager -- Employee subclass
(
    mpassword VARCHAR(20) NOT NULL,
    eID INTEGER PRIMARY KEY,
    FOREIGN KEY (eID) REFERENCES Employee
);

CREATE TABLE Facility
(
    fRoomNum INTEGER PRIMARY KEY,
    fRoomType VARCHAR(30),
    fCapacity INTEGER
);

CREATE TABLE Class
(
    cID INTEGER PRIMARY KEY,
    cName VARCHAR(30),
    cTime TIMESTAMP,
    -- impose 'At Facility' relationship
    fRoomNum INTEGER NOT NULL,
    FOREIGN KEY (fRoomNum) REFERENCES Facility
);

CREATE TABLE Equipment
(
    eqNum INTEGER,
    eqType VARCHAR(30),
    eqSerialNum VARCHAR(30),
    PRIMARY KEY (eqNum, eqType)
);

CREATE TABLE Member
(
    mID INTEGER PRIMARY KEY,
    mName VARCHAR(30),
    mPhone INTEGER,
    mAddress VARCHAR(50)
);
CREATE TABLE Expense
(
    exTransNum INTEGER PRIMARY KEY,
    exType VARCHAR(30),
    exAmount FLOAT,
    eID INTEGER,
    fRoomNum INTEGER,
    eqNum INTEGER,
    eqType VARCHAR(30),
    FOREIGN KEY (eID) REFERENCES Employee,
    FOREIGN KEY (fRoomNum) REFERENCES Facility,
    FOREIGN KEY (eqNum,eqType) REFERENCES Equipment,
    -- enforce one and only one of its participation constraints
    -- bracketing ensures that clauses are always compared in pairs
    CONSTRAINT ex_chk_null CHECK (
        (eID IS NOT NULL AND (fRoomNum IS NULL AND (eqNum IS NULL AND eqType IS NULL)))
        OR
        ((fRoomNum IS NOT NULL AND (eID IS NULL AND (eqNum IS NULL AND eqType IS NULL)))
        OR
        (eqNum IS NOT NULL AND (eqType IS NOT NULL AND (eID IS NULL AND fRoomNum IS NULL))))
    )
);

CREATE TABLE Payment
(
    pTransNum INTEGER PRIMARY KEY,
    pType VARCHAR(30),
    pAmount FLOAT,
    mID INTEGER NOT NULL,
    cID INTEGER,
    fRoomNum INTEGER,
    eqNum INTEGER,
    eqType VARCHAR(30),
    FOREIGN KEY (mID) REFERENCES Member,
    FOREIGN KEY (cID) REFERENCES Class,
    FOREIGN KEY (fRoomNum) REFERENCES Facility,
    FOREIGN KEY (eqNum,eqType) REFERENCES Equipment,
    -- enforce one and only one of its participation constraints
    -- bracketing ensures that clauses are always compared in pairs
    CONSTRAINT p_chk_null CHECK (
        (cID IS NOT NULL AND (fRoomNum IS NULL AND (eqNum IS NULL AND eqType IS NULL)))
        OR
        ((fRoomNum IS NOT NULL AND (cID IS NULL AND (eqNum IS NULL AND eqType IS NULL)))
        OR
        (eqNum IS NOT NULL AND (eqType IS NOT NULL AND (cID IS NULL AND fRoomNum IS NULL))))
    )
);

-- *******************
--    Relationships
-- *******************

CREATE TABLE Teaches
(
    cID INTEGER,
    eID INTEGER,
    PRIMARY KEY (cID, eID),
    FOREIGN KEY (cID) REFERENCES Class,
    FOREIGN KEY (eID) REFERENCES Instructor
);

CREATE TABLE Maintains
(
    fRoomNum INTEGER,
    eID INTEGER,
    PRIMARY KEY (fRoomNum,eID),
    FOREIGN KEY (fRoomNum) REFERENCES Facility,
    FOREIGN KEY (eID) REFERENCES Janitor
);

CREATE TABLE Purchases
(
    eqNum INTEGER,
    eqType VARCHAR(30),
    eID INTEGER,
    PRIMARY KEY (eqNum, eqType, eID),
    FOREIGN KEY (eqNum,eqType) REFERENCES Equipment,
    FOREIGN KEY (eID) REFERENCES Manager
);

CREATE TABLE Upkeeps
(
    fRoomNum INTEGER,
    eID INTEGER,
    PRIMARY KEY (fRoomNum, eID),
    FOREIGN KEY (fRoomNum) REFERENCES Facility,
    FOREIGN KEY (eID) REFERENCES Manager
);

CREATE TABLE Pays
(
    -- rename eID to differentiate managers and employees
    managerID INTEGER,
    employeeID INTEGER,
    salary FLOAT CHECK (salary >= 0),
    PRIMARY KEY (managerID, employeeID),
    FOREIGN KEY (managerID) REFERENCES Manager(eID),
    FOREIGN KEY (employeeID) REFERENCES Employee(eID)
);

CREATE TABLE Signs_up_for
(
    mID INTEGER,
    cID INTEGER,
    PRIMARY KEY (mID, cID),
    FOREIGN KEY (mID) REFERENCES Member,
    FOREIGN KEY (cID) REFERENCES Class
);

CREATE TABLE Buys
(
    mID INTEGER,
    eqNum INTEGER,
    eqType VARCHAR(30),
    PRIMARY KEY (mID, eqNum, eqType),
    FOREIGN KEY (mID) REFERENCES Member,
    FOREIGN KEY (eqNum,eqType) REFERENCES Equipment
);

CREATE TABLE Rents
(
    mID INTEGER,
    fRoomNum INTEGER,
    timeSlot TIMESTAMP,
    PRIMARY KEY (mID, fRoomNum),
    FOREIGN KEY (mID) REFERENCES Member,
    FOREIGN KEY (fRoomNum) REFERENCES Facility
);