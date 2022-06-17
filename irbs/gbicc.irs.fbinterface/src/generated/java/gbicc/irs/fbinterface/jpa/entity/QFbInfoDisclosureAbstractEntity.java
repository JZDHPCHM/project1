package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbInfoDisclosureAbstractEntity is a Querydsl query type for FbInfoDisclosureAbstractEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbInfoDisclosureAbstractEntity extends EntityPathBase<FbInfoDisclosureAbstractEntity> {

    private static final long serialVersionUID = 555402751L;

    public static final QFbInfoDisclosureAbstractEntity fbInfoDisclosureAbstractEntity = new QFbInfoDisclosureAbstractEntity("fbInfoDisclosureAbstractEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath digest = createString("digest");

    public final StringPath id = createString("id");

    public final StringPath infoDisclosureId = createString("infoDisclosureId");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QFbInfoDisclosureAbstractEntity(String variable) {
        super(FbInfoDisclosureAbstractEntity.class, forVariable(variable));
    }

    public QFbInfoDisclosureAbstractEntity(Path<? extends FbInfoDisclosureAbstractEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbInfoDisclosureAbstractEntity(PathMetadata metadata) {
        super(FbInfoDisclosureAbstractEntity.class, metadata);
    }

}

