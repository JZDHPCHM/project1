package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbBoundAnnouncementEntity is a Querydsl query type for FbBoundAnnouncementEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbBoundAnnouncementEntity extends EntityPathBase<FbBoundAnnouncementEntity> {

    private static final long serialVersionUID = 1159175561L;

    public static final QFbBoundAnnouncementEntity fbBoundAnnouncementEntity = new QFbBoundAnnouncementEntity("fbBoundAnnouncementEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath disclosure = createString("disclosure");

    public final StringPath id = createString("id");

    public final StringPath issuerName = createString("issuerName");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath publicDate = createString("publicDate");

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final StringPath url = createString("url");

    public QFbBoundAnnouncementEntity(String variable) {
        super(FbBoundAnnouncementEntity.class, forVariable(variable));
    }

    public QFbBoundAnnouncementEntity(Path<? extends FbBoundAnnouncementEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbBoundAnnouncementEntity(PathMetadata metadata) {
        super(FbBoundAnnouncementEntity.class, metadata);
    }

}

