@echo off
color 0A
::sm��ȡ��ǰ���� m��ȡ��ǰ�·� d��ǰ���� 
set sm=%date:~0,10% %time:~0,5%  
set y=%sm:~0,4%
set m=%sm:~5,2%
set d=%sm:~8,2%
set md=%m%%d%
set ymd=%y%%m%%d%


echo %md%
sqlplus irr/irr@127.0.0.1:1521/irr @all_job.sql>C:\oracle\sql_all\log_%ymd%.log
sqlplus irr/irr@127.0.0.1:1521/irr @C:\oracle\sql_all\all_job.sql