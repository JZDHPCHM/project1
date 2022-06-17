package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportBaseInfoEntity is a Querydsl query type for FbAnnualReportBaseInfoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportBaseInfoEntity extends EntityPathBase<FbAnnualReportBaseInfoEntity> {

    private static final long serialVersionUID = -1646986316L;

    public static final QFbAnnualReportBaseInfoEntity fbAnnualReportBaseInfoEntity = new QFbAnnualReportBaseInfoEntity("fbAnnualReportBaseInfoEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath employeeFemialNumber = createString("employeeFemialNumber");

    public final StringPath employeeNumber = createString("employeeNumber");

    public final StringPath enterpriseAddress = createString("enterpriseAddress");

    public final StringPath enterpriseEmail = createString("enterpriseEmail");

    public final StringPath enterpriseHolding = createString("enterpriseHolding");

    public final StringPath enterpriseName = createString("enterpriseName");

    public final StringPath enterpriseOperateStatus = createString("enterpriseOperateStatus");

    public final StringPath enterpriseTelephone = createString("enterpriseTelephone");

    public final StringPath equityTransfer = createString("equityTransfer");

    public final StringPath externalGuarantee = createString("externalGuarantee");

    public final StringPath id = createString("id");

    public final StringPath investment = createString("investment");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath mainActivities = createString("mainActivities");

    public final StringPath website = createString("website");

    public final StringPath zipCode = createString("zipCode");

    public QFbAnnualReportBaseInfoEntity(String variable) {
        super(FbAnnualReportBaseInfoEntity.class, forVariable(variable));
    }

    public QFbAnnualReportBaseInfoEntity(Path<? extends FbAnnualReportBaseInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportBaseInfoEntity(PathMetadata metadata) {
        super(FbAnnualReportBaseInfoEntity.class, metadata);
    }

}

