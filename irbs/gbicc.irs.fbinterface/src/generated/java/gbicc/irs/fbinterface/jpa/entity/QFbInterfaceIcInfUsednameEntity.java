package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbInterfaceIcInfUsednameEntity is a Querydsl query type for FbInterfaceIcInfUsednameEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbInterfaceIcInfUsednameEntity extends EntityPathBase<FbInterfaceIcInfUsednameEntity> {

    private static final long serialVersionUID = -1138764456L;

    public static final QFbInterfaceIcInfUsednameEntity fbInterfaceIcInfUsednameEntity = new QFbInterfaceIcInfUsednameEntity("fbInterfaceIcInfUsednameEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath icInfId = createString("icInfId");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath usedName = createString("usedName");

    public QFbInterfaceIcInfUsednameEntity(String variable) {
        super(FbInterfaceIcInfUsednameEntity.class, forVariable(variable));
    }

    public QFbInterfaceIcInfUsednameEntity(Path<? extends FbInterfaceIcInfUsednameEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbInterfaceIcInfUsednameEntity(PathMetadata metadata) {
        super(FbInterfaceIcInfUsednameEntity.class, metadata);
    }

}

