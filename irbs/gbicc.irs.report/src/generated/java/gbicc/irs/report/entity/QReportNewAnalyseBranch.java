package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportNewAnalyseBranch is a Querydsl query type for ReportNewAnalyseBranch
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportNewAnalyseBranch extends EntityPathBase<ReportNewAnalyseBranch> {

    private static final long serialVersionUID = 137603070L;

    public static final QReportNewAnalyseBranch reportNewAnalyseBranch = new QReportNewAnalyseBranch("reportNewAnalyseBranch");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath area = createString("area");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> customerNum = createNumber("customerNum", Double.class);

    public final NumberPath<Double> customerNumPercent = createNumber("customerNumPercent", Double.class);

    public final NumberPath<Double> grantNum = createNumber("grantNum", Double.class);

    public final NumberPath<Double> grantNumPercent = createNumber("grantNumPercent", Double.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath repTime = createString("repTime");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportNewAnalyseBranch(String variable) {
        super(ReportNewAnalyseBranch.class, forVariable(variable));
    }

    public QReportNewAnalyseBranch(Path<? extends ReportNewAnalyseBranch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportNewAnalyseBranch(PathMetadata metadata) {
        super(ReportNewAnalyseBranch.class, metadata);
    }

}

