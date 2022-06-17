package gbicc.irs.reportTemplate.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFinAccountNorm is a Querydsl query type for FinAccountNorm
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinAccountNorm extends EntityPathBase<FinAccountNorm> {

    private static final long serialVersionUID = 520941882L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFinAccountNorm finAccountNorm = new QFinAccountNorm("finAccountNorm");

    public final StringPath fdCode = createString("fdCode");

    public final StringPath fdId = createString("fdId");

    public final StringPath fdName = createString("fdName");

    public final StringPath fdNorm = createString("fdNorm");

    public final StringPath fdNormConfig = createString("fdNormConfig");

    public final QFinancialStatementTemplate fdNormId;

    public QFinAccountNorm(String variable) {
        this(FinAccountNorm.class, forVariable(variable), INITS);
    }

    public QFinAccountNorm(Path<? extends FinAccountNorm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFinAccountNorm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFinAccountNorm(PathMetadata metadata, PathInits inits) {
        this(FinAccountNorm.class, metadata, inits);
    }

    public QFinAccountNorm(Class<? extends FinAccountNorm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fdNormId = inits.isInitialized("fdNormId") ? new QFinancialStatementTemplate(forProperty("fdNormId")) : null;
    }

}

