package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportBylawsEntity is a Querydsl query type for FbAnnualReportBylawsEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportBylawsEntity extends EntityPathBase<FbAnnualReportBylawsEntity> {

    private static final long serialVersionUID = -330966467L;

    public static final QFbAnnualReportBylawsEntity fbAnnualReportBylawsEntity = new QFbAnnualReportBylawsEntity("fbAnnualReportBylawsEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

    public final StringPath companyId = createString("companyId");

    public final StringPath consitent = createString("consitent");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath noConsitentDetail = createString("noConsitentDetail");

    public QFbAnnualReportBylawsEntity(String variable) {
        super(FbAnnualReportBylawsEntity.class, forVariable(variable));
    }

    public QFbAnnualReportBylawsEntity(Path<? extends FbAnnualReportBylawsEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportBylawsEntity(PathMetadata metadata) {
        super(FbAnnualReportBylawsEntity.class, metadata);
    }

}

