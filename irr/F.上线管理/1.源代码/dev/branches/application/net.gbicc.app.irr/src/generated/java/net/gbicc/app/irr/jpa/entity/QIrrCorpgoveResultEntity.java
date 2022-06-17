package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrCorpgoveResultEntity is a Querydsl query type for IrrCorpgoveResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrCorpgoveResultEntity extends EntityPathBase<IrrCorpgoveResultEntity> {

    private static final long serialVersionUID = 387440885L;

    public static final QIrrCorpgoveResultEntity irrCorpgoveResultEntity = new QIrrCorpgoveResultEntity("irrCorpgoveResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath id = createString("id");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexId = createString("indexId");

    public final StringPath indexName = createString("indexName");

    public final StringPath indexScore = createString("indexScore");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath selfEval = createString("selfEval");

    public final StringPath taskBatch = createString("taskBatch");

    public QIrrCorpgoveResultEntity(String variable) {
        super(IrrCorpgoveResultEntity.class, forVariable(variable));
    }

    public QIrrCorpgoveResultEntity(Path<? extends IrrCorpgoveResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrCorpgoveResultEntity(PathMetadata metadata) {
        super(IrrCorpgoveResultEntity.class, metadata);
    }

}

