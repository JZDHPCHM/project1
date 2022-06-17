package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrLiquriskResultEntity is a Querydsl query type for IrrLiquriskResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrLiquriskResultEntity extends EntityPathBase<IrrLiquriskResultEntity> {

    private static final long serialVersionUID = -1071486748L;

    public static final QIrrLiquriskResultEntity irrLiquriskResultEntity = new QIrrLiquriskResultEntity("irrLiquriskResultEntity");

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

    public final StringPath indexResult = createString("indexResult");

    public final StringPath indexScore = createString("indexScore");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath taskBatch = createString("taskBatch");

    public QIrrLiquriskResultEntity(String variable) {
        super(IrrLiquriskResultEntity.class, forVariable(variable));
    }

    public QIrrLiquriskResultEntity(Path<? extends IrrLiquriskResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrLiquriskResultEntity(PathMetadata metadata) {
        super(IrrLiquriskResultEntity.class, metadata);
    }

}

