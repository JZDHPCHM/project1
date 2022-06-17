package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbEventPunishDetailEntity is a Querydsl query type for FbEventPunishDetailEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbEventPunishDetailEntity extends EntityPathBase<FbEventPunishDetailEntity> {

    private static final long serialVersionUID = -444597084L;

    public static final QFbEventPunishDetailEntity fbEventPunishDetailEntity = new QFbEventPunishDetailEntity("fbEventPunishDetailEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath author = createString("author");

    public final StringPath category = createString("category");

    public final StringPath companyId = createString("companyId");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath eventCompanyId = createString("eventCompanyId");

    public final NumberPath<Long> eventTime = createNumber("eventTime", Long.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Long> postTime = createNumber("postTime", Long.class);

    public final StringPath sourceId = createString("sourceId");

    public final StringPath title = createString("title");

    public final StringPath url = createString("url");

    public QFbEventPunishDetailEntity(String variable) {
        super(FbEventPunishDetailEntity.class, forVariable(variable));
    }

    public QFbEventPunishDetailEntity(Path<? extends FbEventPunishDetailEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbEventPunishDetailEntity(PathMetadata metadata) {
        super(FbEventPunishDetailEntity.class, metadata);
    }

}

