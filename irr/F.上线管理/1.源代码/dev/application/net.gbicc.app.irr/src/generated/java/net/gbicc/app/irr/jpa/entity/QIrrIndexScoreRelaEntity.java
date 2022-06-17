package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrIndexScoreRelaEntity is a Querydsl query type for IrrIndexScoreRelaEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrIndexScoreRelaEntity extends EntityPathBase<IrrIndexScoreRelaEntity> {

    private static final long serialVersionUID = 2074723743L;

    public static final QIrrIndexScoreRelaEntity irrIndexScoreRelaEntity = new QIrrIndexScoreRelaEntity("irrIndexScoreRelaEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexId = createString("indexId");

    public final StringPath indexName = createString("indexName");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath relaCode = createString("relaCode");

    public final StringPath relaId = createString("relaId");

    public final StringPath relaName = createString("relaName");

    public QIrrIndexScoreRelaEntity(String variable) {
        super(IrrIndexScoreRelaEntity.class, forVariable(variable));
    }

    public QIrrIndexScoreRelaEntity(Path<? extends IrrIndexScoreRelaEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrIndexScoreRelaEntity(PathMetadata metadata) {
        super(IrrIndexScoreRelaEntity.class, metadata);
    }

}

