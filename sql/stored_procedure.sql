--Delete function if it exists and replace with updated version
DROP FUNCTION IF EXISTS member_report(integer);

--This stored procedure will get the total amount of money
--the given member has spent grouped by the type of payment
CREATE OR REPLACE FUNCTION member_report(member_id integer)
RETURNS TABLE (ptype VARCHAR(30), total FLOAT) AS $$
DECLARE
        payments CURSOR FOR SELECT * FROM Payment WHERE Payment.mid = member_id; --Gets all payments by member
        types CURSOR FOR SELECT DISTINCT Payment.ptype FROM Payment WHERE Payment.mid = member_id ORDER BY Payment.ptype DESC; --Gets types of payments made by member
        cost FLOAT := 0; --Local variable to hold the individual costs

        --Variables used to access values in cursors
        cur_type RECORD;
        cur_payment RECORD;
BEGIN
        CREATE TABLE Temp --Temporary table to hold types with costs
        (
                ptype VARCHAR(30),
                amount FLOAT
        );

        --Open cursors
        OPEN types;
        OPEN payments;

        LOOP --Loop over types
                FETCH types INTO cur_type;
                EXIT WHEN NOT FOUND;

                LOOP --Loop over payments
                        FETCH payments INTO cur_payment;
                        EXIT WHEN NOT FOUND;

                        --Add to cost if correct type
                        IF cur_payment.ptype = cur_type.ptype THEN
                                cost := cost + cur_payment.pamount;
                        END IF;
                END LOOP;

                INSERT INTO Temp VALUES (cur_type.ptype, cost); --Add into temp table
                cost := 0; --Reset cost
                MOVE FIRST IN payments; --Reset payments cursor
        END LOOP;

        CLOSE types;
        CLOSE payments;

        RETURN QUERY SELECT * FROM Temp ORDER BY Temp.ptype;
        DROP TABLE Temp;
END; $$

LANGUAGE 'plpgsql';