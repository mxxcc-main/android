package com.qy.ccm.utils.function.safety;

/**
 * Description:
 * Data：2019/5/8-1:54 PM
 *
 * @author
 */
public class EException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}