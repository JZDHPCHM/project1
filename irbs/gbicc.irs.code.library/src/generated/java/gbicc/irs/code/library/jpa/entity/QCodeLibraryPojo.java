package gbicc.irs.code.library.jpa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCodeLibraryPojo is a Querydsl query type for CodeLibraryPojo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCodeLibraryPojo extends EntityPathBase<CodeLibraryPojo> {

    private static final long serialVersionUID = 1023884042L;

    public static final QCodeLibraryPojo codeLibraryPojo = new QCodeLibraryPojo("codeLibraryPojo");

    public final org.wsp.framework.jpa.entity.QAuditorEntity _super = new org.wsp.framework.jpa.entity.QAuditorEntity(this);

    public final StringPath codeName = createString("codeName");

    public final StringPath codeNo = createString("codeNo");

    //inherited
    public final DateTimePath<java.util.Date> createDate = _super.createDate;

    //inherited
    public final StringPath creator = _super.creator;

    public final StringPath id = createString("id");

    public final StringPath isInuse = createString("isInuse");

    public final StringPath itemCode = createString("itemCode");

    public final StringPath itemName = createString("itemName");

    //inherited
    public final StringPath lastModifier = _super.lastModifier;

    //inherited
    public final DateTimePath<java.util.Date> lastModifyDate = _super.lastModifyDate;

    public final StringPath sortNo = createString("sortNo");

    public QCodeLibraryPojo(String variable) {
        super(CodeLibraryPojo.class, forVariable(variable));
    }

    public QCodeLibraryPojo(Path<? extends CodeLibraryPojo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCodeLibraryPojo(PathMetadata metadata) {
        super(CodeLibraryPojo.class, metadata);
    }

}

