package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportAnalyseTrend is a Querydsl query type for ReportAnalyseTrend
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportAnalyseTrend extends EntityPathBase<ReportAnalyseTrend> {

    private static final long serialVersionUID = -1273937101L;

    public static final QReportAnalyseTrend reportAnalyseTrend = new QReportAnalyseTrend("reportAnalyseTrend");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<Double> compQuarterGrantNum = createNumber("compQuarterGrantNum", Double.class);

    public final StringPath compQuarterGrantPer = createString("compQuarterGrantPer");

    public final NumberPath<Double> compYearGrantNum = createNumber("compYearGrantNum", Double.class);

    public final StringPath compYearGrantPer = createString("compYearGrantPer");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> grantNum = createNumber("grantNum", Double.class);

    public final StringPath grantPer = createString("grantPer");

    public final StringPath guestGr = createString("guestGr");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath quarter = createString("quarter");

    public final NumberPath<Double> repTime = createNumber("repTime", Double.class);

    public final StringPath type = createString("type");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportAnalyseTrend(String variable) {
        super(ReportAnalyseTrend.class, forVariable(variable));
    }

    public QReportAnalyseTrend(Path<? extends ReportAnalyseTrend> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportAnalyseTrend(PathMetadata metadata) {
        super(ReportAnalyseTrend.class, metadata);
    }

}

