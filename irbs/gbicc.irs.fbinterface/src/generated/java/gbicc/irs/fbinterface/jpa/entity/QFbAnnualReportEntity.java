package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportEntity is a Querydsl query type for FbAnnualReportEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportEntity extends EntityPathBase<FbAnnualReportEntity> {

    private static final long serialVersionUID = 2105869749L;

    public static final QFbAnnualReportEntity fbAnnualReportEntity = new QFbAnnualReportEntity("fbAnnualReportEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

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

    public final StringPath publicDate = createString("publicDate");

    public final StringPath registeNumber = createString("registeNumber");

    public final StringPath reportDate = createString("reportDate");

    public QFbAnnualReportEntity(String variable) {
        super(FbAnnualReportEntity.class, forVariable(variable));
    }

    public QFbAnnualReportEntity(Path<? extends FbAnnualReportEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportEntity(PathMetadata metadata) {
        super(FbAnnualReportEntity.class, metadata);
    }

}

