package gbicc.irs.risk.exposure.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRiskTypeCtmEntity is a Querydsl query type for RiskTypeCtmEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRiskTypeCtmEntity extends EntityPathBase<RiskTypeCtmEntity> {

    private static final long serialVersionUID = 262659799L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRiskTypeCtmEntity riskTypeCtmEntity = new QRiskTypeCtmEntity("riskTypeCtmEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath ctmName = createString("ctmName");

    public final StringPath ctmNo = createString("ctmNo");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final QRiskTypeEntity riskTypeEntity;

    public QRiskTypeCtmEntity(String variable) {
        this(RiskTypeCtmEntity.class, forVariable(variable), INITS);
    }

    public QRiskTypeCtmEntity(Path<? extends RiskTypeCtmEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRiskTypeCtmEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRiskTypeCtmEntity(PathMetadata metadata, PathInits inits) {
        this(RiskTypeCtmEntity.class, metadata, inits);
    }

    public QRiskTypeCtmEntity(Class<? extends RiskTypeCtmEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.riskTypeEntity = inits.isInitialized("riskTypeEntity") ? new QRiskTypeEntity(forProperty("riskTypeEntity")) : null;
    }

}

