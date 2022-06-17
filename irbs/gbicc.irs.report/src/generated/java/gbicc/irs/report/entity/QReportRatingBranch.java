package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportRatingBranch is a Querydsl query type for ReportRatingBranch
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportRatingBranch extends EntityPathBase<ReportRatingBranch> {

    private static final long serialVersionUID = -531359768L;

    public static final QReportRatingBranch reportRatingBranch = new QReportRatingBranch("reportRatingBranch");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<Double> aaaNum = createNumber("aaaNum", Double.class);

    public final NumberPath<Double> aaaPercent = createNumber("aaaPercent", Double.class);

    public final NumberPath<Double> aaMNum = createNumber("aaMNum", Double.class);

    public final NumberPath<Double> aaMPercent = createNumber("aaMPercent", Double.class);

    public final NumberPath<Double> aaNum = createNumber("aaNum", Double.class);

    public final NumberPath<Double> aaPercent = createNumber("aaPercent", Double.class);

    public final NumberPath<Double> aaPNum = createNumber("aaPNum", Double.class);

    public final NumberPath<Double> aaPPercent = createNumber("aaPPercent", Double.class);

    public final NumberPath<Double> aMNum = createNumber("aMNum", Double.class);

    public final NumberPath<Double> aMPercent = createNumber("aMPercent", Double.class);

    public final NumberPath<Double> aNum = createNumber("aNum", Double.class);

    public final NumberPath<Double> aPercent = createNumber("aPercent", Double.class);

    public final NumberPath<Double> aPNum = createNumber("aPNum", Double.class);

    public final NumberPath<Double> aPPercent = createNumber("aPPercent", Double.class);

    public final StringPath area = createString("area");

    public final NumberPath<Double> bbbMNum = createNumber("bbbMNum", Double.class);

    public final NumberPath<Double> bbbMPercent = createNumber("bbbMPercent", Double.class);

    public final NumberPath<Double> bbbNum = createNumber("bbbNum", Double.class);

    public final NumberPath<Double> bbbPercent = createNumber("bbbPercent", Double.class);

    public final NumberPath<Double> bbbPNum = createNumber("bbbPNum", Double.class);

    public final NumberPath<Double> bbbPPercent = createNumber("bbbPPercent", Double.class);

    public final NumberPath<Double> bbMNum = createNumber("bbMNum", Double.class);

    public final NumberPath<Double> bbMPercent = createNumber("bbMPercent", Double.class);

    public final NumberPath<Double> bbNum = createNumber("bbNum", Double.class);

    public final NumberPath<Double> bbPercent = createNumber("bbPercent", Double.class);

    public final NumberPath<Double> bbPNum = createNumber("bbPNum", Double.class);

    public final NumberPath<Double> bbPPercent = createNumber("bbPPercent", Double.class);

    public final NumberPath<Double> bNum = createNumber("bNum", Double.class);

    public final NumberPath<Double> bPercent = createNumber("bPercent", Double.class);

    public final NumberPath<Double> cNum = createNumber("cNum", Double.class);

    public final NumberPath<Double> cPercent = createNumber("cPercent", Double.class);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> dNum = createNumber("dNum", Double.class);

    public final NumberPath<Double> dPercent = createNumber("dPercent", Double.class);

    public final StringPath id = createString("id");

    public final StringPath kindName = createString("kindName");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final StringPath repTime = createString("repTime");

    public final NumberPath<Double> sumNum = createNumber("sumNum", Double.class);

    public final NumberPath<Double> sumPercent = createNumber("sumPercent", Double.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportRatingBranch(String variable) {
        super(ReportRatingBranch.class, forVariable(variable));
    }

    public QReportRatingBranch(Path<? extends ReportRatingBranch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportRatingBranch(PathMetadata metadata) {
        super(ReportRatingBranch.class, metadata);
    }

}

