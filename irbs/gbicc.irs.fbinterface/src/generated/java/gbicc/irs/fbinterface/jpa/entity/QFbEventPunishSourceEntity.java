package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbEventPunishSourceEntity is a Querydsl query type for FbEventPunishSourceEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbEventPunishSourceEntity extends EntityPathBase<FbEventPunishSourceEntity> {

    private static final long serialVersionUID = 354012398L;

    public static final QFbEventPunishSourceEntity fbEventPunishSourceEntity = new QFbEventPunishSourceEntity("fbEventPunishSourceEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath author = createString("author");

    public final StringPath companyId = createString("companyId");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath eventCompanyId = createString("eventCompanyId");

    public final StringPath eventDetailId = createString("eventDetailId");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Long> postTime = createNumber("postTime", Long.class);

    public final StringPath sourceId = createString("sourceId");

    public final StringPath title = createString("title");

    public final StringPath url = createString("url");

    public QFbEventPunishSourceEntity(String variable) {
        super(FbEventPunishSourceEntity.class, forVariable(variable));
    }

    public QFbEventPunishSourceEntity(Path<? extends FbEventPunishSourceEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbEventPunishSourceEntity(PathMetadata metadata) {
        super(FbEventPunishSourceEntity.class, metadata);
    }

}

