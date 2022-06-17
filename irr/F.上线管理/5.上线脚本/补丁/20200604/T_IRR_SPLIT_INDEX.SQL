--采集分配表，"精算与战略部-总部" 改为 "精算部-总部"
UPDATE T_IRR_SPLIT_INDEX SET SPLIT_NAME=REPLACE(SPLIT_NAME,'(精算与战略部-总部)','(精算部-总部)'),ORG_NAME='精算部-总部' WHERE ORG_NAME LIKE '%精算%';