package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportOrgInfoEntity is a Querydsl query type for FbAnnualReportOrgInfoEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportOrgInfoEntity extends EntityPathBase<FbAnnualReportOrgInfoEntity> {

    private static final long serialVersionUID = -1634777565L;

    public static final QFbAnnualReportOrgInfoEntity fbAnnualReportOrgInfoEntity = new QFbAnnualReportOrgInfoEntity("fbAnnualReportOrgInfoEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath address = createString("address");

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

    public final StringPath orgName = createString("orgName");

    public final StringPath registeNumber = createString("registeNumber");

    public final StringPath responsePerson = createString("responsePerson");

    public QFbAnnualReportOrgInfoEntity(String variable) {
        super(FbAnnualReportOrgInfoEntity.class, forVariable(variable));
    }

    public QFbAnnualReportOrgInfoEntity(Path<? extends FbAnnualReportOrgInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportOrgInfoEntity(PathMetadata metadata) {
        super(FbAnnualReportOrgInfoEntity.class, metadata);
    }

}

