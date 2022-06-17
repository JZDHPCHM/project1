package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbIndustrialChangeEntity is a Querydsl query type for FbIndustrialChangeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbIndustrialChangeEntity extends EntityPathBase<FbIndustrialChangeEntity> {

    private static final long serialVersionUID = 2115037515L;

    public static final QFbIndustrialChangeEntity fbIndustrialChangeEntity = new QFbIndustrialChangeEntity("fbIndustrialChangeEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath changeAfter = createString("changeAfter");

    public final StringPath changeBefore = createString("changeBefore");

    public final StringPath changeContent = createString("changeContent");

    public final StringPath changeDate = createString("changeDate");

    public final StringPath changeDetail = createString("changeDetail");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QFbIndustrialChangeEntity(String variable) {
        super(FbIndustrialChangeEntity.class, forVariable(variable));
    }

    public QFbIndustrialChangeEntity(Path<? extends FbIndustrialChangeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbIndustrialChangeEntity(PathMetadata metadata) {
        super(FbIndustrialChangeEntity.class, metadata);
    }

}

