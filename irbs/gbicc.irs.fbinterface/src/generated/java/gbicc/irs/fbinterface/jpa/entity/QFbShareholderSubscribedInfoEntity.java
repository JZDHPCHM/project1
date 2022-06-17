package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbShareholderSubscribedInfoEntity is a Querydsl query type for FbShareholderSubscribedInfoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbShareholderSubscribedInfoEntity extends EntityPathBase<FbShareholderSubscribedInfoEntity> {

    private static final long serialVersionUID = -1307335817L;

    public static final QFbShareholderSubscribedInfoEntity fbShareholderSubscribedInfoEntity = new QFbShareholderSubscribedInfoEntity("fbShareholderSubscribedInfoEntity");

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

    public final StringPath shareholderInfoId = createString("shareholderInfoId");

    public final StringPath subscribedAmount = createString("subscribedAmount");

    public final StringPath subscribedCurrency = createString("subscribedCurrency");

    public final StringPath subscribedDate = createString("subscribedDate");

    public final StringPath subscribedType = createString("subscribedType");

    public QFbShareholderSubscribedInfoEntity(String variable) {
        super(FbShareholderSubscribedInfoEntity.class, forVariable(variable));
    }

    public QFbShareholderSubscribedInfoEntity(Path<? extends FbShareholderSubscribedInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbShareholderSubscribedInfoEntity(PathMetadata metadata) {
        super(FbShareholderSubscribedInfoEntity.class, metadata);
    }

}

