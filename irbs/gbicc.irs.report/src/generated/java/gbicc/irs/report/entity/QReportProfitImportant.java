package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportProfitImportant is a Querydsl query type for ReportProfitImportant
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportProfitImportant extends EntityPathBase<ReportProfitImportant> {

    private static final long serialVersionUID = -2009954475L;

    public static final QReportProfitImportant reportProfitImportant = new QReportProfitImportant("reportProfitImportant");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath compareMonth1 = createString("compareMonth1");

    public final StringPath compareMonth2 = createString("compareMonth2");

    public final StringPath compareMonth3 = createString("compareMonth3");

    public final StringPath compareMonth4 = createString("compareMonth4");

    public final StringPath compareMonth5 = createString("compareMonth5");

    public final NumberPath<Double> compareYear1 = createNumber("compareYear1", Double.class);

    public final NumberPath<Double> compareYear2 = createNumber("compareYear2", Double.class);

    public final NumberPath<Double> compareYear3 = createNumber("compareYear3", Double.class);

    public final NumberPath<Double> compareYear4 = createNumber("compareYear4", Double.class);

    public final NumberPath<Double> compareYear5 = createNumber("compareYear5", Double.class);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath guestGr = createString("guestGr");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> month = createNumber("month", Integer.class);

    public final NumberPath<Double> monthRate1 = createNumber("monthRate1", Double.class);

    public final NumberPath<Double> monthRate2 = createNumber("monthRate2", Double.class);

    public final NumberPath<Double> monthRate3 = createNumber("monthRate3", Double.class);

    public final NumberPath<Double> monthRate4 = createNumber("monthRate4", Double.class);

    public final NumberPath<Double> monthRate5 = createNumber("monthRate5", Double.class);

    public final NumberPath<Double> repTime = createNumber("repTime", Double.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportProfitImportant(String variable) {
        super(ReportProfitImportant.class, forVariable(variable));
    }

    public QReportProfitImportant(Path<? extends ReportProfitImportant> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportProfitImportant(PathMetadata metadata) {
        super(ReportProfitImportant.class, metadata);
    }

}

