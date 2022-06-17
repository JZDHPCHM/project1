package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportRatingImportant is a Querydsl query type for ReportRatingImportant
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportRatingImportant extends EntityPathBase<ReportRatingImportant> {

    private static final long serialVersionUID = 2089797660L;

    public static final QReportRatingImportant reportRatingImportant = new QReportRatingImportant("reportRatingImportant");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> customerNum1 = createNumber("customerNum1", Double.class);

    public final NumberPath<Double> customerNum2 = createNumber("customerNum2", Double.class);

    public final NumberPath<Double> customerNum3 = createNumber("customerNum3", Double.class);

    public final NumberPath<Double> customerNum4 = createNumber("customerNum4", Double.class);

    public final NumberPath<Double> customerNum5 = createNumber("customerNum5", Double.class);

    public final StringPath guestGr = createString("guestGr");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final StringPath percent1 = createString("percent1");

    public final StringPath percent2 = createString("percent2");

    public final StringPath percent3 = createString("percent3");

    public final StringPath percent4 = createString("percent4");

    public final StringPath percent5 = createString("percent5");

    public final NumberPath<Double> repTime = createNumber("repTime", Double.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportRatingImportant(String variable) {
        super(ReportRatingImportant.class, forVariable(variable));
    }

    public QReportRatingImportant(Path<? extends ReportRatingImportant> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportRatingImportant(PathMetadata metadata) {
        super(ReportRatingImportant.class, metadata);
    }

}

