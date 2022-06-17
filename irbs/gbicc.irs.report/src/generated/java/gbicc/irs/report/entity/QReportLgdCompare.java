package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportLgdCompare is a Querydsl query type for ReportLgdCompare
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportLgdCompare extends EntityPathBase<ReportLgdCompare> {

    private static final long serialVersionUID = 1670871781L;

    public static final QReportLgdCompare reportLgdCompare = new QReportLgdCompare("reportLgdCompare");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> fdEadAvgAct = createNumber("fdEadAvgAct", Double.class);

    public final NumberPath<Double> fdEadAvgExp = createNumber("fdEadAvgExp", Double.class);

    public final StringPath fdGuestGr = createString("fdGuestGr");

    public final NumberPath<Double> fdLgdAvg = createNumber("fdLgdAvg", Double.class);

    public final NumberPath<Double> fdLgdIlegal = createNumber("fdLgdIlegal", Double.class);

    public final NumberPath<Double> fdLgdMax = createNumber("fdLgdMax", Double.class);

    public final NumberPath<Double> fdLgdMiddle = createNumber("fdLgdMiddle", Double.class);

    public final NumberPath<Double> fdLgdMin = createNumber("fdLgdMin", Double.class);

    public final StringPath half = createString("half");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Double> repTime = createNumber("repTime", Double.class);

    public final StringPath year = createString("year");

    public QReportLgdCompare(String variable) {
        super(ReportLgdCompare.class, forVariable(variable));
    }

    public QReportLgdCompare(Path<? extends ReportLgdCompare> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportLgdCompare(PathMetadata metadata) {
        super(ReportLgdCompare.class, metadata);
    }

}

