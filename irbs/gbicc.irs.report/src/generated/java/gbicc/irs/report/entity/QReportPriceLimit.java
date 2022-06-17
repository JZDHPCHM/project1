package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportPriceLimit is a Querydsl query type for ReportPriceLimit
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportPriceLimit extends EntityPathBase<ReportPriceLimit> {

    private static final long serialVersionUID = 620153979L;

    public static final QReportPriceLimit reportPriceLimit = new QReportPriceLimit("reportPriceLimit");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath fdGuestGr = createString("fdGuestGr");

    public final NumberPath<Double> fdLimit10 = createNumber("fdLimit10", Double.class);

    public final NumberPath<Double> fdLimit30 = createNumber("fdLimit30", Double.class);

    public final NumberPath<Double> fdLimit50 = createNumber("fdLimit50", Double.class);

    public final NumberPath<Double> fdLimitGreat50 = createNumber("fdLimitGreat50", Double.class);

    public final NumberPath<Double> fdLoanSum = createNumber("fdLoanSum", Double.class);

    public final NumberPath<Double> fdOverLimitSum = createNumber("fdOverLimitSum", Double.class);

    public final NumberPath<Double> fdOverLoanSum = createNumber("fdOverLoanSum", Double.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> quarter = createNumber("quarter", Integer.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportPriceLimit(String variable) {
        super(ReportPriceLimit.class, forVariable(variable));
    }

    public QReportPriceLimit(Path<? extends ReportPriceLimit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportPriceLimit(PathMetadata metadata) {
        super(ReportPriceLimit.class, metadata);
    }

}

