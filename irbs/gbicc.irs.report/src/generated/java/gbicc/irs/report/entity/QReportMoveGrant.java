package gbicc.irs.report.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportMoveGrant is a Querydsl query type for ReportMoveGrant
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReportMoveGrant extends EntityPathBase<ReportMoveGrant> {

    private static final long serialVersionUID = 698375714L;

    public static final QReportMoveGrant reportMoveGrant = new QReportMoveGrant("reportMoveGrant");

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

    public final StringPath quarter = createString("quarter");

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QReportMoveGrant(String variable) {
        super(ReportMoveGrant.class, forVariable(variable));
    }

    public QReportMoveGrant(Path<? extends ReportMoveGrant> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportMoveGrant(PathMetadata metadata) {
        super(ReportMoveGrant.class, metadata);
    }

}

