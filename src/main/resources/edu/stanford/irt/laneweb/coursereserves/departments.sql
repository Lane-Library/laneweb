SELECT DISTINCT(department.department_id),
  department_name
FROM lmldb.department,
  lmldb.reserve_list,
  lmldb.reserve_list_courses
WHERE department.department_id  != 22
AND department.department_id    != 11
AND department.department_id    != 8
AND reserve_list.reserve_list_id = reserve_list_courses.reserve_list_id
AND department.department_id     = reserve_list_courses.department_id
AND expire_date                  > SYSDATE
ORDER BY department_name