package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrInfosysResultEntity is a Querydsl query type for IrrInfosysResultEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrInfosysResultEntity extends EntityPathBase<IrrInfosysResultEntity> {

    private static final long serialVersionUID = 157871403L;

    public static final QIrrInfosysResultEntity irrInfosysResultEntity = new QIrrInfosysResultEntity("irrInfosysResultEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath dataBody = createString("dataBody");

    public final StringPath id = createString("id");

    public final StringPath indexCentScore = createString("indexCentScore");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexId = createString("indexId");

    public final StringPath indexName = createString("indexName");

    public final StringPath indexScore = createString("indexScore");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath taskBatch = createString("taskBatch");

    public QIrrInfosysResultEntity(String variable) {
        super(IrrInfosysResultEntity.class, forVariable(variable));
    }

    public QIrrInfosysResultEntity(Path<? extends IrrInfosysResultEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrInfosysResultEntity(PathMetadata metadata) {
        super(IrrInfosysResultEntity.class, metadata);
    }

}

