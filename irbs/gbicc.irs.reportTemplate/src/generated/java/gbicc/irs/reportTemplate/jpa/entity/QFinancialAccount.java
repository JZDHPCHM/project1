package gbicc.irs.reportTemplate.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFinancialAccount is a Querydsl query type for FinancialAccount
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFinancialAccount extends EntityPathBase<FinancialAccount> {

    private static final long serialVersionUID = 789738784L;

    public static final QFinancialAccount financialAccount = new QFinancialAccount("financialAccount");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath faCategory = createString("faCategory");

    public final StringPath faCode = createString("faCode");

    public final StringPath faName = createString("faName");

    public final NumberPath<Integer> faNumber = createNumber("faNumber", Integer.class);

    public final StringPath id = createString("id");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final SetPath<String, StringPath> statementTemplate = this.<String, StringPath>createSet("statementTemplate", String.class, StringPath.class, PathInits.DIRECT2);

    public QFinancialAccount(String variable) {
        super(FinancialAccount.class, forVariable(variable));
    }

    public QFinancialAccount(Path<? extends FinancialAccount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinancialAccount(PathMetadata metadata) {
        super(FinancialAccount.class, metadata);
    }

}

