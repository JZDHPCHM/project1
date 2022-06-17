package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportWebsiteEntity is a Querydsl query type for FbAnnualReportWebsiteEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportWebsiteEntity extends EntityPathBase<FbAnnualReportWebsiteEntity> {

    private static final long serialVersionUID = -1842143668L;

    public static final QFbAnnualReportWebsiteEntity fbAnnualReportWebsiteEntity = new QFbAnnualReportWebsiteEntity("fbAnnualReportWebsiteEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

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

    public final StringPath name = createString("name");

    public final StringPath type = createString("type");

    public final StringPath url = createString("url");

    public QFbAnnualReportWebsiteEntity(String variable) {
        super(FbAnnualReportWebsiteEntity.class, forVariable(variable));
    }

    public QFbAnnualReportWebsiteEntity(Path<? extends FbAnnualReportWebsiteEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportWebsiteEntity(PathMetadata metadata) {
        super(FbAnnualReportWebsiteEntity.class, metadata);
    }

}

