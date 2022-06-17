package gbicc.irs.risk.exposure.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRiskEntity is a Querydsl query type for RiskEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRiskEntity extends EntityPathBase<RiskEntity> {

    private static final long serialVersionUID = -822105007L;

    public static final QRiskEntity riskEntity = new QRiskEntity("riskEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath contractNum = createString("contractNum");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath ctmName = createString("ctmName");

    public final StringPath ctmNum = createString("ctmNum");

    public final StringPath currentTaskTerson = createString("currentTaskTerson");

    public final StringPath finalDecision = createString("finalDecision");

    public final DateTimePath<java.util.Date> finshDate = createDateTime("finshDate", java.util.Date.class);

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath mainGuaranteeMethod = createString("mainGuaranteeMethod");

    public final StringPath manualDecisionResult = createString("manualDecisionResult");

    public final EnumPath<gbicc.irs.risk.exposure.support.DefaultRiskProcessStatus> processState = createEnum("processState", gbicc.irs.risk.exposure.support.DefaultRiskProcessStatus.class);

    public final StringPath productType = createString("productType");

    public final StringPath reason = createString("reason");

    public final StringPath regulatingMethods = createString("regulatingMethods");

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public final StringPath startUser = createString("startUser");

    public final DateTimePath<java.util.Date> SystemDecisionDate = createDateTime("SystemDecisionDate", java.util.Date.class);

    public final StringPath systemDecisionResult = createString("systemDecisionResult");

    public QRiskEntity(String variable) {
        super(RiskEntity.class, forVariable(variable));
    }

    public QRiskEntity(Path<? extends RiskEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRiskEntity(PathMetadata metadata) {
        super(RiskEntity.class, metadata);
    }

}

