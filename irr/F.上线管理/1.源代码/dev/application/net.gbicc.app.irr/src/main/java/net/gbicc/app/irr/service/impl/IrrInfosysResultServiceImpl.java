package net.gbicc.app.irr.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.querydsl.core.BooleanBuilder;

import net.gbicc.app.irr.jpa.entity.IrrInfosysResultEntity;
import net.gbicc.app.irr.jpa.entity.QIrrInfosysResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrInfosysResultRepository;
import net.gbicc.app.irr.service.IrrInfosysResultService;

/**
* 信息系统结果
*/
@Service
public class IrrInfosysResultServiceImpl extends DaoServiceImpl<IrrInfosysResultEntity, String, IrrInfosysResultRepository> 
	implements IrrInfosysResultService {

    @Override
    public String findIndexValueByCondition(String taskBatch, String indexCode) throws Exception {
        if (StringUtils.isBlank(taskBatch) || StringUtils.isBlank(indexCode)) {
            return null;
        }
        QIrrInfosysResultEntity qInfoSys = QIrrInfosysResultEntity.irrInfosysResultEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qInfoSys.taskBatch.eq(taskBatch));
        builder.and(qInfoSys.indexCode.eq(indexCode));
        Optional<IrrInfosysResultEntity> optional = repository.findOne(builder);
        if(optional.isPresent()) {
            return optional.get().getIndexScore();
        }
        return null;
    }

}
