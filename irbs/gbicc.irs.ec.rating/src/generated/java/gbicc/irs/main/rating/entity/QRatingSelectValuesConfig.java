package gbicc.irs.main.rating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRatingSelectValuesConfig is a Querydsl query type for RatingSelectValuesConfig
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRatingSelectValuesConfig extends EntityPathBase<RatingSelectValuesConfig> {

    private static final long serialVersionUID = 649617992L;

    public static final QRatingSelectValuesConfig ratingSelectValuesConfig = new QRatingSelectValuesConfig("ratingSelectValuesConfig");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath additID = createString("additID");

    public final StringPath adjustmentFraction = createString("adjustmentFraction");

    public final StringPath adjustmentType = createString("adjustmentType");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath defCode = createString("defCode");

    public final StringPath defId = createString("defId");

    public final StringPath displayValue = createString("displayValue");

    public final StringPath id = createString("id");

    public final BooleanPath isHis = createBoolean("isHis");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> orderNum = createNumber("orderNum", Integer.class);

    public final StringPath realValue = createString("realValue");

    public final StringPath reMarks = createString("reMarks");

    public final StringPath weight = createString("weight");

    public QRatingSelectValuesConfig(String variable) {
        super(RatingSelectValuesConfig.class, forVariable(variable));
    }

    public QRatingSelectValuesConfig(Path<? extends RatingSelectValuesConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRatingSelectValuesConfig(PathMetadata metadata) {
        super(RatingSelectValuesConfig.class, metadata);
    }

}

