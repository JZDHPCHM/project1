package net.gbicc.app.irr.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QIrrAuthEntity is a Querydsl query type for IrrAuthEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QIrrAuthEntity extends EntityPathBase<IrrAuthEntity> {

    private static final long serialVersionUID = -959288225L;

    public static final QIrrAuthEntity irrAuthEntity = new QIrrAuthEntity("irrAuthEntity");

    public final StringPath authId = createString("authId");

    public final StringPath id = createString("id");

    public final StringPath userId = createString("userId");

    public QIrrAuthEntity(String variable) {
        super(IrrAuthEntity.class, forVariable(variable));
    }

    public QIrrAuthEntity(Path<? extends IrrAuthEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIrrAuthEntity(PathMetadata metadata) {
        super(IrrAuthEntity.class, metadata);
    }

}

