--Select number of members taking yoga class--
SELECT COUNT(*)
FROM Signs_up_for
WHERE cid =
(
        SELECT cid
        FROM Class
        WHERE cname = 'Yoga'
);

--Get names of classes starting after 9am--
SELECT cname
FROM Class
WHERE ctime > '2018-01-01 09:00:00';

--Get purchases made by 'Burt Macklin'--
SELECT eqtype
FROM Buys
WHERE mid =
(
        SELECT mid
        FROM Member
        WHERE mname = 'Burt Macklin'
)
GROUP BY eqtype;

--Get number of distinct members in all classes tought by 'Craig Pelton'--
SELECT COUNT(*)
FROM
(
        SELECT mid
        FROM
        (
                SELECT mid, cid
                FROM Signs_up_for
                GROUP BY mid, cid
                HAVING cid IN
                (
                        SELECT cid
                        FROM Teaches
                        WHERE eid =
                        (
                                SELECT eid
                                FROM Employee
                                WHERE ename = 'Craig Pelton'
                        )
                )
        ) AS mem_in_classes
        GROUP BY mid
) AS uniq_mem_in_classes;


--Get most popular item that is bought by members (if tie, show all that are tied)--
SELECT eqtype
FROM Buys
GROUP BY eqtype
HAVING COUNT(*) =
(
        SELECT MAX(count)
        FROM
        (
                SELECT COUNT(*) as count, eqtype
                FROM Buys
                GROUP BY eqtype
        ) as Equip_Counts
);