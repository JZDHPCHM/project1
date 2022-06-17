package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportRatingDistribution is a Querydsl query type for ReportRatingDistribution
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportRatingDistribution extends EntityPathBase<ReportRatingDistribution> {

    private static final long serialVersionUID = 1684272202L;

    public static final QReportRatingDistribution reportRatingDistribution = new QReportRatingDistribution("reportRatingDistribution");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<Double> beginYearChange = createNumber("beginYearChange", Double.class);

    public final NumberPath<Double> beginYearMinus = createNumber("beginYearMinus", Double.class);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> customerNum = createNumber("customerNum", Double.class);

    public final NumberPath<Double> customerNumPercent = createNumber("customerNumPercent", Double.class);

    public final StringPath guestGr = createString("guestGr");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Double> lastMonthChange = createNumber("lastMonthChange", Double.class);

    public final NumberPath<Double> lastMonthMinus = createNumber("lastMonthMinus", Double.class);

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Double> repTime = createNumber("repTime", Double.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportRatingDistribution(String variable) {
        super(ReportRatingDistribution.class, forVariable(variable));
    }

    public QReportRatingDistribution(Path<? extends ReportRatingDistribution> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportRatingDistribution(PathMetadata metadata) {
        super(ReportRatingDistribution.class, metadata);
    }

}

