-- All instructors receive a 10% raise

UPDATE pays p
SET salary = salary*1.1
FROM instructor AS i
WHERE (i.eid = p.employeeid);

-- Since Wall E does not exist, reduce his salary and hours to 0, remove his cleaning duties, re-assign them if needed

UPDATE employee e
SET ehoursperweek = 0
WHERE e.ename = 'Wall E';

UPDATE pays p
SET salary = 0
FROM employee AS e
WHERE (p.employeeid = e.eid) AND (e.ename = 'Wall E');

UPDATE maintains m
SET eid = 8
FROM (SELECT froomnum, COUNT(froomnum) as num FROM maintains GROUP BY froomnum) m1
WHERE (m.eid = 9) AND (m1.num = 1);

-- Employees are no longer allowed to work more than 40 hours/week

UPDATE employee
SET ehoursperweek = 40
WHERE ehoursperweek > 40;

-- Some members move in with other members, change addresses to match

UPDATE member m
SET maddress = m1.maddress
FROM member AS m1
WHERE (m.mname = 'Abed Nadir' OR m.mname = 'Troy Barnes')
AND (m1.mname = 'Philip Fry');