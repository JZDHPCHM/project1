package gbicc.irs.fbinterface.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFbAnnualReportModifyLogEntity is a Querydsl query type for FbAnnualReportModifyLogEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFbAnnualReportModifyLogEntity extends EntityPathBase<FbAnnualReportModifyLogEntity> {

    private static final long serialVersionUID = -2032364773L;

    public static final QFbAnnualReportModifyLogEntity fbAnnualReportModifyLogEntity = new QFbAnnualReportModifyLogEntity("fbAnnualReportModifyLogEntity");

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

    public final StringPath modifyAfter = createString("modifyAfter");

    public final StringPath modifyBefore = createString("modifyBefore");

    public final StringPath modifyDate = createString("modifyDate");

    public final StringPath modifyDetail = createString("modifyDetail");

    public final StringPath serialNumber = createString("serialNumber");

    public QFbAnnualReportModifyLogEntity(String variable) {
        super(FbAnnualReportModifyLogEntity.class, forVariable(variable));
    }

    public QFbAnnualReportModifyLogEntity(Path<? extends FbAnnualReportModifyLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFbAnnualReportModifyLogEntity(PathMetadata metadata) {
        super(FbAnnualReportModifyLogEntity.class, metadata);
    }

}

