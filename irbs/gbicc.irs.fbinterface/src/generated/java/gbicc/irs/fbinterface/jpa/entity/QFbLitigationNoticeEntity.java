package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbLitigationNoticeEntity is a Querydsl query type for FbLitigationNoticeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbLitigationNoticeEntity extends EntityPathBase<FbLitigationNoticeEntity> {

    private static final long serialVersionUID = 1931108250L;

    public static final QFbLitigationNoticeEntity fbLitigationNoticeEntity = new QFbLitigationNoticeEntity("fbLitigationNoticeEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath noticeDate = createString("noticeDate");

    public final StringPath noticePeople = createString("noticePeople");

    public final StringPath noticeType = createString("noticeType");

    public QFbLitigationNoticeEntity(String variable) {
        super(FbLitigationNoticeEntity.class, forVariable(variable));
    }

    public QFbLitigationNoticeEntity(Path<? extends FbLitigationNoticeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbLitigationNoticeEntity(PathMetadata metadata) {
        super(FbLitigationNoticeEntity.class, metadata);
    }

}

