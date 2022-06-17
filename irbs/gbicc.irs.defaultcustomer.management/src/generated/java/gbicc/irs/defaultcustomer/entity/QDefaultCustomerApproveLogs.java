package gbicc.irs.defaultcustomer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDefaultCustomerApproveLogs is a Querydsl query type for DefaultCustomerApproveLogs
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDefaultCustomerApproveLogs extends EntityPathBase<DefaultCustomerApproveLogs> {

    private static final long serialVersionUID = 1629435189L;

    public static final QDefaultCustomerApproveLogs defaultCustomerApproveLogs = new QDefaultCustomerApproveLogs("defaultCustomerApproveLogs");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath approvalOpinion = createString("approvalOpinion");

    public final StringPath bussId = createString("bussId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath roleCode = createString("roleCode");

    public final StringPath roleName = createString("roleName");

    public final StringPath userCode = createString("userCode");

    public final StringPath userName = createString("userName");

    public QDefaultCustomerApproveLogs(String variable) {
        super(DefaultCustomerApproveLogs.class, forVariable(variable));
    }

    public QDefaultCustomerApproveLogs(Path<? extends DefaultCustomerApproveLogs> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDefaultCustomerApproveLogs(PathMetadata metadata) {
        super(DefaultCustomerApproveLogs.class, metadata);
    }

}

