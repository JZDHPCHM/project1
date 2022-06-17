package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportMoveCustom is a Querydsl query type for ReportMoveCustom
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportMoveCustom extends EntityPathBase<ReportMoveCustom> {

    private static final long serialVersionUID = 63606571L;

    public static final QReportMoveCustom reportMoveCustom = new QReportMoveCustom("reportMoveCustom");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final NumberPath<Double> fdAaaDistri = createNumber("fdAaaDistri", Double.class);

    public final NumberPath<Double> fdAaDistri = createNumber("fdAaDistri", Double.class);

    public final NumberPath<Double> fdAaMDistri = createNumber("fdAaMDistri", Double.class);

    public final NumberPath<Double> fdAaPDistri = createNumber("fdAaPDistri", Double.class);

    public final NumberPath<Double> fdADistri = createNumber("fdADistri", Double.class);

    public final NumberPath<Double> fdAMDistri = createNumber("fdAMDistri", Double.class);

    public final NumberPath<Double> fdAPDistri = createNumber("fdAPDistri", Double.class);

    public final NumberPath<Double> fdBbbDistri = createNumber("fdBbbDistri", Double.class);

    public final NumberPath<Double> fdBbbMDistri = createNumber("fdBbbMDistri", Double.class);

    public final NumberPath<Double> fdBbbPDistri = createNumber("fdBbbPDistri", Double.class);

    public final NumberPath<Double> fdBbDistri = createNumber("fdBbDistri", Double.class);

    public final NumberPath<Double> fdBbMDistri = createNumber("fdBbMDistri", Double.class);

    public final NumberPath<Double> fdBbPDistri = createNumber("fdBbPDistri", Double.class);

    public final NumberPath<Double> fdBDistri = createNumber("fdBDistri", Double.class);

    public final NumberPath<Double> fdCDistri = createNumber("fdCDistri", Double.class);

    public final NumberPath<Double> fdDDistri = createNumber("fdDDistri", Double.class);

    public final StringPath fdKindName = createString("fdKindName");

    public final NumberPath<Double> fdLastSumDistri = createNumber("fdLastSumDistri", Double.class);

    public final NumberPath<Double> fdMoveDownNum = createNumber("fdMoveDownNum", Double.class);

    public final NumberPath<Double> fdMoveDownPer = createNumber("fdMoveDownPer", Double.class);

    public final NumberPath<Double> fdMovePoint = createNumber("fdMovePoint", Double.class);

    public final NumberPath<Double> fdMoveUpNum = createNumber("fdMoveUpNum", Double.class);

    public final NumberPath<Double> fdMoveUpPer = createNumber("fdMoveUpPer", Double.class);

    public final StringPath fdRepTime = createString("fdRepTime");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> quarter = createNumber("quarter", Integer.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportMoveCustom(String variable) {
        super(ReportMoveCustom.class, forVariable(variable));
    }

    public QReportMoveCustom(Path<? extends ReportMoveCustom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportMoveCustom(PathMetadata metadata) {
        super(ReportMoveCustom.class, metadata);
    }

}

