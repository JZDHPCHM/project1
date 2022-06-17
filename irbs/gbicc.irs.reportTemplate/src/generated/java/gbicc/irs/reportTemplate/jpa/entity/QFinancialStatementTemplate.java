package gbicc.irs.reportTemplate.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFinancialStatementTemplate is a Querydsl query type for FinancialStatementTemplate
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinancialStatementTemplate extends EntityPathBase<FinancialStatementTemplate> {

    private static final long serialVersionUID = -1317563844L;

    public static final QFinancialStatementTemplate financialStatementTemplate = new QFinancialStatementTemplate("financialStatementTemplate");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final ListPath<FinAccountNorm, QFinAccountNorm> finAccountNorm = this.<FinAccountNorm, QFinAccountNorm>createList("finAccountNorm", FinAccountNorm.class, QFinAccountNorm.class, PathInits.DIRECT2);

    public final ListPath<FinancialAccountTemplateMapping, QFinancialAccountTemplateMapping> financialAccountTemplateMapping = this.<FinancialAccountTemplateMapping, QFinancialAccountTemplateMapping>createList("financialAccountTemplateMapping", FinancialAccountTemplateMapping.class, QFinancialAccountTemplateMapping.class, PathInits.DIRECT2);

    public final StringPath fsFormerType = createString("fsFormerType");

    public final StringPath fsType = createString("fsType");

    public final BooleanPath fsValid = createBoolean("fsValid");

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public QFinancialStatementTemplate(String variable) {
        super(FinancialStatementTemplate.class, forVariable(variable));
    }

    public QFinancialStatementTemplate(Path<? extends FinancialStatementTemplate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinancialStatementTemplate(PathMetadata metadata) {
        super(FinancialStatementTemplate.class, metadata);
    }

}

