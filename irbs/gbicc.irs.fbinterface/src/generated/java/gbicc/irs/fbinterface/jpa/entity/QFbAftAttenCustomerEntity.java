package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAftAttenCustomerEntity is a Querydsl query type for FbAftAttenCustomerEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAftAttenCustomerEntity extends EntityPathBase<FbAftAttenCustomerEntity> {

    private static final long serialVersionUID = -1469904357L;

    public static final QFbAftAttenCustomerEntity fbAftAttenCustomerEntity = new QFbAftAttenCustomerEntity("fbAftAttenCustomerEntity");

    public final StringPath assoCompanyId = createString("assoCompanyId");

    public final StringPath assoId = createString("assoId");

    public final StringPath assoName = createString("assoName");

    public final StringPath assoType = createString("assoType");

    public final StringPath assoTypeName = createString("assoTypeName");

    public final DateTimePath<java.util.Date> dataDt = createDateTime("dataDt", java.util.Date.class);

    public final StringPath id = createString("id");

    public final StringPath isAtten = createString("isAtten");

    public final StringPath lesseeCompanyId = createString("lesseeCompanyId");

    public final StringPath lesseeId = createString("lesseeId");

    public final StringPath lesseeName = createString("lesseeName");

    public final StringPath partnerCategory = createString("partnerCategory");

    public QFbAftAttenCustomerEntity(String variable) {
        super(FbAftAttenCustomerEntity.class, forVariable(variable));
    }

    public QFbAftAttenCustomerEntity(Path<? extends FbAftAttenCustomerEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAftAttenCustomerEntity(PathMetadata metadata) {
        super(FbAftAttenCustomerEntity.class, metadata);
    }

}

