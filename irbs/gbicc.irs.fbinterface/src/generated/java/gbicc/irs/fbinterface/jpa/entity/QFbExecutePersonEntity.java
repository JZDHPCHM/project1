package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbExecutePersonEntity is a Querydsl query type for FbExecutePersonEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbExecutePersonEntity extends EntityPathBase<FbExecutePersonEntity> {

    private static final long serialVersionUID = -1040747634L;

    public static final QFbExecutePersonEntity fbExecutePersonEntity = new QFbExecutePersonEntity("fbExecutePersonEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath caseDate = createString("caseDate");

    public final StringPath caseNumber = createString("caseNumber");

    public final StringPath caseStatus = createString("caseStatus");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath executionCourt = createString("executionCourt");

    public final StringPath executionName = createString("executionName");

    public final StringPath executionUnderly = createString("executionUnderly");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath publicDate = createString("publicDate");

    public QFbExecutePersonEntity(String variable) {
        super(FbExecutePersonEntity.class, forVariable(variable));
    }

    public QFbExecutePersonEntity(Path<? extends FbExecutePersonEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbExecutePersonEntity(PathMetadata metadata) {
        super(FbExecutePersonEntity.class, metadata);
    }

}

