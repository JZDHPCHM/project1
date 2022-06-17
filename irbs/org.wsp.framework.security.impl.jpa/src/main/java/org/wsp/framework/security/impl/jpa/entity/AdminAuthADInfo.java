package org.wsp.framework.security.impl.jpa.entity;

/**
 * User: lzhang1
 * Date: 2011-10-17
 * Time: 15:08:51
 * Content: 统一用户登录AD认证参数
 */
public class AdminAuthADInfo {
	private String ID = "410";
    private String Version = "0";
    private String AppID = "1";
    private String AuthADName = "生产AD";
    private String Remark ;
    private String Host = "192.168.5.28";
    private int Port =389;
    private String Principal = "CN=hkbdp,OU=管理用户,DC=wucb,DC=com";
    private String Password = "hkbdphkbank";
    private String UserBaseDN = "DC=wucb,DC=com";
    private String UserObjectClass = "person";
    private String AccountNameAttr = "sAMAccountName";
    private String UserNameAttr = "name";
    private String OrgBaseDN ;
    private String OrgObjectClass ;
    private String OrgNameAttr ;
    private String OrgNameFilter ;
    private String RoleBaseDN ;
    private String RoleObjectClass ;
    private String RoleNameAttr ;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getAuthADName() {
        return AuthADName;
    }

    public void setAuthADName(String authADName) {
        AuthADName = authADName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public String getPrincipal() {
        return Principal;
    }

    public void setPrincipal(String principal) {
        Principal = principal;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserBaseDN() {
        return UserBaseDN;
    }

    public void setUserBaseDN(String userBaseDN) {
        UserBaseDN = userBaseDN;
    }

    public String getUserObjectClass() {
        return UserObjectClass;
    }

    public void setUserObjectClass(String userObjectClass) {
        UserObjectClass = userObjectClass;
    }

    public String getAccountNameAttr() {
        return AccountNameAttr;
    }

    public void setAccountNameAttr(String accountNameAttr) {
        AccountNameAttr = accountNameAttr;
    }

    public String getUserNameAttr() {
        return UserNameAttr;
    }

    public void setUserNameAttr(String userNameAttr) {
        UserNameAttr = userNameAttr;
    }

    public String getOrgBaseDN() {
        return OrgBaseDN;
    }

    public void setOrgBaseDN(String orgBaseDN) {
        OrgBaseDN = orgBaseDN;
    }

    public String getOrgObjectClass() {
        return OrgObjectClass;
    }

    public void setOrgObjectClass(String orgObjectClass) {
        OrgObjectClass = orgObjectClass;
    }

    public String getOrgNameAttr() {
        return OrgNameAttr;
    }

    public void setOrgNameAttr(String orgNameAttr) {
        OrgNameAttr = orgNameAttr;
    }

    public String getOrgNameFilter() {
        return OrgNameFilter;
    }

    public void setOrgNameFilter(String orgNameFilter) {
        OrgNameFilter = orgNameFilter;
    }

    public String getRoleBaseDN() {
        return RoleBaseDN;
    }

    public void setRoleBaseDN(String roleBaseDN) {
        RoleBaseDN = roleBaseDN;
    }

    public String getRoleObjectClass() {
        return RoleObjectClass;
    }

    public void setRoleObjectClass(String roleObjectClass) {
        RoleObjectClass = roleObjectClass;
    }

    public String getRoleNameAttr() {
        return RoleNameAttr;
    }

    public void setRoleNameAttr(String roleNameAttr) {
        RoleNameAttr = roleNameAttr;
    }
}
