package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbOpeningNoticeOtherEntity is a Querydsl query type for FbOpeningNoticeOtherEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbOpeningNoticeOtherEntity extends EntityPathBase<FbOpeningNoticeOtherEntity> {

    private static final long serialVersionUID = -731918494L;

    public static final QFbOpeningNoticeOtherEntity fbOpeningNoticeOtherEntity = new QFbOpeningNoticeOtherEntity("fbOpeningNoticeOtherEntity");

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

    public final StringPath name = createString("name");

    public final StringPath openingNoticeId = createString("openingNoticeId");

    public final StringPath openingNoticePartyId = createString("openingNoticePartyId");

    public QFbOpeningNoticeOtherEntity(String variable) {
        super(FbOpeningNoticeOtherEntity.class, forVariable(variable));
    }

    public QFbOpeningNoticeOtherEntity(Path<? extends FbOpeningNoticeOtherEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbOpeningNoticeOtherEntity(PathMetadata metadata) {
        super(FbOpeningNoticeOtherEntity.class, metadata);
    }

}

