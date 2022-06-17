package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportPdCompare is a Querydsl query type for ReportPdCompare
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportPdCompare extends EntityPathBase<ReportPdCompare> {

    private static final long serialVersionUID = -116786520L;

    public static final QReportPdCompare reportPdCompare = new QReportPdCompare("reportPdCompare");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> fdActualIlegalPer = createNumber("fdActualIlegalPer", Double.class);

    public final NumberPath<Double> fdCustomNum = createNumber("fdCustomNum", Double.class);

    public final NumberPath<Double> fdEadNum = createNumber("fdEadNum", Double.class);

    public final NumberPath<Double> fdEadPer = createNumber("fdEadPer", Double.class);

    public final StringPath fdGuestGr = createString("fdGuestGr");

    public final NumberPath<Double> fdIlegalCustomNum = createNumber("fdIlegalCustomNum", Double.class);

    public final NumberPath<Double> fdPdAvg = createNumber("fdPdAvg", Double.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath repTime = createString("repTime");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportPdCompare(String variable) {
        super(ReportPdCompare.class, forVariable(variable));
    }

    public QReportPdCompare(Path<? extends ReportPdCompare> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportPdCompare(PathMetadata metadata) {
        super(ReportPdCompare.class, metadata);
    }

}

