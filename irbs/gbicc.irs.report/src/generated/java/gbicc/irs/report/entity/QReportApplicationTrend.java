package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportApplicationTrend is a Querydsl query type for ReportApplicationTrend
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportApplicationTrend extends EntityPathBase<ReportApplicationTrend> {

    private static final long serialVersionUID = 1034309622L;

    public static final QReportApplicationTrend reportApplicationTrend = new QReportApplicationTrend("reportApplicationTrend");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Double> localRufuseCompQ = createNumber("localRufuseCompQ", Double.class);

    public final NumberPath<Double> localRufuseCompY = createNumber("localRufuseCompY", Double.class);

    public final StringPath localRufusePer = createString("localRufusePer");

    public final NumberPath<Double> localRufuseTimes = createNumber("localRufuseTimes", Double.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> quarter = createNumber("quarter", Integer.class);

    public final StringPath rangeOrder = createString("rangeOrder");

    public final StringPath repTime = createString("repTime");

    public final NumberPath<Double> sumRufusePer = createNumber("sumRufusePer", Double.class);

    public final NumberPath<Double> upperRufuseCompQ = createNumber("upperRufuseCompQ", Double.class);

    public final NumberPath<Double> upperRufuseCompY = createNumber("upperRufuseCompY", Double.class);

    public final NumberPath<Double> upperRufusePer = createNumber("upperRufusePer", Double.class);

    public final NumberPath<Double> upperRufuseTimes = createNumber("upperRufuseTimes", Double.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportApplicationTrend(String variable) {
        super(ReportApplicationTrend.class, forVariable(variable));
    }

    public QReportApplicationTrend(Path<? extends ReportApplicationTrend> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportApplicationTrend(PathMetadata metadata) {
        super(ReportApplicationTrend.class, metadata);
    }

}

