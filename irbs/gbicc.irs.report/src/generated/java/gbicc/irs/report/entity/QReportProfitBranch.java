package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportProfitBranch is a Querydsl query type for ReportProfitBranch
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportProfitBranch extends EntityPathBase<ReportProfitBranch> {

    private static final long serialVersionUID = -634434737L;

    public static final QReportProfitBranch reportProfitBranch = new QReportProfitBranch("reportProfitBranch");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> fdAaaProfit = createNumber("fdAaaProfit", Double.class);

    public final NumberPath<Double> fdAaMProfit = createNumber("fdAaMProfit", Double.class);

    public final NumberPath<Double> fdAaPProfit = createNumber("fdAaPProfit", Double.class);

    public final NumberPath<Double> fdAaProfit = createNumber("fdAaProfit", Double.class);

    public final NumberPath<Double> fdAMProfit = createNumber("fdAMProfit", Double.class);

    public final NumberPath<Double> fdAPProfit = createNumber("fdAPProfit", Double.class);

    public final NumberPath<Double> fdAProfit = createNumber("fdAProfit", Double.class);

    public final StringPath fdArea = createString("fdArea");

    public final NumberPath<Double> fdAvgProfit = createNumber("fdAvgProfit", Double.class);

    public final NumberPath<Double> fdBbbMProfit = createNumber("fdBbbMProfit", Double.class);

    public final NumberPath<Double> fdBbbPProfit = createNumber("fdBbbPProfit", Double.class);

    public final NumberPath<Double> fdBbbProfit = createNumber("fdBbbProfit", Double.class);

    public final NumberPath<Double> fdBbMProfit = createNumber("fdBbMProfit", Double.class);

    public final NumberPath<Double> fdBbPProfit = createNumber("fdBbPProfit", Double.class);

    public final NumberPath<Double> fdBbProfit = createNumber("fdBbProfit", Double.class);

    public final NumberPath<Double> fdBProfit = createNumber("fdBProfit", Double.class);

    public final NumberPath<Double> fdCProfit = createNumber("fdCProfit", Double.class);

    public final NumberPath<Double> fdDProfit = createNumber("fdDProfit", Double.class);

    public final StringPath fdKindName = createString("fdKindName");

    public final NumberPath<Integer> fdRepTime = createNumber("fdRepTime", Integer.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportProfitBranch(String variable) {
        super(ReportProfitBranch.class, forVariable(variable));
    }

    public QReportProfitBranch(Path<? extends ReportProfitBranch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportProfitBranch(PathMetadata metadata) {
        super(ReportProfitBranch.class, metadata);
    }

}

