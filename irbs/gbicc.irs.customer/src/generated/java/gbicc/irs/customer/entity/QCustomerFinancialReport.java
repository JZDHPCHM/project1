package gbicc.irs.customer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomerFinancialReport is a Querydsl query type for CustomerFinancialReport
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomerFinancialReport extends EntityPathBase<CustomerFinancialReport> {

    private static final long serialVersionUID = 986872480L;

    public static final QCustomerFinancialReport customerFinancialReport = new QCustomerFinancialReport("customerFinancialReport");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath ctmNo = createString("ctmNo");

    public final StringPath financialFeportType = createString("financialFeportType");

    public final StringPath financialReport = createString("financialReport");

    public final NumberPath<Long> financialReportDt = createNumber("financialReportDt", Long.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QCustomerFinancialReport(String variable) {
        super(CustomerFinancialReport.class, forVariable(variable));
    }

    public QCustomerFinancialReport(Path<? extends CustomerFinancialReport> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomerFinancialReport(PathMetadata metadata) {
        super(CustomerFinancialReport.class, metadata);
    }

}

