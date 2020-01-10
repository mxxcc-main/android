package com.qy.ccm.config;

import com.qy.ccm.app.MyApp;
import com.qy.ccm.utils.sp.SpKeyUtils;
import com.qy.ccm.utils.sp.SpUtils;

import java.util.List;

/**
 * @author xq
 */
public class Config {

    /**
     * true:首次登录
     * <p>
     * false:不是首次登录
     */
    public static void setFirst(boolean isFirst) {
        SpUtils.putBoolean(MyApp.getSingleInstance(), SpKeyUtils.IS_FIRST, isFirst);

    }


    /**
     * true:首次登录
     * <p>
     * false:不是首次登录
     */
    public static Boolean getFirst() {
        boolean isFirst = SpUtils.getBoolean(MyApp.getSingleInstance(), SpKeyUtils.IS_FIRST, true);
        return isFirst;
    }


    /**
     * 获得交易密码
     *
     * @return
     */
    public static String getTransactionPassword() {
        return SpUtils.getString(MyApp.getSingleInstance(), SpKeyUtils.tPow, "");
    }

    /**
     * 设置交易密码
     */
    public static void setTransactionPassword(String str) {
        SpUtils.putString(MyApp.getSingleInstance(), SpKeyUtils.tPow, str);
    }


    /**
     * 获得密码
     */
    public static String getPassword() {
        return SpUtils.getString(MyApp.getSingleInstance(), SpKeyUtils.password, "");
    }

    /**
     * 设置密码
     */
    public static void setPassword(String str) {
        SpUtils.putString(MyApp.getSingleInstance(), SpKeyUtils.password, str);
    }

    /**
     * 获取Cookie
     */
    public static void setCookie(List<String> cookieList) {
        SpUtils.putStringList(SpKeyUtils.COOKIE, cookieList);
    }

    /**
     * 获取Cookie
     */
    public static List<String> getCookie() {
        return SpUtils.getStringList(SpKeyUtils.COOKIE);
    }

    /**
     * 手机 mobleMapping
     */
    public static String getMobleMapping() {
        return SpUtils.getString(MyApp.getSingleInstance(), SpKeyUtils.MOBILE_MAPPING, "");
    }


    public static void setMobleMapping(String mobleMapping) {
        SpUtils.putString(MyApp.getSingleInstance(), SpKeyUtils.MOBILE_MAPPING, mobleMapping);
    }

    /**
     * 手机 mobleMapping
     */
    public static String getToken() {
        return SpUtils.getString(MyApp.getSingleInstance(), SpKeyUtils.TOKEN, "");
    }


    public static void setToken(String token) {
        SpUtils.putString(MyApp.getSingleInstance(), SpKeyUtils.TOKEN, token);
    }

    /**
     * 手机 mobleMapping
     */
    public static String getCustomerId() {
        return SpUtils.getString(MyApp.getSingleInstance(), SpKeyUtils.CUSTOMERID, "");
    }


    public static void setCustomerId(String customerId) {
        SpUtils.putString(MyApp.getSingleInstance(), SpKeyUtils.CUSTOMERID, customerId);
    }

    /**
     * 邀请码
     */
    public static String getInviteCode() {
        return SpUtils.getString(MyApp.getSingleInstance(), SpKeyUtils.INVITE_CODE, "");
    }


    public static void setInviteCode(String inviteCode) {
        SpUtils.putString(MyApp.getSingleInstance(), SpKeyUtils.INVITE_CODE, inviteCode);
    }
}
