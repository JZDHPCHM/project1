package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportGuaranteeEntity is a Querydsl query type for FbAnnualReportGuaranteeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportGuaranteeEntity extends EntityPathBase<FbAnnualReportGuaranteeEntity> {

    private static final long serialVersionUID = 2065322745L;

    public static final QFbAnnualReportGuaranteeEntity fbAnnualReportGuaranteeEntity = new QFbAnnualReportGuaranteeEntity("fbAnnualReportGuaranteeEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath creditors = createString("creditors");

    public final StringPath debtor = createString("debtor");

    public final StringPath guaranteeDurPeriod = createString("guaranteeDurPeriod");

    public final StringPath guaranteeType = createString("guaranteeType");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath performanceLimit = createString("performanceLimit");

    public final StringPath principalClaimAmount = createString("principalClaimAmount");

    public final StringPath principalClaimType = createString("principalClaimType");

    public QFbAnnualReportGuaranteeEntity(String variable) {
        super(FbAnnualReportGuaranteeEntity.class, forVariable(variable));
    }

    public QFbAnnualReportGuaranteeEntity(Path<? extends FbAnnualReportGuaranteeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportGuaranteeEntity(PathMetadata metadata) {
        super(FbAnnualReportGuaranteeEntity.class, metadata);
    }

}

