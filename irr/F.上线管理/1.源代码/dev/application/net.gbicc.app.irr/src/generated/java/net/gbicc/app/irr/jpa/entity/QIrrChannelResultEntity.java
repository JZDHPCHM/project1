package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrChannelResultEntity is a Querydsl query type for IrrChannelResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrChannelResultEntity extends EntityPathBase<IrrChannelResultEntity> {

    private static final long serialVersionUID = 988422351L;

    public static final QIrrChannelResultEntity irrChannelResultEntity = new QIrrChannelResultEntity("irrChannelResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath channelFlag = createString("channelFlag");

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

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath orgCode = createString("orgCode");

    public final StringPath orgId = createString("orgId");

    public final StringPath orgName = createString("orgName");

    public final StringPath taskBatch = createString("taskBatch");

    public final StringPath taskId = createString("taskId");

    public QIrrChannelResultEntity(String variable) {
        super(IrrChannelResultEntity.class, forVariable(variable));
    }

    public QIrrChannelResultEntity(Path<? extends IrrChannelResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrChannelResultEntity(PathMetadata metadata) {
        super(IrrChannelResultEntity.class, metadata);
    }

}

