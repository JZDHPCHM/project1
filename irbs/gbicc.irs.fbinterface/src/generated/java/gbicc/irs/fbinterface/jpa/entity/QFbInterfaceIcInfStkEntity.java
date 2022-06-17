package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbInterfaceIcInfStkEntity is a Querydsl query type for FbInterfaceIcInfStkEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbInterfaceIcInfStkEntity extends EntityPathBase<FbInterfaceIcInfStkEntity> {

    private static final long serialVersionUID = -886028544L;

    public static final QFbInterfaceIcInfStkEntity fbInterfaceIcInfStkEntity = new QFbInterfaceIcInfStkEntity("fbInterfaceIcInfStkEntity");

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

    public final StringPath stkCode = createString("stkCode");

    public final StringPath stkName = createString("stkName");

    public QFbInterfaceIcInfStkEntity(String variable) {
        super(FbInterfaceIcInfStkEntity.class, forVariable(variable));
    }

    public QFbInterfaceIcInfStkEntity(Path<? extends FbInterfaceIcInfStkEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbInterfaceIcInfStkEntity(PathMetadata metadata) {
        super(FbInterfaceIcInfStkEntity.class, metadata);
    }

}

