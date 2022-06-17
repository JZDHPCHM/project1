package gbicc.irs.customer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFinAccountData is a Querydsl query type for FinAccountData
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinAccountData extends EntityPathBase<FinAccountData> {

    private static final long serialVersionUID = 530874891L;

    public static final QFinAccountData finAccountData = new QFinAccountData("finAccountData");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    public final NumberPath<Double> beginValue = createNumber("beginValue", Double.class);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath finStatementId = createString("finStatementId");

    public final StringPath finStatementItem = createString("finStatementItem");

    public final StringPath finStatementType = createString("finStatementType");

    public final StringPath id = createString("id");

    public final StringPath itemDesc = createString("itemDesc");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final NumberPath<Integer> lineCode = createNumber("lineCode", Integer.class);

    public QFinAccountData(String variable) {
        super(FinAccountData.class, forVariable(variable));
    }

    public QFinAccountData(Path<? extends FinAccountData> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinAccountData(PathMetadata metadata) {
        super(FinAccountData.class, metadata);
    }

}

