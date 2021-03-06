CREATE TABLE TESTSERVICERESULT (
	ID        NUMBER(*,0) NOT NULL ENABLE,
	CODE      VARCHAR2(50) NOT NULL ENABLE,
	"NUMBER"  NUMBER(*,0),
	FILENAMES VARCHAR2(100),
	ERROR     VARCHAR2(100),
	 CONSTRAINT PK$TESTSERVICERESULT_ID PRIMARY KEY (ID)
);

CREATE SEQUENCE id_seq;

CREATE OR REPLACE TRIGGER id_on_ins
  BEFORE INSERT ON TESTSERVICERESULT
  FOR EACH ROW
BEGIN
  SELECT id_seq.nextval
  INTO :new.id
  FROM dual;
END;
