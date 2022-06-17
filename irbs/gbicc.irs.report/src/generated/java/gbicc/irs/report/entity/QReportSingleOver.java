package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportSingleOver is a Querydsl query type for ReportSingleOver
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportSingleOver extends EntityPathBase<ReportSingleOver> {

    private static final long serialVersionUID = -1280275003L;

    public static final QReportSingleOver reportSingleOver = new QReportSingleOver("reportSingleOver");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath area = createString("area");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> fdAvgLimitUse = createNumber("fdAvgLimitUse", Double.class);

    public final NumberPath<Double> fdCustomNum = createNumber("fdCustomNum", Double.class);

    public final NumberPath<Double> fdGrantNum = createNumber("fdGrantNum", Double.class);

    public final StringPath fdKindName = createString("fdKindName");

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

    public QReportSingleOver(String variable) {
        super(ReportSingleOver.class, forVariable(variable));
    }

    public QReportSingleOver(Path<? extends ReportSingleOver> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportSingleOver(PathMetadata metadata) {
        super(ReportSingleOver.class, metadata);
    }

}

