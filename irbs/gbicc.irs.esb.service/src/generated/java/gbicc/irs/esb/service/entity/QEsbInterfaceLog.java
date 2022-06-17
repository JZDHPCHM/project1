package gbicc.irs.esb.service.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QEsbInterfaceLog is a Querydsl query type for EsbInterfaceLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QEsbInterfaceLog extends EntityPathBase<EsbInterfaceLog> {

    private static final long serialVersionUID = 411988971L;

    public static final QEsbInterfaceLog esbInterfaceLog = new QEsbInterfaceLog("esbInterfaceLog");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final DateTimePath<java.util.Date> inputDate = createDateTime("inputDate", java.util.Date.class);

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath msg = createString("msg");

    public final StringPath serviceCode = createString("serviceCode");

    public final StringPath serviceScene = createString("serviceScene");

    public final StringPath source = createString("source");

    public final StringPath status = createString("status");

    public final StringPath sysBody = createString("sysBody");

    public final StringPath sysHead = createString("sysHead");

    public QEsbInterfaceLog(String variable) {
        super(EsbInterfaceLog.class, forVariable(variable));
    }

    public QEsbInterfaceLog(Path<? extends EsbInterfaceLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEsbInterfaceLog(PathMetadata metadata) {
        super(EsbInterfaceLog.class, metadata);
    }

}

