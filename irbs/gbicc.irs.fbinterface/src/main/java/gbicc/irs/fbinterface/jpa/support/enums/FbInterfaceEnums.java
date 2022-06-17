package gbicc.irs.fbinterface.jpa.support.enums;

public enum FbInterfaceEnums {

    /**
     * 风报接口配置
     */
    FB_INTERFACE("FB_INTERFACE"),
    /**
     * APIKEY
     */
    APIKEY("APIKEY"),
    /**
     * 接口请求地址：BASE_URL
     */
    BASE_URL("BASE_URL"),
    /**
     * 股权出质：GQCZ
     */
    GQCZ("GQCZ"),
    /**
     * 工商变更：GSBG
     */
    GSBG("GSBG"),
    /**
     * 年报：NIANBAO
     */
    NIANBAO("NIANBAO"),
    /**
     * 司法协助：SFXZ
     */
    SFXZ("SFXZ"),
    /**
     * 催缴/欠税：QSJL
     */
    QSJL("QSJL"),
    /**
     * 税务非正常户：SWFZCH
     */
    SWFZCH("SWFZCH"),
    /**
     * 股东信息：INVESTOR
     */
    INVESTOR("INVESTOR"),
    /**
     * 经营异常：JYYC
     */
    JYYC("JYYC"),
    /**
     * 裁判文书：CPWS
     */
    CPWS("CPWS"),
    /**
     * 开庭公告：KTGG
     */
    KTGG("KTGG"),
    /**
     * 被执行人：ZHIXING
     */
    ZHIXING("ZHIXING"),
    /**
     * 失信被执行人：SHIXIN
     */
    SHIXIN("SHIXIN"),
    /**
     * 审判流程：SPLC
     */
    SPLC("SPLC"),
    /**
     * 涉诉公告：SSGG
     */
    SSGG("SSGG"),
    /**
     * 重大税收违法：SSWF
     */
    SSWF("SSWF"),
    /**
     * 行政处罚：XZCF2
     */
    XZCF2("XZCF2"),
    /**
     * 事件检索：EVENT
     */
    EVENT("EVENT"),
    /**
     * 事件检索时间长度：月
     */
    EVENT_INTERVAL("EVENT_INTERVAL"),
    /**
     * 事件检索获取原文请求地址：EVENT_NEWS_URL
     */
    EVENT_NEWS_URL("EVENT_NEWS_URL"),
    /**
     * 债券公告：ZQGG
     */
    ZQGG("ZQGG"),
    /**
     * 评级记录：PJJL
     */
    PJJL("PJJL"),
    /**
     * 信息披露：XXPL
     */
    XXPL("XXPL"),
    /**
     * 所有新增关注动态
     */
    FOLLOWING_FEED("FOLLOWING_FEED"),
    /**
     * 全量任务调度开关
     */
    RUNNER_STATUS("RUNNER_STATUS"),
    /**
     * 增量任务调度开关
     */
    RUNNER_STATUS_INCREMENT("RUNNER_STATUS_INCREMENT"),
    /**
     * 规则任务调度开关
     */
    RUNNER_STATUS_RULE("RUNNER_STATUS_RULE"),
    /**
     * 开
     */
    RUNNER_STATUS_Y("Y"),
    /**
     * 关
     */
    RUNNER_STATUS_N("N"),
    /**
     * 接口地址分隔符：/
     */
    SEPRATOR("/"),
    /**
     * 请求地址中apikey参数：?apikey=
     */
    APIKEY_SUFFIX("?apikey="),
    /**
     * 请求地址游标翻页模式参数：&fetch_mode=cursor
     */
    FETCH_MODE("&fetch_mode=cursor"),
    /**
     * 请求地址游标翻页下一页参数：&page_id=
     */
    CURSOR_PAGE("&page_id="),
    /**
     * 接口返回下页地址
     */
    NEXT_PAGE_ID("next_page_id"),
    /**
     * 接口返回内容中关联实体标识：entities
     */
    ENTITIES("entities"),
    /**
     * 请求返回结果标识：hits
     */
    HITS("hits"),
    /**
     * 接口返回内容中当事人标识：当事人
     */
    PARTY("当事人"),
    /**
     * 接口返回内容中当事人详情标识：当事人详情
     */
    PARTY_DETAL("当事人详情"),
    /**
     * 接口返回内容中标识：alias
     */
    ALIAS("alias"),
    /**
     * 接口返回内容中标识：event
     */
    JSON_EVENT("event"),
    /**
     * 接口返回内容中标识：event
     */
    JSON_ENTITY("entity"),
    /**
     * jsonObject空判断
     */
    JSONOBJECT_NULL("{}"),
    /**
     * 增量接口返回类型doc_type
     */
    DOC_TYPE("doc_type"),
    /**
     * 增量接口返回内容content
     */
    CONTENT("content"),
    /**
     * 增量接口返回数据总数total
     */
    TOTAL("total"),
    /**
     * 请求参数from
     */
    FROM_SUFFIX("&from="),
    /**
     * 请求参数size
     */
    SIZE_SUFFIX("&size="),
    /**
     * 请求参数begin
     */
    BEGIN_SUFFIX("&begin="),
    /**
     * 请求参数end
     */
    END_SUFFIX("&end="),
    /**
     * 请求结果每页返回数量
     */
    PAGE_SIZE("50"),
    /**
     * 请求成功CODE
     */
    STATUS_CODE_200("200"),
    /**
     * 请求成功信息
     */
    STATUS_CODE_200_MSG("请求成功"),
    /**
     * 请求参数错误CODE
     */
    STATUS_CODE_400("400"),
    /**
     * 请求参数错误信息
     */
    STATUS_CODE_400_MSG("请求参数错误"),
    /**
     * apiKey无效CODE
     */
    STATUS_CODE_401("401"),
    /**
     * apiKey无效信息
     */
    STATUS_CODE_401_MSG("apiKey无效"),
    /**
     * 公司不存在CODE
     */
    STATUS_CODE_404("404"),
    /**
     * 公司不存在信息
     */
    STATUS_CODE_404_MSG("公司不存在"),
    /**
     * 访问次数超过限制次数CODE
     */
    STATUS_CODE_429("429"),
    /**
     * 关注公司成功:201
     */
    STATUS_CODE_201("201"),
    /**
     * 关注公司成功返回消息:Created
     */
    STATUS_CODE_201_MSG("Created"),
    /**
     * 取消关注公司成功:204
     */
    STATUS_CODE_204("204"),
    /**
     * 取消关注公司成功返回消息:No Content
     */
    STATUS_CODE_204_MSG("No Content"),
    /**
     * 访问次数超过限制次数
     */
    STATUS_CODE_429_MSG("访问次数超过限制次数");
    
    private FbInterfaceEnums(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
