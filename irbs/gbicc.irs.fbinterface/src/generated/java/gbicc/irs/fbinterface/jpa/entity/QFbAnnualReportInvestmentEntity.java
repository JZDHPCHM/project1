package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportInvestmentEntity is a Querydsl query type for FbAnnualReportInvestmentEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportInvestmentEntity extends EntityPathBase<FbAnnualReportInvestmentEntity> {

    private static final long serialVersionUID = 295623656L;

    public static final QFbAnnualReportInvestmentEntity fbAnnualReportInvestmentEntity = new QFbAnnualReportInvestmentEntity("fbAnnualReportInvestmentEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath enterpriseCode = createString("enterpriseCode");

    public final StringPath enterpriseName = createString("enterpriseName");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QFbAnnualReportInvestmentEntity(String variable) {
        super(FbAnnualReportInvestmentEntity.class, forVariable(variable));
    }

    public QFbAnnualReportInvestmentEntity(Path<? extends FbAnnualReportInvestmentEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportInvestmentEntity(PathMetadata metadata) {
        super(FbAnnualReportInvestmentEntity.class, metadata);
    }

}

