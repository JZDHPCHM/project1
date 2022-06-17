package gbicc.irs.reportTemplate.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFinancialAccountTemplateMapping is a Querydsl query type for FinancialAccountTemplateMapping
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinancialAccountTemplateMapping extends EntityPathBase<FinancialAccountTemplateMapping> {

    private static final long serialVersionUID = 194979252L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFinancialAccountTemplateMapping financialAccountTemplateMapping = new QFinancialAccountTemplateMapping("financialAccountTemplateMapping");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final NumberPath<Integer> amNumber = createNumber("amNumber", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath faCategory = createString("faCategory");

    public final StringPath faFormerCode = createString("faFormerCode");

    public final StringPath faFormerName = createString("faFormerName");

    public final QFinancialStatementTemplate finanStateTemplate;

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath templateFaCode = createString("templateFaCode");

    public final StringPath templateFaName = createString("templateFaName");

    public QFinancialAccountTemplateMapping(String variable) {
        this(FinancialAccountTemplateMapping.class, forVariable(variable), INITS);
    }

    public QFinancialAccountTemplateMapping(Path<? extends FinancialAccountTemplateMapping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFinancialAccountTemplateMapping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFinancialAccountTemplateMapping(PathMetadata metadata, PathInits inits) {
        this(FinancialAccountTemplateMapping.class, metadata, inits);
    }

    public QFinancialAccountTemplateMapping(Class<? extends FinancialAccountTemplateMapping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.finanStateTemplate = inits.isInitialized("finanStateTemplate") ? new QFinancialStatementTemplate(forProperty("finanStateTemplate")) : null;
    }

}

