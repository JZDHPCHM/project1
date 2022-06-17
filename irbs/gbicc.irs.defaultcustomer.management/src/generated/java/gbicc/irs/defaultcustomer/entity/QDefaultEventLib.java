package gbicc.irs.defaultcustomer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDefaultEventLib is a Querydsl query type for DefaultEventLib
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDefaultEventLib extends EntityPathBase<DefaultEventLib> {

    private static final long serialVersionUID = -1664928620L;

    public static final QDefaultEventLib defaultEventLib = new QDefaultEventLib("defaultEventLib");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath eventCode = createString("eventCode");

    public final StringPath eventDescribe = createString("eventDescribe");

    public final StringPath eventRulesExpression = createString("eventRulesExpression");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath mark = createString("mark");

    public final StringPath quietPeriod = createString("quietPeriod");

    public final EnumPath<gbicc.irs.defaultcustomer.support.DefaultCustomerAffirmMode> type = createEnum("type", gbicc.irs.defaultcustomer.support.DefaultCustomerAffirmMode.class);

    public final BooleanPath valid = createBoolean("valid");

    public QDefaultEventLib(String variable) {
        super(DefaultEventLib.class, forVariable(variable));
    }

    public QDefaultEventLib(Path<? extends DefaultEventLib> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDefaultEventLib(PathMetadata metadata) {
        super(DefaultEventLib.class, metadata);
    }

}

