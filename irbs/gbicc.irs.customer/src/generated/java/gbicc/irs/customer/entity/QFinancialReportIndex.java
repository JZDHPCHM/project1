package gbicc.irs.customer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFinancialReportIndex is a Querydsl query type for FinancialReportIndex
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinancialReportIndex extends EntityPathBase<FinancialReportIndex> {

    private static final long serialVersionUID = 2071336980L;

    public static final QFinancialReportIndex financialReportIndex = new QFinancialReportIndex("financialReportIndex");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<Integer> corporateRankings = createNumber("corporateRankings", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath customerNo = createString("customerNo");

    public final StringPath id = createString("id");

    public final StringPath indexCode = createString("indexCode");

    public final StringPath indexName = createString("indexName");

    public final StringPath indexType = createString("indexType");

    public final NumberPath<Double> indexValue = createNumber("indexValue", Double.class);

    public final NumberPath<Double> industryAverage = createNumber("industryAverage", Double.class);

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath reportDate = createString("reportDate");

    public final StringPath reportSpecif = createString("reportSpecif");

    public QFinancialReportIndex(String variable) {
        super(FinancialReportIndex.class, forVariable(variable));
    }

    public QFinancialReportIndex(Path<? extends FinancialReportIndex> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinancialReportIndex(PathMetadata metadata) {
        super(FinancialReportIndex.class, metadata);
    }

}

