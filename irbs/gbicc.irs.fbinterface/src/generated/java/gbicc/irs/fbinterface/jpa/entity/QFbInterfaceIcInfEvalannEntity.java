package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbInterfaceIcInfEvalannEntity is a Querydsl query type for FbInterfaceIcInfEvalannEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbInterfaceIcInfEvalannEntity extends EntityPathBase<FbInterfaceIcInfEvalannEntity> {

    private static final long serialVersionUID = -950732069L;

    public static final QFbInterfaceIcInfEvalannEntity fbInterfaceIcInfEvalannEntity = new QFbInterfaceIcInfEvalannEntity("fbInterfaceIcInfEvalannEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath evalAnnu = createString("evalAnnu");

    public final StringPath icInfId = createString("icInfId");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QFbInterfaceIcInfEvalannEntity(String variable) {
        super(FbInterfaceIcInfEvalannEntity.class, forVariable(variable));
    }

    public QFbInterfaceIcInfEvalannEntity(Path<? extends FbInterfaceIcInfEvalannEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbInterfaceIcInfEvalannEntity(PathMetadata metadata) {
        super(FbInterfaceIcInfEvalannEntity.class, metadata);
    }

}

