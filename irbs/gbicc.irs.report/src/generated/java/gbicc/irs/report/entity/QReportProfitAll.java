package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportProfitAll is a Querydsl query type for ReportProfitAll
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportProfitAll extends EntityPathBase<ReportProfitAll> {

    private static final long serialVersionUID = -1353057516L;

    public static final QReportProfitAll reportProfitAll = new QReportProfitAll("reportProfitAll");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<Double> compareMonthRate = createNumber("compareMonthRate", Double.class);

    public final NumberPath<Double> compareYearRate = createNumber("compareYearRate", Double.class);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath guestGr = createString("guestGr");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Double> lastMonthRate = createNumber("lastMonthRate", Double.class);

    public final NumberPath<Double> lastYearRate = createNumber("lastYearRate", Double.class);

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Double> monthRate = createNumber("monthRate", Double.class);

    public final StringPath repTime = createString("repTime");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportProfitAll(String variable) {
        super(ReportProfitAll.class, forVariable(variable));
    }

    public QReportProfitAll(Path<? extends ReportProfitAll> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportProfitAll(PathMetadata metadata) {
        super(ReportProfitAll.class, metadata);
    }

}

