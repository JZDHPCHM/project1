package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportAnalyseKinds is a Querydsl query type for ReportAnalyseKinds
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportAnalyseKinds extends EntityPathBase<ReportAnalyseKinds> {

    private static final long serialVersionUID = -1282508555L;

    public static final QReportAnalyseKinds reportAnalyseKinds = new QReportAnalyseKinds("reportAnalyseKinds");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> customerNum = createNumber("customerNum", Double.class);

    public final StringPath customerNumPercent = createString("customerNumPercent");

    public final NumberPath<Double> grantNum = createNumber("grantNum", Double.class);

    public final StringPath grantNumPercent = createString("grantNumPercent");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Double> repTime = createNumber("repTime", Double.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportAnalyseKinds(String variable) {
        super(ReportAnalyseKinds.class, forVariable(variable));
    }

    public QReportAnalyseKinds(Path<? extends ReportAnalyseKinds> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportAnalyseKinds(PathMetadata metadata) {
        super(ReportAnalyseKinds.class, metadata);
    }

}

