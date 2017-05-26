SELECT bi.bib_id, COUNT(*) FROM lmldb.bib_item bi,
  lmldb.item_status item_status_1 LEFT OUTER JOIN lmldb.item_status item_status_2
ON (item_status_1.item_id          = item_status_2.item_id
AND item_status_1.item_status_date < item_status_2.item_status_date)
WHERE item_status_2.item_id       IS NULL
AND bi.item_id                     = item_status_1.item_id
AND item_status_1.item_status      = 1
AND bi.bib_id in (select regexp_substr(?,'[^,]+', 1, level) from dual connect by regexp_substr(?, '[^,]+', 1, level) is not null)
GROUP BY bi.bib_id