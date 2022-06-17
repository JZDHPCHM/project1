package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbInfoDisclosureEntity is a Querydsl query type for FbInfoDisclosureEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbInfoDisclosureEntity extends EntityPathBase<FbInfoDisclosureEntity> {

    private static final long serialVersionUID = -1968306883L;

    public static final QFbInfoDisclosureEntity fbInfoDisclosureEntity = new QFbInfoDisclosureEntity("fbInfoDisclosureEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath announcementDate = createString("announcementDate");

    public final StringPath code = createString("code");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath eventDate = createString("eventDate");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath originalLink = createString("originalLink");

    public final StringPath publicDate = createString("publicDate");

    public final StringPath shortSecuritites = createString("shortSecuritites");

    public final StringPath title = createString("title");

    public final StringPath url = createString("url");

    public QFbInfoDisclosureEntity(String variable) {
        super(FbInfoDisclosureEntity.class, forVariable(variable));
    }

    public QFbInfoDisclosureEntity(Path<? extends FbInfoDisclosureEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbInfoDisclosureEntity(PathMetadata metadata) {
        super(FbInfoDisclosureEntity.class, metadata);
    }

}

