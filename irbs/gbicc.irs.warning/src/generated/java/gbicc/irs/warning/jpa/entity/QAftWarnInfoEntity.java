package gbicc.irs.warning.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAftWarnInfoEntity is a Querydsl query type for AftWarnInfoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAftWarnInfoEntity extends EntityPathBase<AftWarnInfoEntity> {

    private static final long serialVersionUID = -752274524L;

    public static final QAftWarnInfoEntity aftWarnInfoEntity = new QAftWarnInfoEntity("aftWarnInfoEntity");

    public final StringPath assoId = createString("assoId");

    public final StringPath assoType = createString("assoType");

    public final StringPath businessProcess = createString("businessProcess");

    public final StringPath dispResult = createString("dispResult");

    public final StringPath dispTime = createString("dispTime");

    public final StringPath id = createString("id");

    public final StringPath lesseeId = createString("lesseeId");

    public final StringPath ruleCode = createString("ruleCode");

    public final NumberPath<Short> taskSeqno = createNumber("taskSeqno", Short.class);

    public final StringPath warnDesc = createString("warnDesc");

    public final DateTimePath<java.util.Date> warnTime = createDateTime("warnTime", java.util.Date.class);

    public QAftWarnInfoEntity(String variable) {
        super(AftWarnInfoEntity.class, forVariable(variable));
    }

    public QAftWarnInfoEntity(Path<? extends AftWarnInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAftWarnInfoEntity(PathMetadata metadata) {
        super(AftWarnInfoEntity.class, metadata);
    }

}

