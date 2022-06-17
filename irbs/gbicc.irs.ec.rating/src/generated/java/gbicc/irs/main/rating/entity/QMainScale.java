package gbicc.irs.main.rating.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QMainScale is a Querydsl query type for MainScale
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMainScale extends EntityPathBase<MainScale> {

    private static final long serialVersionUID = -32401722L;

    public static final QMainScale mainScale = new QMainScale("mainScale");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath admissionSuggest = createString("admissionSuggest");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<java.math.BigDecimal> pd = createNumber("pd", java.math.BigDecimal.class);

    public final StringPath scaleLevel = createString("scaleLevel");

    public final NumberPath<Integer> sort = createNumber("sort", Integer.class);

    public final StringPath type = createString("type");

    public QMainScale(String variable) {
        super(MainScale.class, forVariable(variable));
    }

    public QMainScale(Path<? extends MainScale> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMainScale(PathMetadata metadata) {
        super(MainScale.class, metadata);
    }

}

