SELECT DISTINCT(bib_item.bib_id),
  title_brief,
  course_number
FROM lmldb.reserve_list_items,
  lmldb.reserve_list,
  lmldb.reserve_list_courses,
  lmldb.course,
  lmldb.bib_item,
  lmldb.bib_text
WHERE reserve_list.reserve_list_id = reserve_list_items.reserve_list_id
AND reserve_list.reserve_list_id   = reserve_list_courses.reserve_list_id
AND reserve_list_courses.department_id != 22
AND course.course_id               = reserve_list_courses.course_id
AND bib_item.item_id               = reserve_list_items.item_id
AND bib_text.bib_id                = bib_item.bib_id
AND bib_item.bib_id NOT           IN
  (SELECT bib_id
  FROM lmldb.bib_index
  WHERE index_code   = '655H'
  AND normal_heading = 'OBJECTS'
  )
AND expire_date > SYSDATE
ORDER BY title_brief