package gbicc.irs.customer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomerEarlyWarning is a Querydsl query type for CustomerEarlyWarning
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomerEarlyWarning extends EntityPathBase<CustomerEarlyWarning> {

    private static final long serialVersionUID = -272963050L;

    public static final QCustomerEarlyWarning customerEarlyWarning = new QCustomerEarlyWarning("customerEarlyWarning");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath ctmNo = createString("ctmNo");

    public final StringPath earlyDesc = createString("earlyDesc");

    public final DateTimePath<java.util.Date> earlyDt = createDateTime("earlyDt", java.util.Date.class);

    public final StringPath earlySignal = createString("earlySignal");

    public final StringPath earlyWarning = createString("earlyWarning");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath signalLevel = createString("signalLevel");

    public final StringPath warningDescription = createString("warningDescription");

    public QCustomerEarlyWarning(String variable) {
        super(CustomerEarlyWarning.class, forVariable(variable));
    }

    public QCustomerEarlyWarning(Path<? extends CustomerEarlyWarning> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomerEarlyWarning(PathMetadata metadata) {
        super(CustomerEarlyWarning.class, metadata);
    }

}

