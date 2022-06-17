SELECT F.CLTNUM,ADD_ORGCOD
  FROM (SELECT APP_CUSTOMERNO, ADD_ORGCOD
          FROM (SELECT A.APP_CUSTOMERNO,
                       A.ADD_ORGCOD,
                       ROW_NUMBER() OVER(PARTITION BY A.APP_CUSTOMERNO ORDER BY A.CHDRCOY ASC) NUM
                  FROM (SELECT DISTINCT D.APP_CUSTOMERNO,
                                        P.ADD_ORGCOD,
                                        P.CHDRCOY
                          FROM (SELECT D.APP_CUSTOMERNO, D.INNER_POLNO
                                  FROM BIDW.DW_POL_PERSONER_DETAIL D
                                UNION ALL
                                SELECT D.ISS_CUSTOMERNO, D.INNER_POLNO
                                  FROM BIDW.DW_POL_PERSONER_DETAIL D
                                UNION ALL
                                SELECT D.BNF_CUSTOMERNO, D.INNER_POLNO
                                  FROM BIDW.DW_POL_PERSONER_DETAIL D
                                UNION ALL
                                SELECT D.EB_CUSTOMERNO, D.INNER_POLNO
                                  FROM BIDW.DW_POL_PERSONER_DETAIL D) D,
                               BIDW.LA_F_POLICY P
                         WHERE D.INNER_POLNO = P.INNER_POLNO) A) C
         WHERE C.NUM = 1) E,
       TEMP_LS_20180705 F
 WHERE E.APP_CUSTOMERNO = '9' || TRIM(F.CLTNUM)
