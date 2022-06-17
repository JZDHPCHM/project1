--修改公式为不适用
UPDATE T_IRR_INDEX_INFO II SET 
II.INDEX_FORMULA='IF(评估期末非寿险业务应收保费余额==0,''不适用'',评估期末非寿险业务一年期以上应收保费余额/评估期末非寿险业务应收保费余额)',
II.INDEX_FORMULA_CODE='IF(OR13009==0,''不适用'',OR13040/OR13009)',
II.INDEX_EVAL_FORM='(OR13009==0?''不适用'':OR13040/OR13009)',
II.PFM_FORMULA='IF(非寿险业务非正常应收保费比例==''不适用'',3,IF(非寿险业务非正常应收保费比例<=0.03,3,IF(非寿险业务非正常应收保费比例<=0.05,1,0)))',
II.PFM_FORMULA_CODE='IF(''OR13008''==''不适用'',3,IF(OR13008<=0.03,3,IF(OR13008<=0.05,1,0)))',
II.PFM_EVAL_FORM='(''OR13008''==''不适用''?3:(OR13008<=0.03?3:(OR13008<=0.05?1:0)))'
WHERE II.INDEX_CODE='OR13008';

