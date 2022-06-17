package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbInfoDisclosureSourceEntity is a Querydsl query type for FbInfoDisclosureSourceEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbInfoDisclosureSourceEntity extends EntityPathBase<FbInfoDisclosureSourceEntity> {

    private static final long serialVersionUID = 160542136L;

    public static final QFbInfoDisclosureSourceEntity fbInfoDisclosureSourceEntity = new QFbInfoDisclosureSourceEntity("fbInfoDisclosureSourceEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath infoDisclosureId = createString("infoDisclosureId");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath source = createString("source");

    public QFbInfoDisclosureSourceEntity(String variable) {
        super(FbInfoDisclosureSourceEntity.class, forVariable(variable));
    }

    public QFbInfoDisclosureSourceEntity(Path<? extends FbInfoDisclosureSourceEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbInfoDisclosureSourceEntity(PathMetadata metadata) {
        super(FbInfoDisclosureSourceEntity.class, metadata);
    }

}

