package com.daquv.Dto;

import com.daquv.Common.BizErrorCodeEnum;
import com.daquv.Common.ErrorCode;
import com.daquv.Common.LocaleUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * Broker Response Result 공통
 * @param <T>
 * 수정 : 20200224 error_code, error_message 추가
 */

@Getter
@Setter
public class ResponseResult<T> implements Serializable {
    private int code = 0;
    private Boolean success=false;
    private String msg = "";
    private String detailMsg = "";

    @JsonIgnoreProperties(ignoreUnknown=true)
    private int error_code = 0;

    @JsonIgnoreProperties(ignoreUnknown=true)
    private String error_message = "";

    @JsonIgnoreProperties(ignoreUnknown = true)
    private T data;

    // success
    public static ResponseResult success() {
        return success("");
    }

    public static <T> ResponseResult success(T data) {
        return build(200, "", "", data, 0, "");
    }

    public static <T> ResponseResult success(String msg, String detailMsg, T data) { return build(200, msg, detailMsg, data, 0, ""); }


    //
    public static ResponseResult success(ErrorCode errorCode) {
        return build(200,  LocaleUtils.getMessage("error.code.biz." + errorCode.getCode()),  LocaleUtils.getMessage("error.code.biz." + errorCode.getCode()), "", 0, "");
    }

    public static <T> ResponseResult success(ErrorCode errorCode, T data) {
        return build(200, LocaleUtils.getMessage("error.code.biz." + errorCode.getCode()), LocaleUtils.getMessage("error.code.biz." + errorCode.getCode()), data, 0, "");
    }

    // failure
    public static ResponseResult failure(ErrorCode errorCode) {
        return failure(errorCode, LocaleUtils.getMessage("error.code.biz." + errorCode.getCode()));
    }

    public static ResponseResult failure(String detailMsg) {
        return failure(BizErrorCodeEnum.FAIL, detailMsg);
    }

    public static ResponseResult failure(ErrorCode errorCode, String detailMsg) {
        return failure(errorCode.getCode(), errorCode.getMessage(), detailMsg);
    }

    public static <T> ResponseResult build(int code, String msg, String detailMsg, T data) {
        return new ResponseResult(code, true, msg, detailMsg, data, 0, "");
    }

    public static <T> ResponseResult build(int code, String msg, String detailMsg, T data, int error_code, String error_message) {
        return new ResponseResult(code, true, msg, detailMsg, data, error_code, error_message);
    }

    public static <T> ResponseResult build(int code, Boolean success, String msg, String detailMsg, T data, int error_code, String error_message) {
        return new ResponseResult(code, success, msg, detailMsg, data, error_code, error_message);
    }

//    public static ResponseResult failure(int code, String msg, String detailMsg) {
//        return build(code, msg, detailMsg, "");
//    }

    public static ResponseResult failure(int code, String msg, String detailMsg) {
        return build(code, false, msg, detailMsg, "", 0, "");
    }

    public static ResponseResult failure(int code, String msg, String detailMsg, String error_message) {
        return build(code, false, msg, detailMsg, "", 0, error_message);
    }

    /*@ConstructorProperties({"code", "msg", "detailMsg", "data"})
    public ResponseResult(int code, String msg, String detailMsg, T data) {
        this.code = code;
        this.msg = msg;
        this.detailMsg = detailMsg;
        this.data = data;
        this.error_code = 0;
        this.error_message = "";
    }*/

    //200224 추가
    @ConstructorProperties({"code", "msg", "detailMsg", "data", "error_code", "error_message"})
    public  ResponseResult(int code, String msg, String detailMsg, T data, int error_code, String error_message) {
        this.code = code;
        this.msg = msg;
        this.success=true;
        this.detailMsg = detailMsg;
        this.data = data;
        this.error_code = error_code;
        this.error_message = error_message;
    }

    @ConstructorProperties({"code", "msg", "detailMsg", "data", "error_code", "error_message"})
    public  ResponseResult(int code, Boolean success, String msg, String detailMsg, T data, int error_code, String error_message) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.detailMsg = detailMsg;
        this.data = data;
        this.error_code = error_code;
        this.error_message = error_message;
    }



}
