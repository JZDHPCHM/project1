package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrIndexResultEntity is a Querydsl query type for IrrIndexResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrIndexResultEntity extends EntityPathBase<IrrIndexResultEntity> {

    private static final long serialVersionUID = 733778206L;

    public static final QIrrIndexResultEntity irrIndexResultEntity = new QIrrIndexResultEntity("irrIndexResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath dataDesc = createString("dataDesc");

    public final NumberPath<java.math.BigDecimal> dataVali = createNumber("dataVali", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> deduValue = createNumber("deduValue", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> fracDiff = createNumber("fracDiff", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> holdOperRisk = createNumber("holdOperRisk", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> holdQuanDiff = createNumber("holdQuanDiff", java.math.BigDecimal.class);

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

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskId = createString("taskId");

    public final NumberPath<java.math.BigDecimal> weigValue = createNumber("weigValue", java.math.BigDecimal.class);

    public QIrrIndexResultEntity(String variable) {
        super(IrrIndexResultEntity.class, forVariable(variable));
    }

    public QIrrIndexResultEntity(Path<? extends IrrIndexResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrIndexResultEntity(PathMetadata metadata) {
        super(IrrIndexResultEntity.class, metadata);
    }

}

