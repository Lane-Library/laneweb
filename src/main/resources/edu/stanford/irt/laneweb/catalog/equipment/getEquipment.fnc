DECLARE
  CURSOR bibid_cursor 
    IS 
        SELECT A.BIB_ID
        FROM LMLDB.BIB_INDEX A, LMLDB.BIB_INDEX B
        WHERE A.NORMAL_HEADING = 'OBJECTS'
        AND B.NORMAL_HEADING = 'SUBSET CIRCBIB'
        AND A.INDEX_CODE = '655H'
        AND B.INDEX_CODE = '655H'
        AND A.BIB_ID <> 296277
        AND A.BIB_ID = B.BIB_ID;
  bibid NUMBER(38,0); 
  bibblob CLOB; 
  equipment CLOB; 
BEGIN 
  OPEN bibid_cursor; 
  DBMS_LOB.CREATETEMPORARY(equipment, TRUE, DBMS_LOB.SESSION); 
  LOOP 
    FETCH bibid_cursor into bibid; 
    EXIT WHEN bibid_cursor%NOTFOUND; 
    bibblob := lmldb.getBibBlob(bibid); 
    DBMS_LOB.COPY(equipment, bibblob, DBMS_LOB.GETLENGTH(bibblob),DBMS_LOB.GETLENGTH(equipment) + 1 ,1); 
  END LOOP; 
  CLOSE bibid_cursor; 
 ? := equipment; 
END;