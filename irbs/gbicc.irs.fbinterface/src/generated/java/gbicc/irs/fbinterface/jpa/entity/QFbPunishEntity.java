package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbPunishEntity is a Querydsl query type for FbPunishEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbPunishEntity extends EntityPathBase<FbPunishEntity> {

    private static final long serialVersionUID = -50131881L;

    public static final QFbPunishEntity fbPunishEntity = new QFbPunishEntity("fbPunishEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath caseName = createString("caseName");

    public final StringPath companyId = createString("companyId");

    public final StringPath content = createString("content");

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

    public final StringPath publicDate = createString("publicDate");

    public final StringPath punishDecisionDate = createString("punishDecisionDate");

    public final StringPath punishNo = createString("punishNo");

    public final StringPath source = createString("source");

    public final StringPath title = createString("title");

    public QFbPunishEntity(String variable) {
        super(FbPunishEntity.class, forVariable(variable));
    }

    public QFbPunishEntity(Path<? extends FbPunishEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbPunishEntity(PathMetadata metadata) {
        super(FbPunishEntity.class, metadata);
    }

}

