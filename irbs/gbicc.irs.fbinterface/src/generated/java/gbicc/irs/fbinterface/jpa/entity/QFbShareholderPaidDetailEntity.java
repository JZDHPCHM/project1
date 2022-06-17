package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbShareholderPaidDetailEntity is a Querydsl query type for FbShareholderPaidDetailEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbShareholderPaidDetailEntity extends EntityPathBase<FbShareholderPaidDetailEntity> {

    private static final long serialVersionUID = -1509770292L;

    public static final QFbShareholderPaidDetailEntity fbShareholderPaidDetailEntity = new QFbShareholderPaidDetailEntity("fbShareholderPaidDetailEntity");

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

    public final StringPath paidAmount = createString("paidAmount");

    public final StringPath paidCurrency = createString("paidCurrency");

    public final StringPath paidDate = createString("paidDate");

    public final StringPath paidType = createString("paidType");

    public final StringPath shareholderInfoId = createString("shareholderInfoId");

    public QFbShareholderPaidDetailEntity(String variable) {
        super(FbShareholderPaidDetailEntity.class, forVariable(variable));
    }

    public QFbShareholderPaidDetailEntity(Path<? extends FbShareholderPaidDetailEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbShareholderPaidDetailEntity(PathMetadata metadata) {
        super(FbShareholderPaidDetailEntity.class, metadata);
    }

}

