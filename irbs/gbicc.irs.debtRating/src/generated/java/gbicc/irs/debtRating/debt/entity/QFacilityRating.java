package gbicc.irs.debtRating.debt.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFacilityRating is a Querydsl query type for FacilityRating
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFacilityRating extends EntityPathBase<FacilityRating> {

    private static final long serialVersionUID = -82009974L;

    public static final QFacilityRating facilityRating = new QFacilityRating("facilityRating");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath assetsCode = createString("assetsCode");

    public final StringPath assetsId = createString("assetsId");

    public final StringPath assetsName = createString("assetsName");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath creditId = createString("creditId");

    public final StringPath custCode = createString("custCode");

    public final StringPath custName = createString("custName");

    public final DateTimePath<java.util.Date> fdDate = createDateTime("fdDate", java.util.Date.class);

    public final StringPath fdVersion = createString("fdVersion");

    public final StringPath finalAdvice = createString("finalAdvice");

    public final StringPath finalCode = createString("finalCode");

    public final DateTimePath<java.util.Date> finalDate = createDateTime("finalDate", java.util.Date.class);

    public final StringPath finalLevel = createString("finalLevel");

    public final StringPath finalName = createString("finalName");

    public final NumberPath<java.math.BigDecimal> finalSco = createNumber("finalSco", java.math.BigDecimal.class);

    public final StringPath id = createString("id");

    public final StringPath internCode = createString("internCode");

    public final DateTimePath<java.util.Date> internDate = createDateTime("internDate", java.util.Date.class);

    public final StringPath internLevel = createString("internLevel");

    public final StringPath internName = createString("internName");

    public final NumberPath<java.math.BigDecimal> internSco = createNumber("internSco", java.math.BigDecimal.class);

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath mainId = createString("mainId");

    public final StringPath projectCode = createString("projectCode");

    public final StringPath projectName = createString("projectName");

    public final StringPath ratingStatus = createString("ratingStatus");

    public final StringPath ratingType = createString("ratingType");

    public final StringPath ratingVaild = createString("ratingVaild");

    public final StringPath type = createString("type");

    public final StringPath vaild = createString("vaild");

    public QFacilityRating(String variable) {
        super(FacilityRating.class, forVariable(variable));
    }

    public QFacilityRating(Path<? extends FacilityRating> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFacilityRating(PathMetadata metadata) {
        super(FacilityRating.class, metadata);
    }

}

