SELECT DISTINCT LOWER(LINK_SUBTYPE) || '://' || URL_HOST "HOST"
FROM LMLDB.ELINK_INDEX
WHERE LINK_SUBTYPE         IN ('HTTP','HTTPS')
AND ELINK_INDEX.RECORD_TYPE = 'A'
AND URL_HOST NOT LIKE '%.stanford.edu'
UNION
SELECT DISTINCT LOWER(LINK_SUBTYPE) ||'://' || URL_HOST "HOST"
FROM LMLDB.ELINK_INDEX,
  LMLDB.MFHD_MASTER,
  LMLDB.BIB_MFHD,
  LMLDB.BIB_MASTER
WHERE ELINK_INDEX.RECORD_ID    = MFHD_MASTER.MFHD_ID
AND MFHD_MASTER.MFHD_ID = BIB_MFHD.MFHD_ID
AND BIB_MFHD.BIB_ID = BIB_MASTER.BIB_ID
AND LINK_SUBTYPE              IN ('HTTP','HTTPS')
AND ELINK_INDEX.RECORD_TYPE    = 'M'
AND ELINK_INDEX.RECORD_ID NOT IN
  (SELECT MFHD_ID
  FROM LMLDB.MFHD_DATA
  WHERE LOWER(RECORD_SEGMENT) LIKE '%, noproxy%'
  )
AND URL_HOST NOT LIKE '%.stanford.edu'
AND MFHD_MASTER.SUPPRESS_IN_OPAC != 'Y'
AND BIB_MASTER.SUPPRESS_IN_OPAC != 'Y'