CREATE FUNCTION FUN_ETL_Q(V_INDATE IN VARCHAR2) RETURN VARCHAR2 AS
/**********************************************************************/
/* Procedure Name : 输入任意时间返回上个评估期                        */
/* Developed By   : WH                                                */
/* Developed Date : 2018-10-31                                        */
/**********************************************************************/
  RTN_DATE VARCHAR2(8);
  M_DATE   VARCHAR2(8) := TO_CHAR(TO_DATE(V_INDATE, 'YYYYMMDD'), 'q');                 --所在月份
  Y_DATE   VARCHAR2(8) := TO_CHAR(TO_DATE(V_INDATE, 'YYYYMMDD'), 'YYYY');               --所在年份
  F_Y_DATE VARCHAR2(8) := TO_CHAR(TRUNC(TO_DATE(V_INDATE, 'YYYYMMDD'),'yyyy')-1,'YYYY');--上一年份yyyy
BEGIN
  IF M_DATE =1 THEN
    SELECT F_Y_DATE || 'Q4' INTO RTN_DATE FROM DUAL;
  ELSIF M_DATE =2 THEN
    SELECT Y_DATE || 'Q1' INTO RTN_DATE FROM DUAL;
  ELSIF M_DATE =3 THEN
    SELECT Y_DATE || 'Q2' INTO RTN_DATE FROM DUAL;
  ELSIF M_DATE =4 THEN
    SELECT Y_DATE || 'Q3' INTO RTN_DATE FROM DUAL;
  END IF;
  RETURN(RTN_DATE);
END;

/
CREATE FUNCTION FUN_ETL_Q_DATE(V_INDATE IN VARCHAR2) RETURN VARCHAR2 AS
/**********************************************************************/
/* Procedure Name : 输入任意时间返回上季度末日期                      */
/* Developed By   : WH                                                */
/* Developed Date : 2018-10-31                                        */
/**********************************************************************/
  RTN_DATE VARCHAR2(8);
  M_DATE   VARCHAR2(8) := TO_CHAR(TO_DATE(V_INDATE, 'YYYYMMDD'), 'MM');                 --所在月份
  Y_DATE   VARCHAR2(8) := TO_CHAR(TO_DATE(V_INDATE, 'YYYYMMDD'), 'YYYY');               --所在年份
  F_Y_DATE VARCHAR2(8) := TO_CHAR(TRUNC(TO_DATE(V_INDATE, 'YYYYMMDD'),'yyyy')-1,'YYYY');--上一年份yyyy
BEGIN
  IF M_DATE IN ('01', '02', '03') THEN
    SELECT F_Y_DATE || '1231' INTO RTN_DATE FROM DUAL;
  ELSIF M_DATE IN ('04', '05', '06') THEN
    SELECT Y_DATE || '0331' INTO RTN_DATE FROM DUAL;
  ELSIF M_DATE IN ('07', '08', '09') THEN
    SELECT Y_DATE || '0630' INTO RTN_DATE FROM DUAL;
  ELSIF M_DATE IN ('10', '11', '12') THEN
    SELECT Y_DATE || '0930' INTO RTN_DATE FROM DUAL;
  END IF;
  RETURN(RTN_DATE);
END;

/
CREATE FUNCTION GET_UUID RETURN VARCHAR IS
  GUID VARCHAR(50);
BEGIN
  GUID := LOWER(RAWTOHEX(SYS_GUID()));
  RETURN SUBSTR(GUID, 1, 8) || '-' || SUBSTR(GUID, 9, 4) || '-' || SUBSTR(GUID,
                                                                          13,
                                                                          4) || '-' || SUBSTR(GUID,
                                                                                              17,
                                                                                              4) || '-' || SUBSTR(GUID,
                                                                                                                  21,
                                                                                                                  12);
END GET_UUID;


/

