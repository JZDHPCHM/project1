package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportAnalyseBranch is a Querydsl query type for ReportAnalyseBranch
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportAnalyseBranch extends EntityPathBase<ReportAnalyseBranch> {

    private static final long serialVersionUID = -1352788276L;

    public static final QReportAnalyseBranch reportAnalyseBranch = new QReportAnalyseBranch("reportAnalyseBranch");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath area = createString("area");

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

    public QReportAnalyseBranch(String variable) {
        super(ReportAnalyseBranch.class, forVariable(variable));
    }

    public QReportAnalyseBranch(Path<? extends ReportAnalyseBranch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportAnalyseBranch(PathMetadata metadata) {
        super(ReportAnalyseBranch.class, metadata);
    }

}

