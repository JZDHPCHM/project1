package gbicc.irs.defaultcustomer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomerOverdue is a Querydsl query type for CustomerOverdue
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomerOverdue extends EntityPathBase<CustomerOverdue> {

    private static final long serialVersionUID = 646618250L;

    public static final QCustomerOverdue customerOverdue = new QCustomerOverdue("customerOverdue");

    public final StringPath custId = createString("custId");

    public final StringPath dataDate = createString("dataDate");

    public final StringPath id = createString("id");

    public final StringPath overdueType = createString("overdueType");

    public QCustomerOverdue(String variable) {
        super(CustomerOverdue.class, forVariable(variable));
    }

    public QCustomerOverdue(Path<? extends CustomerOverdue> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomerOverdue(PathMetadata metadata) {
        super(CustomerOverdue.class, metadata);
    }

}

