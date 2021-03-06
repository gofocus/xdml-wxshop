package com.gofocus.wxshop.main.entity;

/**
 * @Author: GoFocus
 * @Date: 2020-06-14 23:15
 * @Description:
 */
public class TelAndCode {
    private String tel;
    private String code;

    public TelAndCode() {
    }

    public TelAndCode(String tel) {
        this.tel = tel;
    }

    public TelAndCode(String tel, String code) {
        this.tel = tel;
        this.code = code;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
