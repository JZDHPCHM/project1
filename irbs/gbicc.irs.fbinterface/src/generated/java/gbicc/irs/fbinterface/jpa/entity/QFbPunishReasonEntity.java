package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbPunishReasonEntity is a Querydsl query type for FbPunishReasonEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbPunishReasonEntity extends EntityPathBase<FbPunishReasonEntity> {

    private static final long serialVersionUID = -1280008005L;

    public static final QFbPunishReasonEntity fbPunishReasonEntity = new QFbPunishReasonEntity("fbPunishReasonEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath punishId = createString("punishId");

    public final StringPath reason = createString("reason");

    public QFbPunishReasonEntity(String variable) {
        super(FbPunishReasonEntity.class, forVariable(variable));
    }

    public QFbPunishReasonEntity(Path<? extends FbPunishReasonEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbPunishReasonEntity(PathMetadata metadata) {
        super(FbPunishReasonEntity.class, metadata);
    }

}

