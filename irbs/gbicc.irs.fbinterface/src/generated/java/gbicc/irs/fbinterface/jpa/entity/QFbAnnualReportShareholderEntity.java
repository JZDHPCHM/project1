package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportShareholderEntity is a Querydsl query type for FbAnnualReportShareholderEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportShareholderEntity extends EntityPathBase<FbAnnualReportShareholderEntity> {

    private static final long serialVersionUID = -1369895684L;

    public static final QFbAnnualReportShareholderEntity fbAnnualReportShareholderEntity = new QFbAnnualReportShareholderEntity("fbAnnualReportShareholderEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

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

    public final NumberPath<java.math.BigDecimal> paidAmount = createNumber("paidAmount", java.math.BigDecimal.class);

    public final StringPath paidTime = createString("paidTime");

    public final StringPath paidType = createString("paidType");

    public final StringPath serialNumber = createString("serialNumber");

    public final StringPath shareholder = createString("shareholder");

    public final NumberPath<java.math.BigDecimal> subscribedAmount = createNumber("subscribedAmount", java.math.BigDecimal.class);

    public final StringPath subscribedTime = createString("subscribedTime");

    public final StringPath subscribedType = createString("subscribedType");

    public QFbAnnualReportShareholderEntity(String variable) {
        super(FbAnnualReportShareholderEntity.class, forVariable(variable));
    }

    public QFbAnnualReportShareholderEntity(Path<? extends FbAnnualReportShareholderEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportShareholderEntity(PathMetadata metadata) {
        super(FbAnnualReportShareholderEntity.class, metadata);
    }

}

