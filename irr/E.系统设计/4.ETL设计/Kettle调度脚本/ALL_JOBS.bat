rem --------------------------------------------------
rem 每个季度跑一次，需要修改job内时间，和bat脚本中日期
rem --------------------------------------------------

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

if "%md%"=="1012" (
    cd  C:\soft\pdi-ce-6.1.0.1-196\data-integration  
    Kitchen.bat   /rep IRR    /job=ALL_JOBS     > C:\soft\log\ALL_JOBS_%ymd%.log
    echo "Success"
    exit
)else (
    if "%md%"=="0712" (
       cd  C:\soft\pdi-ce-6.1.0.1-196\data-integration  
        Kitchen.bat   /rep IRR    /job=ALL_JOBS     > C:\soft\log\ALL_JOBS_%ymd%.log
       echo "Success"
       exit
    )else (
        if "%md%"=="0412" (
            cd  C:\soft\pdi-ce-6.1.0.1-196\data-integration  
              Kitchen.bat   /rep IRR    /job=ALL_JOBS     > C:\soft\log\ALL_JOBS_%ymd%.log
            echo "Success"
            exit
        )else (
            if "%md%"=="0112" (
                 cd  C:\soft\pdi-ce-6.1.0.1-196\data-integration  
   		  Kitchen.bat   /rep IRR    /job=ALL_JOBS     > C:\soft\log\ALL_JOBS_%ymd%.log
                 echo "Success"
   		 exit
            )
        )
    )
    rem echo 不是每个季度首月12日退出
    rem pause
    exit
)

