package net.gbicc.app.irr.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 授权管理实体
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
@Entity
@Table(name = "T_IRR_AUTH")
public class IrrAuthEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    /**
     * 授权人ID
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     * 被授权人ID
     */
    @Column(name = "AUTH_ID")
    private String authId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

}
