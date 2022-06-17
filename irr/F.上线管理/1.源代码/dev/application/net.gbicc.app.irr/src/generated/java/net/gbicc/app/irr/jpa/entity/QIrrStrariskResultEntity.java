package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrStrariskResultEntity is a Querydsl query type for IrrStrariskResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrStrariskResultEntity extends EntityPathBase<IrrStrariskResultEntity> {

    private static final long serialVersionUID = 1663959763L;

    public static final QIrrStrariskResultEntity irrStrariskResultEntity = new QIrrStrariskResultEntity("irrStrariskResultEntity");

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

    public final StringPath taskBatch = createString("taskBatch");

    public QIrrStrariskResultEntity(String variable) {
        super(IrrStrariskResultEntity.class, forVariable(variable));
    }

    public QIrrStrariskResultEntity(Path<? extends IrrStrariskResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrStrariskResultEntity(PathMetadata metadata) {
        super(IrrStrariskResultEntity.class, metadata);
    }

}

