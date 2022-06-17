package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportRatingKinds is a Querydsl query type for ReportRatingKinds
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportRatingKinds extends EntityPathBase<ReportRatingKinds> {

    private static final long serialVersionUID = 1099293785L;

    public static final QReportRatingKinds reportRatingKinds = new QReportRatingKinds("reportRatingKinds");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<Double> aaaNum = createNumber("aaaNum", Double.class);

    public final StringPath aaaPercent = createString("aaaPercent");

    public final NumberPath<Double> aaMNum = createNumber("aaMNum", Double.class);

    public final StringPath aaMPercent = createString("aaMPercent");

    public final NumberPath<Double> aaNum = createNumber("aaNum", Double.class);

    public final StringPath aaPercent = createString("aaPercent");

    public final NumberPath<Double> aaPNum = createNumber("aaPNum", Double.class);

    public final StringPath aaPPercent = createString("aaPPercent");

    public final NumberPath<Double> aMNum = createNumber("aMNum", Double.class);

    public final StringPath aMPercent = createString("aMPercent");

    public final NumberPath<Double> aNum = createNumber("aNum", Double.class);

    public final StringPath aPercent = createString("aPercent");

    public final NumberPath<Double> aPNum = createNumber("aPNum", Double.class);

    public final StringPath aPPercent = createString("aPPercent");

    public final NumberPath<Double> bbbMNum = createNumber("bbbMNum", Double.class);

    public final StringPath bbbMPercent = createString("bbbMPercent");

    public final NumberPath<Double> bbbNum = createNumber("bbbNum", Double.class);

    public final StringPath bbbPercent = createString("bbbPercent");

    public final NumberPath<Double> bbbPNum = createNumber("bbbPNum", Double.class);

    public final StringPath bbbPPercent = createString("bbbPPercent");

    public final NumberPath<Double> bbMNum = createNumber("bbMNum", Double.class);

    public final StringPath bbMPercent = createString("bbMPercent");

    public final NumberPath<Double> bbNum = createNumber("bbNum", Double.class);

    public final StringPath bbPercent = createString("bbPercent");

    public final NumberPath<Double> bbPNum = createNumber("bbPNum", Double.class);

    public final StringPath bbPPercent = createString("bbPPercent");

    public final NumberPath<Double> bNum = createNumber("bNum", Double.class);

    public final StringPath bPercent = createString("bPercent");

    public final NumberPath<Double> cNum = createNumber("cNum", Double.class);

    public final StringPath cPercent = createString("cPercent");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> dNum = createNumber("dNum", Double.class);

    public final StringPath dPercent = createString("dPercent");

    public final StringPath id = createString("id");

    public final StringPath kindName = createString("kindName");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Double> repTime = createNumber("repTime", Double.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportRatingKinds(String variable) {
        super(ReportRatingKinds.class, forVariable(variable));
    }

    public QReportRatingKinds(Path<? extends ReportRatingKinds> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportRatingKinds(PathMetadata metadata) {
        super(ReportRatingKinds.class, metadata);
    }

}

