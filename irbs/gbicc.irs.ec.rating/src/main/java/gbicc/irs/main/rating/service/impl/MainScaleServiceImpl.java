package gbicc.irs.main.rating.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.main.rating.entity.MainScale;
import gbicc.irs.main.rating.repository.MainScaleRepository;
import gbicc.irs.main.rating.service.MainScaleService;


@Service
public class MainScaleServiceImpl extends DaoServiceImpl<MainScale, String, MainScaleRepository> implements
MainScaleService {

	@Override
	public MainScale findByScaleLevelAndType(String scaleLevel, String type) throws Exception{
		if(StringUtils.isEmpty(scaleLevel) || StringUtils.isEmpty(type)) {
			return null;
		}
		MainScale ms = repository.findByScaleLevelAndType(scaleLevel, type);
		return ms;
	}

	@Override
	public MainScale getCurrentLevel(String type, Integer num) throws Exception{
		if(StringUtils.isEmpty(type) || num == null) {
			return null;
		}
		List<MainScale> msList = repository.findAll();
		if(!CollectionUtils.isEmpty(msList)) {
			msList = msList.stream().filter(m -> type.equals(m.getType())).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(msList)) {
				if(msList.size() < num) {
					num = msList.size();
				}
				if(num == 0) {
					num = 1;
				}
				for(MainScale mainScale : msList) {
					if(mainScale.getSort() == num) {
						return mainScale;
					}
				}
			}
		}
		return null;
	}

	@Override
	public List<MainScale> findByType(String type) throws Exception {
		return repository.findByType(type);
	}


}
