package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbLitigationNoticeRelatedEntity is a Querydsl query type for FbLitigationNoticeRelatedEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbLitigationNoticeRelatedEntity extends EntityPathBase<FbLitigationNoticeRelatedEntity> {

    private static final long serialVersionUID = 1595119703L;

    public static final QFbLitigationNoticeRelatedEntity fbLitigationNoticeRelatedEntity = new QFbLitigationNoticeRelatedEntity("fbLitigationNoticeRelatedEntity");

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

    public final StringPath litigationNoticeId = createString("litigationNoticeId");

    public final StringPath name = createString("name");

    public QFbLitigationNoticeRelatedEntity(String variable) {
        super(FbLitigationNoticeRelatedEntity.class, forVariable(variable));
    }

    public QFbLitigationNoticeRelatedEntity(Path<? extends FbLitigationNoticeRelatedEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbLitigationNoticeRelatedEntity(PathMetadata metadata) {
        super(FbLitigationNoticeRelatedEntity.class, metadata);
    }

}

