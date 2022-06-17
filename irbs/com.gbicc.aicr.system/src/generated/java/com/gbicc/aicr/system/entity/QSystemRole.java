package com.gbicc.aicr.system.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSystemRole is a Querydsl query type for SystemRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSystemRole extends EntityPathBase<SystemRole> {

    private static final long serialVersionUID = 2133528507L;

    public static final QSystemRole systemRole = new QSystemRole("systemRole");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath inputTime = createString("inputTime");

    public final StringPath inputUserId = createString("inputUserId");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath roleId = createString("roleId");

    public final StringPath roleName = createString("roleName");

    public final StringPath roleStatus = createString("roleStatus");

    public final StringPath roleType = createString("roleType");

    public final StringPath updateTime = createString("updateTime");

    public QSystemRole(String variable) {
        super(SystemRole.class, forVariable(variable));
    }

    public QSystemRole(Path<? extends SystemRole> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSystemRole(PathMetadata metadata) {
        super(SystemRole.class, metadata);
    }

}

