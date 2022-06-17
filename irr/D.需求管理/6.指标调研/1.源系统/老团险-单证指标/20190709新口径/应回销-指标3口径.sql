
--Ó¦»ØÏú


     SELECT 'OR13015',
                  B.PROVINCECOMCODE,
                 'GP',
                 NVL(COUNT(A.PRTNO),0),
                '2019Q1'
            FROM E_BDS_LISHASL_LZCARDSTATE A
            RIGHT JOIN E_BDS_BIDM_MGECMP B
            ON A.MANAGECOM = B.INNERORGCODE
            AND  TO_CHAR(A.BACKDATE,'YYYYMMDD') BETWEEN '20180401' 
                  AND '20190331'
            GROUP BY B.PROVINCECOMCODE
