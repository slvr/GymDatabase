DROP VIEW fulltimeemployees;
DROP VIEW upkeepexpenses;

CREATE VIEW fulltimeemployees (eid,ename,eentrydate,ehoursperweek)
AS SELECT *
FROM employee
WHERE ehoursperweek >= 40;

CREATE VIEW upkeepexpenses (extransnum,extype,examount,froomnum)
AS SELECT extransnum,extype,examount,froomnum
FROM expense
WHERE extype = 'Upkeep';