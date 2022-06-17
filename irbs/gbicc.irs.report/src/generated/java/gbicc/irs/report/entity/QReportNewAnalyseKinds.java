package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportNewAnalyseKinds is a Querydsl query type for ReportNewAnalyseKinds
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportNewAnalyseKinds extends EntityPathBase<ReportNewAnalyseKinds> {

    private static final long serialVersionUID = 705231235L;

    public static final QReportNewAnalyseKinds reportNewAnalyseKinds = new QReportNewAnalyseKinds("reportNewAnalyseKinds");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

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

    public QReportNewAnalyseKinds(String variable) {
        super(ReportNewAnalyseKinds.class, forVariable(variable));
    }

    public QReportNewAnalyseKinds(Path<? extends ReportNewAnalyseKinds> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportNewAnalyseKinds(PathMetadata metadata) {
        super(ReportNewAnalyseKinds.class, metadata);
    }

}

