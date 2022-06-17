package com.gbicc.aicr.system.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gbicc.aicr.system.flowable.listener.TaskCompleteListener;
import com.gbicc.aicr.system.service.CommToolService;

@Service
public class CommToolServiceImpl implements CommToolService{
	
	private static final Logger log = LoggerFactory.getLogger(CommToolServiceImpl.class);
	@Autowired private JdbcTemplate jdbcTemplate;
	/**
	 * 生成流水号service
	 * 生成流水号,格式如下：[A-Za-z]{0,7}2013031500001
	 * 
	 * @param prefix
	 * @param format 格式 日期:年月日=Ymd 全年=Y 小年=y 月=m 日=d
	 * @param seqDigit 序列位数
	 */
	@Override
	public synchronized String  getFlowCode(String prefix,String format,String seqDigit){
		Calendar date=Calendar.getInstance();
		int year=date.get(Calendar.YEAR);
		int month=date.get(Calendar.MONTH)+1;
		int day=date.get(Calendar.DATE);
		String code_str="";
		
		if(prefix==null){///可以为空字符
			return code_str;
		}
		if(StringUtils.isBlank(seqDigit)){
			seqDigit="3";//默认
		}
		
		/*前缀*/
		String pmd=null;
		if(StringUtils.isNotBlank(format)){
			if("Ymd".equals(format)){
				pmd=prefix+year+String.format("%02d", month)+String.format("%02d", day);//前缀加年月
			}else if("ymd".equals(format)){
				
			}else if("Y".equals(format)){
				pmd=prefix+year;
			}else if("y".equals(format)){
				
			}else if("m".equals(format)){
				pmd=prefix+String.format("%02d", month);
			}else if("d".equals(format)){
				pmd=prefix+String.format("%02d", day);
			}
		}else{
			pmd=prefix;
		}
		
		//------------------编号
		//step1:如果没有此编号规则，从001开始
		//step2:如果有编码，则+1自增
		try {
			//最大编码
			String sql="SELECT MAX(T.PREFIX||T.SEQNUM) SEQNUM" +
					" FROM T_RCA_FLOW_CODE T WHERE T.PREFIX='"+pmd+"' "; 
			if("".equals(pmd)){
				sql="SELECT MAX(T.PREFIX||T.SEQNUM) SEQNUM" +
					" FROM T_RCA_FLOW_CODE T WHERE T.PREFIX is null";
			}
			Map<String,Object> result=jdbcTemplate.queryForMap(sql);
			String seq="";
			if(result!=null && result.size()>0 && result.get("SEQNUM")!=null){
				seq=result.get("SEQNUM").toString();
				//找到最大编码ID
				String sqlid="SELECT T.ID FROM T_RCA_FLOW_CODE T WHERE T.PREFIX||T.SEQNUM='"+seq+"'"; 
				Map<String,Object> id=jdbcTemplate.queryForMap(sqlid);
				///编号自增
				int tmpl=Integer.parseInt(seq.substring(seq.indexOf(pmd)+pmd.length(),seq.length()))+1;
				code_str=pmd+String.format("%0"+seqDigit+"d", tmpl);
				//更新最大编码序列
				String sqlupt="UPDATE T_RCA_FLOW_CODE t SET t.seqnum='" +
						""+String.format("%0"+seqDigit+"d", tmpl)+"' WHERE t.id='"+id.get("ID")+"'"; 
				jdbcTemplate.execute(sqlupt);
			}else{
				code_str=pmd+String.format("%0"+seqDigit+"d",1);
				String tmp="'"+pmd+"'";
				if("".equals(pmd)){
					tmp=null;
				}
				String sqlupt="INSERT INTO T_RCA_FLOW_CODE VALUES(sys_guid(),"+tmp+",'"+String.format("%0"+seqDigit+"d",1)+"')"; 
				jdbcTemplate.execute(sqlupt);
			}
		} catch (Exception e) {
			log.error("Exception error",e);
		} 
		return code_str;
	}
	
	
	
	
}
