package gbicc.irs.main.rating.service;

import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.main.rating.entity.MainScale;
import gbicc.irs.main.rating.repository.MainScaleRepository;

public interface MainScaleService extends DaoService<MainScale, String, MainScaleRepository> {

	/**
	 * 根据对应等级和类型查询当前主标尺
	 * @param scaleLevel
	 * @param type
	 * @return
	 */
	MainScale findByScaleLevelAndType(String scaleLevel,String type) throws Exception;
	
	/**
	 * 获取当前对应级别标尺
	 * @param type
	 * @param num 调整后的位置
	 * @return
	 */
	MainScale getCurrentLevel(String type,Integer num) throws Exception;
	
	List<MainScale> findByType(String type) throws Exception;
}
