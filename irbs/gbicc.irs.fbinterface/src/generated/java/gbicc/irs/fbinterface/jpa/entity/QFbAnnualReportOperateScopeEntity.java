package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportOperateScopeEntity is a Querydsl query type for FbAnnualReportOperateScopeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportOperateScopeEntity extends EntityPathBase<FbAnnualReportOperateScopeEntity> {

    private static final long serialVersionUID = 1117796869L;

    public static final QFbAnnualReportOperateScopeEntity fbAnnualReportOperateScopeEntity = new QFbAnnualReportOperateScopeEntity("fbAnnualReportOperateScopeEntity");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath annualReportId = createString("annualReportId");

    public final StringPath companyId = createString("companyId");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath generalOperateItems = createString("generalOperateItems");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath licenseOperateItems = createString("licenseOperateItems");

    public QFbAnnualReportOperateScopeEntity(String variable) {
        super(FbAnnualReportOperateScopeEntity.class, forVariable(variable));
    }

    public QFbAnnualReportOperateScopeEntity(Path<? extends FbAnnualReportOperateScopeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportOperateScopeEntity(PathMetadata metadata) {
        super(FbAnnualReportOperateScopeEntity.class, metadata);
    }

}

