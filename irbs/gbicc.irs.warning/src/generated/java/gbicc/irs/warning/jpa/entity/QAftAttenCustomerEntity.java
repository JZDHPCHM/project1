package gbicc.irs.warning.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAftAttenCustomerEntity is a Querydsl query type for AftAttenCustomerEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAftAttenCustomerEntity extends EntityPathBase<AftAttenCustomerEntity> {

    private static final long serialVersionUID = -2140949282L;

    public static final QAftAttenCustomerEntity aftAttenCustomerEntity = new QAftAttenCustomerEntity("aftAttenCustomerEntity");

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

    public QAftAttenCustomerEntity(String variable) {
        super(AftAttenCustomerEntity.class, forVariable(variable));
    }

    public QAftAttenCustomerEntity(Path<? extends AftAttenCustomerEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAftAttenCustomerEntity(PathMetadata metadata) {
        super(AftAttenCustomerEntity.class, metadata);
    }

}

