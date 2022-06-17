package gbicc.irs.risk.exposure.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRiskApplyForLogEntity is a Querydsl query type for RiskApplyForLogEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRiskApplyForLogEntity extends EntityPathBase<RiskApplyForLogEntity> {

    private static final long serialVersionUID = 3882718L;

    public static final QRiskApplyForLogEntity riskApplyForLogEntity = new QRiskApplyForLogEntity("riskApplyForLogEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final DateTimePath<java.util.Date> maintainDt = createDateTime("maintainDt", java.util.Date.class);

    public final StringPath maintainResult = createString("maintainResult");

    public final StringPath operate = createString("operate");

    public final StringPath processor = createString("processor");

    public final StringPath reason = createString("reason");

    public final StringPath riskExposureId = createString("riskExposureId");

    public final StringPath roleName = createString("roleName");

    public QRiskApplyForLogEntity(String variable) {
        super(RiskApplyForLogEntity.class, forVariable(variable));
    }

    public QRiskApplyForLogEntity(Path<? extends RiskApplyForLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRiskApplyForLogEntity(PathMetadata metadata) {
        super(RiskApplyForLogEntity.class, metadata);
    }

}

