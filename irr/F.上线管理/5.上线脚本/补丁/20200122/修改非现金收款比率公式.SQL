--修改非现金收款比率公式为
UPDATE T_IRR_INDEX_INFO II SET 
II.INDEX_FORMULA='IF((评估期评估公司原保费收入+评估期内投连险独立账户本年新增交费)==0,null,IF(评估期内非现金收款金额/(评估期评估公司原保费收入+评估期内保户投资款本年新增交费)>1,1,评估期内非现金收款金额/(评估期评估公司原保费收入+评估期内保户投资款本年新增交费)))',
II.INDEX_FORMULA_CODE='IF((OR13002+OR13004)==0,null,IF(OR13038/(OR13002+OR13003)>1,1,OR13038/(OR13002+OR13003)))',
II.INDEX_EVAL_FORM='((OR13002+OR13004)==0?null:(OR13038/(OR13002+OR13003)>1?1:OR13038/(OR13002+OR13003)))'
WHERE II.INDEX_CODE='OR13001';