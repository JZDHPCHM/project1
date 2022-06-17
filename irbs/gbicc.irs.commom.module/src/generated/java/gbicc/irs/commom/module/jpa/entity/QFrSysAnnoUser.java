package gbicc.irs.commom.module.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFrSysAnnoUser is a Querydsl query type for FrSysAnnoUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFrSysAnnoUser extends EntityPathBase<FrSysAnnoUser> {

    private static final long serialVersionUID = -1269949500L;

    public static final QFrSysAnnoUser frSysAnnoUser = new QFrSysAnnoUser("frSysAnnoUser");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath aId = createString("aId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath uId = createString("uId");

    public QFrSysAnnoUser(String variable) {
        super(FrSysAnnoUser.class, forVariable(variable));
    }

    public QFrSysAnnoUser(Path<? extends FrSysAnnoUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFrSysAnnoUser(PathMetadata metadata) {
        super(FrSysAnnoUser.class, metadata);
    }

}

