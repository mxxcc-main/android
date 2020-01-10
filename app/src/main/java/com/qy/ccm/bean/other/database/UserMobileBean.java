package com.qy.ccm.bean.other.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/5/8-11:47 AM
 *
 * @author
 */
public class UserMobileBean extends LitePalSupport implements Serializable {

    /**
     * The user id
     */
    @Column(defaultValue = "unknown")
    private String uid;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 登录密码
     */
    private String loginPass;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 手机号关联的唯一码
     */
    private String mobileMapping;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getMobileMapping() {
        return mobileMapping;
    }

    public void setMobileMapping(String mobileMapping) {
        this.mobileMapping = mobileMapping;
    }
}
