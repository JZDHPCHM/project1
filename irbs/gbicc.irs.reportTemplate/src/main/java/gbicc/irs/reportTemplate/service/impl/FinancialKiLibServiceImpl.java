package gbicc.irs.reportTemplate.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.jpa.service.support.DaoExceptionProvider;
import org.wsp.framework.jpa.service.support.DaoExceptionProviderAdaptor;

import com.googlecode.aviator.AviatorEvaluator;

import gbicc.irs.reportTemplate.jpa.entity.FinancialKiLib;
import gbicc.irs.reportTemplate.jpa.repository.FinancialKiLibRepository;
import gbicc.irs.reportTemplate.jpa.support.FinancialStatementsWrap;
import gbicc.irs.reportTemplate.jpa.support.OutlierHandlingType;
import gbicc.irs.reportTemplate.service.FinancialBasicsLibService;
import gbicc.irs.reportTemplate.service.FinancialKiLibService;

@Service
public class FinancialKiLibServiceImpl extends DaoServiceImpl<FinancialKiLib, String, FinancialKiLibRepository>
		implements FinancialKiLibService {
	private static Log log = LogFactory.getLog(FinancialKiLibServiceImpl.class);
	@Autowired
	private FinancialBasicsLibService financialBasicsLibService;
	
	@Override
	public DaoExceptionProvider<FinancialKiLib, String> getExceptionProvider() {
		return new DaoExceptionProviderAdaptor<FinancialKiLib, String>(){
			@Override
			public Exception getCreateObjectAlreadyExistsException(FinancialKiLib entity) {
				return new RuntimeException("指标编号或名称已存在！");
			}
		};
	}

	@Override
	protected FinancialKiLib findByUniqueKey(FinancialKiLib entity) throws Exception {
		List<FinancialKiLib> resuts = repository.findByKiCodeOrKiName(entity.getKiCode(), entity.getKiName());
		if(resuts != null && resuts.size() > 0) {
			return resuts.get(0);
		}
		return null;
	}
	
	
	@Override
	public Map<String, Object> calculateKiLib(List<FinancialStatementsWrap> finStats) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		log.info("开始计算基础指标层...");
		//计算基础指标
		Map<String, Object> kiParameters = financialBasicsLibService.calculateBaseLib(finStats);
		log.info("基础指标层计算完毕!");
		log.info("基础指标层计算结果:"+kiParameters);
		
		log.info("开始模型指标...");
		//获取需要计算的指标
		List<FinancialKiLib> kiLibs = repository.findAll();
		for(FinancialKiLib kiLib : kiLibs) {
			log.info("开始计算模型指标:"+kiLib.getKiCode());
			//指标值
			Double kiResult = null;
			//分子
			String numerator = kiLib.getKiNumerator();
			//分子结果
			Double numeratorRes = null;
			if(StringUtils.hasText(numerator)) {
				Object tempResult = null;
				try {
					tempResult = AviatorEvaluator.execute(numerator,kiParameters);
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
				if(tempResult!=null) {
					if(Double.isNaN(Double.valueOf(tempResult.toString())) || Double.isInfinite(Double.valueOf(tempResult.toString()))) {
						tempResult = null;
					}
				}
				numeratorRes = tempResult==null?null:Double.valueOf(tempResult.toString());
			}
			//分母
			String denominator = kiLib.getKiDenominator();
			//分母结果
			Double denominatorRes = null;
			if(StringUtils.hasText(denominator)){
				Object tempResult = null;
				try {
					tempResult = AviatorEvaluator.execute(denominator,kiParameters);
				} catch (Exception e) {
					log.error(e);
				}
				if(tempResult!=null) {
					if(Double.isNaN(Double.valueOf(tempResult.toString())) || Double.isInfinite(Double.valueOf(tempResult.toString()))) {
						tempResult = null;
					}
				}
				denominatorRes = tempResult==null?null:Double.valueOf(tempResult.toString());
			}
			kiResult = this.outlierHandlingprocessor(numeratorRes, denominatorRes, kiLib.getOutlierHandling());
			log.info("模型指标:"+kiLib.getKiCode()+"计算结果为:"+kiResult);
			resultMap.put(kiLib.getKiCode(), kiResult);
		}
		log.info("模型指标计算完毕！");
		log.info("模型指标计算结果:"+resultMap);
		return resultMap;
	}

	/**
	 * 指标异常值处理
	 * @param numeratorRes
	 * @param denominatorRes
	 * @param outlierHandlingType
	 * @return
	 */
	private Double outlierHandlingprocessor(Double numeratorRes,Double denominatorRes,OutlierHandlingType outlierHandlingType) {
		Double kiResult = null;
		
		if(numeratorRes == null || denominatorRes == null) {
			return kiResult;
		}
		if(outlierHandlingType == null) {
			if(denominatorRes == 0d) {
				return kiResult = 0d;
			}
			kiResult = numeratorRes / denominatorRes;
		}else {
			//异常值处理
			if(outlierHandlingType == OutlierHandlingType.DEAL_ONE) {
				//特殊处理1	del_nega_rec：1
				//若分母<0，则指标=“缺失”；
				//若分母=0，则指标=“分子”（分母置为1）；
				if(denominatorRes == null || denominatorRes < 0) {
					kiResult = 0d;
				}else if(denominatorRes == 0) {
					kiResult = numeratorRes/1;
				}else {
					kiResult = numeratorRes / denominatorRes;
				}
			}else if(outlierHandlingType == OutlierHandlingType.DEAL_TWO) {
				//特殊处理2	del_nega_rec：DMT=1
				//若分母<=0，则指标=“分子” （分母置为1）
				if(denominatorRes<=0) {
					kiResult = numeratorRes/1;
				}else{
					kiResult = numeratorRes / denominatorRes;
				}
			}else if(outlierHandlingType == OutlierHandlingType.DEAL_THREE) {
				//特殊处理3	del_nega_rec：=treat
				//若分子>0 且分母<=0，则指标=“分子”；
				//若分子>0 且分母>0 ，不做特殊处理；
				//若分子<=0 且分母<0，则指标=-1*公式；
				//若分子<=0且分母>=0，则指标=“分子” ；
				if(numeratorRes > 0 && denominatorRes <= 0) {
					kiResult = numeratorRes;
				}else if(numeratorRes <= 0 && denominatorRes < 0) {
					kiResult = -1*(numeratorRes/denominatorRes);
				}else if(numeratorRes <= 0 && denominatorRes >= 0) {
					kiResult = numeratorRes/1;
				}else {
					kiResult = numeratorRes / denominatorRes;
				}
			}
		}
		return kiResult;
	}

	@Override
	public Map<String, Object> calculateKiLib(List<FinancialStatementsWrap> finStats, Map<String, Object> parameters)
			throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		log.info("开始计算基础指标层...");
		//计算基础指标
		Map<String, Object> kiParameters = financialBasicsLibService.calculateBaseLib(finStats,parameters);
		log.info("基础指标层计算完毕!");
		log.info("基础指标层计算结果:"+kiParameters);
		
		log.info("开始模型指标...");
		//获取需要计算的指标
		List<FinancialKiLib> kiLibs = repository.findAll();
		for(FinancialKiLib kiLib : kiLibs) {
			log.info("开始计算模型指标:"+kiLib.getKiCode());
			//指标值
			Double kiResult = null;
			//分子
			String numerator = kiLib.getKiNumerator();
			//分子结果
			Double numeratorRes = null;
			if(StringUtils.hasText(numerator)) {
				Object tempResult = null;
				try {
					tempResult = AviatorEvaluator.execute(numerator,kiParameters);
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
				if(tempResult!=null) {
					if(Double.isNaN(Double.valueOf(tempResult.toString())) || Double.isInfinite(Double.valueOf(tempResult.toString()))) {
						tempResult = null;
					}
				}
				numeratorRes = tempResult==null?null:Double.valueOf(tempResult.toString());
			}
			//分母
			String denominator = kiLib.getKiDenominator();
			//分母结果
			Double denominatorRes = null;
			if(StringUtils.hasText(denominator)){
				Object tempResult = null;
				try {
					tempResult = AviatorEvaluator.execute(denominator,kiParameters);
				} catch (Exception e) {
					log.error(e);
				}
				if(tempResult!=null) {
					if(Double.isNaN(Double.valueOf(tempResult.toString())) || Double.isInfinite(Double.valueOf(tempResult.toString()))) {
						tempResult = null;
					}
				}
				denominatorRes = tempResult==null?null:Double.valueOf(tempResult.toString());
			}
			kiResult = this.outlierHandlingprocessor(numeratorRes, denominatorRes, kiLib.getOutlierHandling());
			log.info("模型指标:"+kiLib.getKiCode()+"计算结果为:"+kiResult);
			resultMap.put(kiLib.getKiCode(), kiResult);
		}
		log.info("模型指标计算完毕！");
		log.info("模型指标计算结果:"+resultMap);
		return resultMap;
	}
}
