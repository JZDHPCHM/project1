package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbOpeningNoticeEntity is a Querydsl query type for FbOpeningNoticeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbOpeningNoticeEntity extends EntityPathBase<FbOpeningNoticeEntity> {

    private static final long serialVersionUID = -676650444L;

    public static final QFbOpeningNoticeEntity fbOpeningNoticeEntity = new QFbOpeningNoticeEntity("fbOpeningNoticeEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath caseNo = createString("caseNo");

    public final StringPath caseReason = createString("caseReason");

    public final StringPath caseType = createString("caseType");

    public final StringPath companyId = createString("companyId");

    public final StringPath content = createString("content");

    public final StringPath court = createString("court");

    public final StringPath courtRoom = createString("courtRoom");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath judge = createString("judge");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath openDate = createString("openDate");

    public final StringPath publicDate = createString("publicDate");

    public final StringPath title = createString("title");

    public QFbOpeningNoticeEntity(String variable) {
        super(FbOpeningNoticeEntity.class, forVariable(variable));
    }

    public QFbOpeningNoticeEntity(Path<? extends FbOpeningNoticeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbOpeningNoticeEntity(PathMetadata metadata) {
        super(FbOpeningNoticeEntity.class, metadata);
    }

}

