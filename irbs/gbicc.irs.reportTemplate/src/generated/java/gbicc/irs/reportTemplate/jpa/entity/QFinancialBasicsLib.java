package gbicc.irs.reportTemplate.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFinancialBasicsLib is a Querydsl query type for FinancialBasicsLib
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinancialBasicsLib extends EntityPathBase<FinancialBasicsLib> {

    private static final long serialVersionUID = -1141475213L;

    public static final QFinancialBasicsLib financialBasicsLib = new QFinancialBasicsLib("financialBasicsLib");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath basicsCode = createString("basicsCode");

    public final StringPath basicsDescribe = createString("basicsDescribe");

    public final StringPath basicsName = createString("basicsName");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath generalenterprise = createString("generalenterprise");

    public final StringPath generalenterpriseold = createString("generalenterpriseold");

    public final StringPath id = createString("id");

    public final StringPath institution = createString("institution");

    public final StringPath institutionold = createString("institutionold");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath smallenterprise = createString("smallenterprise");

    public final StringPath smallenterpriseold = createString("smallenterpriseold");

    public QFinancialBasicsLib(String variable) {
        super(FinancialBasicsLib.class, forVariable(variable));
    }

    public QFinancialBasicsLib(Path<? extends FinancialBasicsLib> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinancialBasicsLib(PathMetadata metadata) {
        super(FinancialBasicsLib.class, metadata);
    }

}

