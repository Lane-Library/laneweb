DECLARE
  CURSOR bibid_cursor 
    IS 
        SELECT BIB_ID
        FROM LMLDB.BIB_INDEX
        WHERE NORMAL_HEADING = 'SOUND RECORDINGS'
        AND INDEX_CODE = '655H'
        AND BIB_ID NOT IN (
            SELECT BIB_ID FROM LMLDB.BIB_INDEX
            WHERE NORMAL_HEADING = 'VISUAL MATERIALS'
            AND INDEX_CODE = '655H'
        )
        AND BIB_ID NOT IN (
            SELECT BIB_ID FROM LMLDB.BIB_INDEX
            WHERE NORMAL_HEADING = 'AUDIOCASSETTES'
            AND INDEX_CODE = '655H'
        );
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