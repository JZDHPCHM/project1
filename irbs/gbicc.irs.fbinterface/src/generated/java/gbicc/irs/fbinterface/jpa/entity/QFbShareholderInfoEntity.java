package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbShareholderInfoEntity is a Querydsl query type for FbShareholderInfoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbShareholderInfoEntity extends EntityPathBase<FbShareholderInfoEntity> {

    private static final long serialVersionUID = 1578570365L;

    public static final QFbShareholderInfoEntity fbShareholderInfoEntity = new QFbShareholderInfoEntity("fbShareholderInfoEntity");

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

    public final StringPath liabilityForm = createString("liabilityForm");

    public final NumberPath<java.math.BigDecimal> paidAmount = createNumber("paidAmount", java.math.BigDecimal.class);

    public final StringPath shareholder = createString("shareholder");

    public final StringPath shareholderType = createString("shareholderType");

    public final NumberPath<java.math.BigDecimal> shareholdRatio = createNumber("shareholdRatio", java.math.BigDecimal.class);

    public final StringPath subscribedAmount = createString("subscribedAmount");

    public QFbShareholderInfoEntity(String variable) {
        super(FbShareholderInfoEntity.class, forVariable(variable));
    }

    public QFbShareholderInfoEntity(Path<? extends FbShareholderInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbShareholderInfoEntity(PathMetadata metadata) {
        super(FbShareholderInfoEntity.class, metadata);
    }

}

