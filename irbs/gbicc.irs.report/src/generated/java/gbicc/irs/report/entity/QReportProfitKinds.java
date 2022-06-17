package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportProfitKinds is a Querydsl query type for ReportProfitKinds
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportProfitKinds extends EntityPathBase<ReportProfitKinds> {

    private static final long serialVersionUID = 1095968786L;

    public static final QReportProfitKinds reportProfitKinds = new QReportProfitKinds("reportProfitKinds");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> fdAaaCompareMonth = createNumber("fdAaaCompareMonth", Double.class);

    public final NumberPath<Double> fdAaaCompareYear = createNumber("fdAaaCompareYear", Double.class);

    public final NumberPath<Double> fdAaaMonthRate = createNumber("fdAaaMonthRate", Double.class);

    public final NumberPath<Double> fdAaCompareMonth = createNumber("fdAaCompareMonth", Double.class);

    public final NumberPath<Double> fdAaCompareYear = createNumber("fdAaCompareYear", Double.class);

    public final NumberPath<Double> fdAaMCompareMonth = createNumber("fdAaMCompareMonth", Double.class);

    public final NumberPath<Double> fdAaMCompareYear = createNumber("fdAaMCompareYear", Double.class);

    public final NumberPath<Double> fdAaMMonthRate = createNumber("fdAaMMonthRate", Double.class);

    public final NumberPath<Double> fdAaMonthRate = createNumber("fdAaMonthRate", Double.class);

    public final NumberPath<Double> fdAaPCompareMonth = createNumber("fdAaPCompareMonth", Double.class);

    public final NumberPath<Double> fdAaPCompareYear = createNumber("fdAaPCompareYear", Double.class);

    public final NumberPath<Double> fdAaPMonthRate = createNumber("fdAaPMonthRate", Double.class);

    public final NumberPath<Double> fdACompareMonth = createNumber("fdACompareMonth", Double.class);

    public final NumberPath<Double> fdACompareYear = createNumber("fdACompareYear", Double.class);

    public final NumberPath<Double> fdAMCompareMonth = createNumber("fdAMCompareMonth", Double.class);

    public final NumberPath<Double> fdAMCompareYear = createNumber("fdAMCompareYear", Double.class);

    public final NumberPath<Double> fdAMMonthRate = createNumber("fdAMMonthRate", Double.class);

    public final NumberPath<Double> fdAMonthRate = createNumber("fdAMonthRate", Double.class);

    public final NumberPath<Double> fdAPCompareMonth = createNumber("fdAPCompareMonth", Double.class);

    public final NumberPath<Double> fdAPCompareYear = createNumber("fdAPCompareYear", Double.class);

    public final NumberPath<Double> fdAPMonthRate = createNumber("fdAPMonthRate", Double.class);

    public final NumberPath<Double> fdBbbCompareMonth = createNumber("fdBbbCompareMonth", Double.class);

    public final NumberPath<Double> fdBbbCompareYear = createNumber("fdBbbCompareYear", Double.class);

    public final NumberPath<Double> fdBbbMCompareMonth = createNumber("fdBbbMCompareMonth", Double.class);

    public final NumberPath<Double> fdBbbMCompareYear = createNumber("fdBbbMCompareYear", Double.class);

    public final NumberPath<Double> fdBbbMMonthRate = createNumber("fdBbbMMonthRate", Double.class);

    public final NumberPath<Double> fdBbbMonthRate = createNumber("fdBbbMonthRate", Double.class);

    public final NumberPath<Double> fdBbbPCompareMonth = createNumber("fdBbbPCompareMonth", Double.class);

    public final NumberPath<Double> fdBbbPCompareYear = createNumber("fdBbbPCompareYear", Double.class);

    public final NumberPath<Double> fdBbbPMonthRate = createNumber("fdBbbPMonthRate", Double.class);

    public final NumberPath<Double> fdBbCompareMonth = createNumber("fdBbCompareMonth", Double.class);

    public final NumberPath<Double> fdBbCompareYear = createNumber("fdBbCompareYear", Double.class);

    public final NumberPath<Double> fdBbMCompareMonth = createNumber("fdBbMCompareMonth", Double.class);

    public final NumberPath<Double> fdBbMCompareYear = createNumber("fdBbMCompareYear", Double.class);

    public final NumberPath<Double> fdBbMMonthRate = createNumber("fdBbMMonthRate", Double.class);

    public final NumberPath<Double> fdBbMonthRate = createNumber("fdBbMonthRate", Double.class);

    public final NumberPath<Double> fdBbPCompareMonth = createNumber("fdBbPCompareMonth", Double.class);

    public final NumberPath<Double> fdBbPCompareYear = createNumber("fdBbPCompareYear", Double.class);

    public final NumberPath<Double> fdBbPMonthRate = createNumber("fdBbPMonthRate", Double.class);

    public final NumberPath<Double> fdBCompareMonth = createNumber("fdBCompareMonth", Double.class);

    public final NumberPath<Double> fdBCompareYear = createNumber("fdBCompareYear", Double.class);

    public final NumberPath<Double> fdBMonthRate = createNumber("fdBMonthRate", Double.class);

    public final NumberPath<Double> fdCCompareMonth = createNumber("fdCCompareMonth", Double.class);

    public final NumberPath<Double> fdCCompareYear = createNumber("fdCCompareYear", Double.class);

    public final NumberPath<Double> fdCMonthRate = createNumber("fdCMonthRate", Double.class);

    public final NumberPath<Double> fdDCompareMonth = createNumber("fdDCompareMonth", Double.class);

    public final NumberPath<Double> fdDCompareYear = createNumber("fdDCompareYear", Double.class);

    public final NumberPath<Double> fdDMonthRate = createNumber("fdDMonthRate", Double.class);

    public final StringPath fdKindName = createString("fdKindName");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Double> repTime = createNumber("repTime", Double.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportProfitKinds(String variable) {
        super(ReportProfitKinds.class, forVariable(variable));
    }

    public QReportProfitKinds(Path<? extends ReportProfitKinds> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportProfitKinds(PathMetadata metadata) {
        super(ReportProfitKinds.class, metadata);
    }

}

