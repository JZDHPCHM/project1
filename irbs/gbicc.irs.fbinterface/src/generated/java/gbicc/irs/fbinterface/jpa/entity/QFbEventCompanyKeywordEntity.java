package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbEventCompanyKeywordEntity is a Querydsl query type for FbEventCompanyKeywordEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbEventCompanyKeywordEntity extends EntityPathBase<FbEventCompanyKeywordEntity> {

    private static final long serialVersionUID = -1521352054L;

    public static final QFbEventCompanyKeywordEntity fbEventCompanyKeywordEntity = new QFbEventCompanyKeywordEntity("fbEventCompanyKeywordEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath eventCompanyId = createString("eventCompanyId");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath name = createString("name");

    public QFbEventCompanyKeywordEntity(String variable) {
        super(FbEventCompanyKeywordEntity.class, forVariable(variable));
    }

    public QFbEventCompanyKeywordEntity(Path<? extends FbEventCompanyKeywordEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbEventCompanyKeywordEntity(PathMetadata metadata) {
        super(FbEventCompanyKeywordEntity.class, metadata);
    }

}

