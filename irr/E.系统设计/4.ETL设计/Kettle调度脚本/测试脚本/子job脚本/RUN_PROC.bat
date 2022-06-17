@echo off
color 0A
::sm获取当前日期 m获取当前月份 d当前日期 
set sm=%date:~0,10% %time:~0,5%  
set y=%sm:~0,4%
set m=%sm:~5,2%
set d=%sm:~8,2%
set md=%m%%d%
set ymd=%y%%m%%d%


echo %md%





cd  C:\soft\pdi-ce-6.1.0.1-196\data-integration  
Kitchen.bat   /rep IRR    /job=RUN_PROC     > C:\soft\log\RUN_PROC_%ymd%.log
echo "Success"
exit











