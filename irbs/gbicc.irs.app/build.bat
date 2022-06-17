@echo off
echo 清空build文件夹
rd %~dp0\build\  /s/q
echo 开始编译并生成war文件
gradle build
echo 已完成
pause