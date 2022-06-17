package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportAnalyseDistribution is a Querydsl query type for ReportAnalyseDistribution
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportAnalyseDistribution extends EntityPathBase<ReportAnalyseDistribution> {

    private static final long serialVersionUID = -1803274194L;

    public static final QReportAnalyseDistribution reportAnalyseDistribution = new QReportAnalyseDistribution("reportAnalyseDistribution");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> customerNum = createNumber("customerNum", Double.class);

    public final StringPath customerNumPercent = createString("customerNumPercent");

    public final StringPath guestGr = createString("guestGr");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Double> repTime = createNumber("repTime", Double.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportAnalyseDistribution(String variable) {
        super(ReportAnalyseDistribution.class, forVariable(variable));
    }

    public QReportAnalyseDistribution(Path<? extends ReportAnalyseDistribution> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportAnalyseDistribution(PathMetadata metadata) {
        super(ReportAnalyseDistribution.class, metadata);
    }

}

