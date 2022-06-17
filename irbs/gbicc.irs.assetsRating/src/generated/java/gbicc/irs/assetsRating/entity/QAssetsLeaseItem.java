package gbicc.irs.assetsRating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAssetsLeaseItem is a Querydsl query type for AssetsLeaseItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAssetsLeaseItem extends EntityPathBase<AssetsLeaseItem> {

    private static final long serialVersionUID = 2085392681L;

    public static final QAssetsLeaseItem assetsLeaseItem = new QAssetsLeaseItem("assetsLeaseItem");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath amount = createString("amount");

    public final StringPath assetsRatingId = createString("assetsRatingId");

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath isCoreLease = createString("isCoreLease");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath leaseitemName = createString("leaseitemName");

    public final StringPath leaseValue = createString("leaseValue");

    public final StringPath measuringUnit = createString("measuringUnit");

    public final StringPath model = createString("model");

    public final StringPath netWorth = createString("netWorth");

    public final StringPath originalValue = createString("originalValue");

    public final StringPath purchasePrice = createString("purchasePrice");

    public final StringPath serialNumber = createString("serialNumber");

    public final StringPath valuationMethod = createString("valuationMethod");

    public QAssetsLeaseItem(String variable) {
        super(AssetsLeaseItem.class, forVariable(variable));
    }

    public QAssetsLeaseItem(Path<? extends AssetsLeaseItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAssetsLeaseItem(PathMetadata metadata) {
        super(AssetsLeaseItem.class, metadata);
    }

}

