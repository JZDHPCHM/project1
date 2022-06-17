package gbicc.irs.warning.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAftWarnInfoDistinctEntity is a Querydsl query type for AftWarnInfoDistinctEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAftWarnInfoDistinctEntity extends EntityPathBase<AftWarnInfoDistinctEntity> {

    private static final long serialVersionUID = -836513600L;

    public static final QAftWarnInfoDistinctEntity aftWarnInfoDistinctEntity = new QAftWarnInfoDistinctEntity("aftWarnInfoDistinctEntity");

    public final StringPath assoId = createString("assoId");

    public final StringPath assoType = createString("assoType");

    public final StringPath businessProcess = createString("businessProcess");

    public final StringPath dispResult = createString("dispResult");

    public final DateTimePath<java.util.Date> dispTime = createDateTime("dispTime", java.util.Date.class);

    public final StringPath id = createString("id");

    public final StringPath lesseeId = createString("lesseeId");

    public final StringPath pushStatus = createString("pushStatus");

    public final StringPath resultTemp = createString("resultTemp");

    public final StringPath ruleCode = createString("ruleCode");

    public final NumberPath<Short> taskSeqno = createNumber("taskSeqno", Short.class);

    public final StringPath warnDesc = createString("warnDesc");

    public final DateTimePath<java.util.Date> warnTime = createDateTime("warnTime", java.util.Date.class);

    public QAftWarnInfoDistinctEntity(String variable) {
        super(AftWarnInfoDistinctEntity.class, forVariable(variable));
    }

    public QAftWarnInfoDistinctEntity(Path<? extends AftWarnInfoDistinctEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAftWarnInfoDistinctEntity(PathMetadata metadata) {
        super(AftWarnInfoDistinctEntity.class, metadata);
    }

}

