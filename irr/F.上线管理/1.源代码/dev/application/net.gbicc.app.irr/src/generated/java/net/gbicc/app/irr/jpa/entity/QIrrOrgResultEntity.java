package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrOrgResultEntity is a Querydsl query type for IrrOrgResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrOrgResultEntity extends EntityPathBase<IrrOrgResultEntity> {

    private static final long serialVersionUID = -203534544L;

    public static final QIrrOrgResultEntity irrOrgResultEntity = new QIrrOrgResultEntity("irrOrgResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath dataDesc = createString("dataDesc");

    public final NumberPath<java.math.BigDecimal> dataVali = createNumber("dataVali", java.math.BigDecimal.class);

    public final StringPath id = createString("id");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexId = createString("indexId");

    public final StringPath indexName = createString("indexName");

    public final StringPath indexResultQ1 = createString("indexResultQ1");

    public final StringPath indexResultQ2 = createString("indexResultQ2");

    public final StringPath indexScoreQ1 = createString("indexScoreQ1");

    public final StringPath indexScoreQ2 = createString("indexScoreQ2");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath orgCode = createString("orgCode");

    public final StringPath orgId = createString("orgId");

    public final StringPath orgName = createString("orgName");

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskId = createString("taskId");

    public QIrrOrgResultEntity(String variable) {
        super(IrrOrgResultEntity.class, forVariable(variable));
    }

    public QIrrOrgResultEntity(Path<? extends IrrOrgResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrOrgResultEntity(PathMetadata metadata) {
        super(IrrOrgResultEntity.class, metadata);
    }

}

