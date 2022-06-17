package com.gbicc.aicr.system.in2oa.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.service.SystemDictionaryService;
import org.wsp.framework.mvc.service.UserService;

import com.gbicc.aicr.system.httpclient.service.HttpClientService;
import com.gbicc.aicr.system.in2oa.enums.OARemindEnum;
import com.gbicc.aicr.system.in2oa.service.TaskToOAService;

@Service("flowableTaskToOAService")
public class TaskToOAServiceImpl implements TaskToOAService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String sendOARemind(String userAccount, String issue, String startData, String endData) throws Exception {
        if (StringUtils.isBlank(userAccount) || StringUtils.isBlank(issue) || StringUtils.isBlank(startData)
                || StringUtils.isBlank(endData)) {
            throw new RuntimeException("无法发送OA提醒：用户账号、期次、开始时间、截止时间存在空！");
        }
        Map<String, String> oaRemindMap = systemDictionaryService
                .getDictionaryMap(OARemindEnum.OA_REMIND_INTERFACE.getValue(), Locale.CHINA);
        if (MapUtils.isEmpty(oaRemindMap)) {
            throw new RuntimeException("无法发送OA提醒：数据字典中没有配置OA提醒信息！");
        }
        String onOff = oaRemindMap.get(OARemindEnum.SWITCH.getValue());
        if (StringUtils.isBlank(onOff) || OARemindEnum.ON_OFF_OFF.getValue().equals(onOff)) {
            return "OA提醒没有开启！";
        }
        String address = oaRemindMap.get(OARemindEnum.ADDRESS.getValue());
        String url = oaRemindMap.get(OARemindEnum.URL.getValue());
        String title = oaRemindMap.get(OARemindEnum.TITLE.getValue());
        String content = oaRemindMap.get(OARemindEnum.CONTENT.getValue());
        String sysCode = oaRemindMap.get(OARemindEnum.SYS_CODE.getValue());
        if (StringUtils.isBlank(address) || StringUtils.isBlank(url) || StringUtils.isBlank(title)
                || StringUtils.isBlank(content)) {
            throw new RuntimeException("无法发送OA提醒：数据字典中没有配置全OA提醒信息！");
        }
        //当前人发送OA提醒
        User user = userService.getRepository().findByLoginName(userAccount);
        if (CollectionUtils.isNotEmpty(user.getOrgs())) {
            Org org = user.getOrgs().get(0);
            String xml = getXml(sysCode, title, issue, userAccount, org.getName(), startData, endData, content, url);
            httpClientService.postMethodRequestByString(address, xml);
        }
        //发送OA提醒：被授权人
        List<Map<String, Object>> authList = findAuthUserInfo(user.getId());
        if (CollectionUtils.isNotEmpty(authList)) {
            for (Map<String, Object> temp : authList) {
                Object userAccountObj = temp.get("FD_LOGINNAME");
                Object orgNameObj = temp.get("FD_NAME");
                if (userAccountObj == null || orgNameObj == null || StringUtils.isBlank(userAccountObj.toString())
                        || StringUtils.isBlank(orgNameObj.toString())) {
                    continue;
                }
                String xml = getXml(sysCode, title, issue, userAccountObj.toString(), orgNameObj.toString(), startData,
                        endData, content, url);
                httpClientService.postMethodRequestByString(address, xml);
            }
        }
        return "OA提醒发送成功！";
    }

    /**
     * 组织报文
     * 
     * @param sysCode
     *            系统编码
     * @param title
     *            标题
     * @param issue
     *            任务期次
     * @param userName
     *            发起人
     * @param deptName
     *            发起人部门
     * @param startData
     *            发起日期
     * @param endData
     *            截止日期
     * @param content
     *            工作说明
     * @param url
     *            链接
     * @return
     */
    private String getXml(String sysCode,String title, String issue, String userName, String deptName, String startData,
            String endData, String content, String url) throws Exception {
        if (StringUtils.isBlank(title) || StringUtils.isBlank(issue) || StringUtils.isBlank(userName)
                || StringUtils.isBlank(deptName) || StringUtils.isBlank(startData) || StringUtils.isBlank(endData)
                || StringUtils.isBlank(content) || StringUtils.isBlank(url)) {
            throw new RuntimeException("报文出错：传入的参数存在空！");
        }
        StringBuffer sb = new StringBuffer();
        sb.append(
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cre=\"http://localhost/services/CreateWorkflowService\">");
        sb.append("<soapenv:Header/>");
        sb.append("<soapenv:Body>");
        sb.append("<cre:createWorkflow>");
        sb.append("<cre:in0>" + sysCode + "</cre:in0>");
        sb.append("<cre:in1>");
        //内容体-字符转义-start
        sb.append("&lt;?xml version=&quot;1.0&quot; encoding=&quot;GBK&quot;?&gt;");
        sb.append("&lt;ROOT&gt;");
        sb.append("&lt;workflow requestname=&quot;" + title + "&quot; ");
        sb.append("creatorname=&quot;" + userName + "&quot; ");
        sb.append("requestlevel=&quot;0&quot;&gt;");
        sb.append("&lt;field name=&quot;rwqc&quot;&gt;" + issue + "&lt;/field&gt;");
        sb.append("&lt;field name=&quot;cjr&quot;&gt;" + userName + "&lt;/field&gt;");
        sb.append("&lt;field name=&quot;sqbm&quot;&gt;" + deptName + "&lt;/field&gt;");
        sb.append("&lt;field name=&quot;startdate&quot;&gt;" + startData + "&lt;/field&gt;");
        sb.append("&lt;field name=&quot;enddate&quot;&gt;" + endData + "&lt;/field&gt;");
        sb.append("&lt;field name=&quot;gzsm&quot;&gt;" + content + "&lt;/field&gt;");
        sb.append("&lt;field name=&quot;url&quot;&gt;" + url + "&lt;/field&gt;");
        sb.append("&lt;/workflow&gt;");
        sb.append("&lt;/ROOT&gt;");
        //内容体-字符转义-end
        sb.append("</cre:in1>");
        sb.append("</cre:createWorkflow>");
        sb.append("</soapenv:Body>");
        sb.append("</soapenv:Envelope>");
        return sb.toString();
    }

    /**
     * 查询被授权人信息
     *
     * @param userId
     *            授权人
     * @return
     */
    private List<Map<String, Object>> findAuthUserInfo(String userId) {
        if (StringUtils.isBlank(userId)) {
            return new ArrayList<Map<String, Object>>();
        }
        String sql = "SELECT DISTINCT U.FD_LOGINNAME,ORG.FD_NAME FROM T_IRR_AUTH IA\n"
                + "INNER JOIN FR_AA_USER U ON IA.AUTH_ID=U.FD_ID\n"
                + "INNER JOIN FR_AA_USER_ORG UO ON U.FD_ID=UO.FD_USER_ID\n"
                + "INNER JOIN FR_AA_ORG ORG ON UO.FD_ORG_ID=ORG.FD_ID\n"
                + "WHERE U.FD_ENABLE=1 AND ORG.FD_ENABLE=1 AND IA.USER_ID='" + userId + "'";
        return jdbcTemplate.queryForList(sql);
    }

    /* (non-Javadoc)
     * @see net.gbicc.app.irr.service.IrrTaskService#isProcessCollUser(java.lang.String)
     */
    @Override
    public Boolean isProcessCollUser(String loginName) throws Exception {
        if (StringUtils.isBlank(loginName)) {
            throw new RuntimeException("登录名为空");
        }
        String sql = "SELECT COUNT(R.FD_ID) FROM FR_AA_USER U \r\n"
                + "INNER JOIN FR_AA_USER_ROLE UR ON U.FD_ID=UR.FD_USER_ID\r\n"
                + "INNER JOIN FR_AA_ROLE R ON UR.FD_ROLE_ID=R.FD_ID WHERE U.FD_LOGINNAME='" + loginName
                + "' AND (R.FD_CODE = 'irrProcessColl' OR R.FD_CODE LIKE 'irrProcessSignColl%')";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }


}
