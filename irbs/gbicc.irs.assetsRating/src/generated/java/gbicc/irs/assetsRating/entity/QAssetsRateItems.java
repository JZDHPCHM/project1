package gbicc.irs.assetsRating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAssetsRateItems is a Querydsl query type for AssetsRateItems
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAssetsRateItems extends EntityPathBase<AssetsRateItems> {

    private static final long serialVersionUID = 706594652L;

    public static final QAssetsRateItems assetsRateItems = new QAssetsRateItems("assetsRateItems");

    public final StringPath assetsRateId = createString("assetsRateId");

    public final StringPath code = createString("code");

    public final StringPath id = createString("id");

    public final StringPath projectNumber = createString("projectNumber");

    public final StringPath valid = createString("valid");

    public final StringPath value = createString("value");

    public QAssetsRateItems(String variable) {
        super(AssetsRateItems.class, forVariable(variable));
    }

    public QAssetsRateItems(Path<? extends AssetsRateItems> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAssetsRateItems(PathMetadata metadata) {
        super(AssetsRateItems.class, metadata);
    }

}

