package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportSingleLimit is a Querydsl query type for ReportSingleLimit
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportSingleLimit extends EntityPathBase<ReportSingleLimit> {

    private static final long serialVersionUID = -1036969750L;

    public static final QReportSingleLimit reportSingleLimit = new QReportSingleLimit("reportSingleLimit");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> fdAvgLimitUse = createNumber("fdAvgLimitUse", Double.class);

    public final NumberPath<Double> fdCustomNum = createNumber("fdCustomNum", Double.class);

    public final NumberPath<Double> fdGrantNum = createNumber("fdGrantNum", Double.class);

    public final StringPath fdGuestGr = createString("fdGuestGr");

    public final NumberPath<Double> fdOverLimitPer = createNumber("fdOverLimitPer", Double.class);

    public final NumberPath<Double> fdSingleLimitNum = createNumber("fdSingleLimitNum", Double.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> quarter = createNumber("quarter", Integer.class);

    public final StringPath repTime = createString("repTime");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportSingleLimit(String variable) {
        super(ReportSingleLimit.class, forVariable(variable));
    }

    public QReportSingleLimit(Path<? extends ReportSingleLimit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportSingleLimit(PathMetadata metadata) {
        super(ReportSingleLimit.class, metadata);
    }

}

