-- Link to question https://www.hackerrank.com/challenges/the-pads/problem

SELECT CONCAT(Name,'(', SUBSTRING(OCCUPATION,1,1),')') FROM OCCUPATIONS
ORDER BY NAME ASC;
SELECT CONCAT('There are a total of ',COUNT(OCCUPATION),' ',LOWER(OCCUPATION),'s.') FROM OCCUPATIONS
GROUP BY OCCUPATION
ORDER BY COUNT(OCCUPATION)ASC;