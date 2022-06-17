package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrProjTypeEntity is a Querydsl query type for IrrProjTypeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrProjTypeEntity extends EntityPathBase<IrrProjTypeEntity> {

    private static final long serialVersionUID = 1682333102L;

    public static final QIrrProjTypeEntity irrProjTypeEntity = new QIrrProjTypeEntity("irrProjTypeEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<java.math.BigDecimal> aStanScore = createNumber("aStanScore", java.math.BigDecimal.class);

    public final StringPath bureauRate = createString("bureauRate");

    public final StringPath circRate = createString("circRate");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath id = createString("id");

    public final StringPath isLeaf = createString("isLeaf");

    public final StringPath isUse = createString("isUse");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath pCode = createString("pCode");

    public final StringPath pdqRiskRate = createString("pdqRiskRate");

    public final StringPath pId = createString("pId");

    public final NumberPath<java.math.BigDecimal> stanScore = createNumber("stanScore", java.math.BigDecimal.class);

    public final StringPath theRiskRate = createString("theRiskRate");

    public final StringPath totalRiskRate = createString("totalRiskRate");

    public final StringPath typeCode = createString("typeCode");

    public final StringPath typeName = createString("typeName");

    public final StringPath weigScorStand = createString("weigScorStand");

    public QIrrProjTypeEntity(String variable) {
        super(IrrProjTypeEntity.class, forVariable(variable));
    }

    public QIrrProjTypeEntity(Path<? extends IrrProjTypeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrProjTypeEntity(PathMetadata metadata) {
        super(IrrProjTypeEntity.class, metadata);
    }

}

