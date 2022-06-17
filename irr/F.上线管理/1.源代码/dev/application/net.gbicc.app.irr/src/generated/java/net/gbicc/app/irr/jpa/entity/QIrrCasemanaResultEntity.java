package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrCasemanaResultEntity is a Querydsl query type for IrrCasemanaResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrCasemanaResultEntity extends EntityPathBase<IrrCasemanaResultEntity> {

    private static final long serialVersionUID = -1149444821L;

    public static final QIrrCasemanaResultEntity irrCasemanaResultEntity = new QIrrCasemanaResultEntity("irrCasemanaResultEntity");

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

    public QIrrCasemanaResultEntity(String variable) {
        super(IrrCasemanaResultEntity.class, forVariable(variable));
    }

    public QIrrCasemanaResultEntity(Path<? extends IrrCasemanaResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrCasemanaResultEntity(PathMetadata metadata) {
        super(IrrCasemanaResultEntity.class, metadata);
    }

}

