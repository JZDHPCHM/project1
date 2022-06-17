package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportSingleManage is a Querydsl query type for ReportSingleManage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportSingleManage extends EntityPathBase<ReportSingleManage> {

    private static final long serialVersionUID = -2060028394L;

    public static final QReportSingleManage reportSingleManage = new QReportSingleManage("reportSingleManage");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

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

    public QReportSingleManage(String variable) {
        super(ReportSingleManage.class, forVariable(variable));
    }

    public QReportSingleManage(Path<? extends ReportSingleManage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportSingleManage(PathMetadata metadata) {
        super(ReportSingleManage.class, metadata);
    }

}

