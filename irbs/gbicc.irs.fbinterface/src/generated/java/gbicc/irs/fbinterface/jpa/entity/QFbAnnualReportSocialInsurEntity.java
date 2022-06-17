package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportSocialInsurEntity is a Querydsl query type for FbAnnualReportSocialInsurEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportSocialInsurEntity extends EntityPathBase<FbAnnualReportSocialInsurEntity> {

    private static final long serialVersionUID = -38731761L;

    public static final QFbAnnualReportSocialInsurEntity fbAnnualReportSocialInsurEntity = new QFbAnnualReportSocialInsurEntity("fbAnnualReportSocialInsurEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

    public final StringPath birthExpendsBase = createString("birthExpendsBase");

    public final StringPath birthInsurance = createString("birthInsurance");

    public final StringPath birthPayedBase = createString("birthPayedBase");

    public final StringPath birthUnpaidAmount = createString("birthUnpaidAmount");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath endowmentExpendsBase = createString("endowmentExpendsBase");

    public final StringPath endowmentInsurance = createString("endowmentInsurance");

    public final StringPath endowmentPayedBase = createString("endowmentPayedBase");

    public final StringPath endowmentUnpaidAmount = createString("endowmentUnpaidAmount");

    public final StringPath id = createString("id");

    public final StringPath injuryExpendsBase = createString("injuryExpendsBase");

    public final StringPath injuryInsurance = createString("injuryInsurance");

    public final StringPath injuryPayedBase = createString("injuryPayedBase");

    public final StringPath injuryUnpaidAmount = createString("injuryUnpaidAmount");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath medicalExpendsBase = createString("medicalExpendsBase");

    public final StringPath medicalInsurance = createString("medicalInsurance");

    public final StringPath medicalPayedBase = createString("medicalPayedBase");

    public final StringPath medicalUnpaidAmount = createString("medicalUnpaidAmount");

    public final StringPath unemployExpendsBase = createString("unemployExpendsBase");

    public final StringPath unemployInsurance = createString("unemployInsurance");

    public final StringPath unemployPayedBase = createString("unemployPayedBase");

    public final StringPath unemployUnpaidAmount = createString("unemployUnpaidAmount");

    public QFbAnnualReportSocialInsurEntity(String variable) {
        super(FbAnnualReportSocialInsurEntity.class, forVariable(variable));
    }

    public QFbAnnualReportSocialInsurEntity(Path<? extends FbAnnualReportSocialInsurEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportSocialInsurEntity(PathMetadata metadata) {
        super(FbAnnualReportSocialInsurEntity.class, metadata);
    }

}

