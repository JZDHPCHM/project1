package gbicc.irs.risk.exposure.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRiskTypeEntity is a Querydsl query type for RiskTypeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRiskTypeEntity extends EntityPathBase<RiskTypeEntity> {

    private static final long serialVersionUID = -1622708949L;

    public static final QRiskTypeEntity riskTypeEntity = new QRiskTypeEntity("riskTypeEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath detailType = createString("detailType");

    public final StringPath isStart = createString("isStart");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath type = createString("type");

    public QRiskTypeEntity(String variable) {
        super(RiskTypeEntity.class, forVariable(variable));
    }

    public QRiskTypeEntity(Path<? extends RiskTypeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRiskTypeEntity(PathMetadata metadata) {
        super(RiskTypeEntity.class, metadata);
    }

}

