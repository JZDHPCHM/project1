@echo off
echo ���build�ļ���
rd %~dp0\build\  /s/q
echo ��ʼ���벢����war�ļ�
gradle build
echo �����
pause