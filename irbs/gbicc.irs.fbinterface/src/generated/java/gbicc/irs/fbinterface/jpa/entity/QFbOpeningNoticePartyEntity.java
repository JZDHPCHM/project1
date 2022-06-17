package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbOpeningNoticePartyEntity is a Querydsl query type for FbOpeningNoticePartyEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbOpeningNoticePartyEntity extends EntityPathBase<FbOpeningNoticePartyEntity> {

    private static final long serialVersionUID = -1694846280L;

    public static final QFbOpeningNoticePartyEntity fbOpeningNoticePartyEntity = new QFbOpeningNoticePartyEntity("fbOpeningNoticePartyEntity");

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

    public final StringPath role = createString("role");

    public final StringPath type = createString("type");

    public QFbOpeningNoticePartyEntity(String variable) {
        super(FbOpeningNoticePartyEntity.class, forVariable(variable));
    }

    public QFbOpeningNoticePartyEntity(Path<? extends FbOpeningNoticePartyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbOpeningNoticePartyEntity(PathMetadata metadata) {
        super(FbOpeningNoticePartyEntity.class, metadata);
    }

}

