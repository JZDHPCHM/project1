package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportModeRufuse is a Querydsl query type for ReportModeRufuse
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportModeRufuse extends EntityPathBase<ReportModeRufuse> {

    private static final long serialVersionUID = -813157328L;

    public static final QReportModeRufuse reportModeRufuse = new QReportModeRufuse("reportModeRufuse");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> fdCustomNum = createNumber("fdCustomNum", Double.class);

    public final NumberPath<Double> fdDownLessThreePer = createNumber("fdDownLessThreePer", Double.class);

    public final NumberPath<Double> fdDownOnePer = createNumber("fdDownOnePer", Double.class);

    public final NumberPath<Double> fdDownThreePer = createNumber("fdDownThreePer", Double.class);

    public final NumberPath<Double> fdDownToatalPer = createNumber("fdDownToatalPer", Double.class);

    public final NumberPath<Double> fdDownTwoPer = createNumber("fdDownTwoPer", Double.class);

    public final StringPath fdGuestGr = createString("fdGuestGr");

    public final NumberPath<Double> fdJudgeRefusePer = createNumber("fdJudgeRefusePer", Double.class);

    public final NumberPath<Double> fdJudgeRefuseTimes = createNumber("fdJudgeRefuseTimes", Double.class);

    public final NumberPath<Double> fdJudgeTimes = createNumber("fdJudgeTimes", Double.class);

    public final NumberPath<Double> fdUpGreatTwoPer = createNumber("fdUpGreatTwoPer", Double.class);

    public final NumberPath<Double> fdUpOnePer = createNumber("fdUpOnePer", Double.class);

    public final NumberPath<Double> fdUpToatalPer = createNumber("fdUpToatalPer", Double.class);

    public final NumberPath<Double> fdUpTwoPer = createNumber("fdUpTwoPer", Double.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> quarter = createNumber("quarter", Integer.class);

    public final StringPath repTime = createString("repTime");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportModeRufuse(String variable) {
        super(ReportModeRufuse.class, forVariable(variable));
    }

    public QReportModeRufuse(Path<? extends ReportModeRufuse> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportModeRufuse(PathMetadata metadata) {
        super(ReportModeRufuse.class, metadata);
    }

}

