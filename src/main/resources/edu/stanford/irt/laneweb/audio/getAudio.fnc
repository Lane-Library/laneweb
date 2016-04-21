DECLARE
  CURSOR bibid_cursor 
    IS 
        SELECT BIB_INDEX.BIB_ID
        FROM LMLDB.BIB_INDEX,
            LMLDB.BIB_MASTER
        WHERE NORMAL_HEADING = 'AUDIO DIGEST FOUNDATION'
        AND INDEX_CODE = '710H'
        AND BIB_INDEX.BIB_ID = BIB_MASTER.BIB_ID
        AND SUPPRESS_IN_OPAC != 'Y';
  bibid NUMBER(38,0); 
  bibblob CLOB; 
  audio CLOB; 
BEGIN 
  OPEN bibid_cursor; 
  DBMS_LOB.CREATETEMPORARY(audio, TRUE, DBMS_LOB.SESSION); 
  LOOP 
    FETCH bibid_cursor into bibid; 
    EXIT WHEN bibid_cursor%NOTFOUND; 
    bibblob := lmldb.getBibBlob(bibid); 
    DBMS_LOB.COPY(audio, bibblob, DBMS_LOB.GETLENGTH(bibblob),DBMS_LOB.GETLENGTH(audio) + 1 ,1); 
  END LOOP; 
  CLOSE bibid_cursor; 
 ? := audio; 
END;