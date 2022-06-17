package gbicc.irs.reportTemplate.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFinancialKiLib is a Querydsl query type for FinancialKiLib
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinancialKiLib extends EntityPathBase<FinancialKiLib> {

    private static final long serialVersionUID = -1545092102L;

    public static final QFinancialKiLib financialKiLib = new QFinancialKiLib("financialKiLib");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath kiCode = createString("kiCode");

    public final StringPath kiDenominator = createString("kiDenominator");

    public final StringPath kiName = createString("kiName");

    public final StringPath kiNumerator = createString("kiNumerator");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final EnumPath<gbicc.irs.reportTemplate.jpa.support.OutlierHandlingType> outlierHandling = createEnum("outlierHandling", gbicc.irs.reportTemplate.jpa.support.OutlierHandlingType.class);

    public QFinancialKiLib(String variable) {
        super(FinancialKiLib.class, forVariable(variable));
    }

    public QFinancialKiLib(Path<? extends FinancialKiLib> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinancialKiLib(PathMetadata metadata) {
        super(FinancialKiLib.class, metadata);
    }

}

