package com.daquv.Common;

import org.springframework.context.NoSuchMessageException;

import java.util.List;

public enum  BizErrorCodeEnum implements ErrorCode {

    /**
     * 회원가입 3200~3210
     */
    CREATE_USER_SUCCESS(3200),
    NOT_CONTRIES(3201),
    NOT_EMAIL(3202),
    NOT_NAME(3203),
    NOT_PASSWORD(3204),
    NOT_LOCALECODE(3205),
    NOT_MATCH_PASSWORD(3206),
    NOT_EQUALS_PHONENUMBER(3207),
    NOT_TEMP_USER(3208),
    NOT_USE_EMAIL(3209),
    REGISTER_USER_EXIST(3211),
    CREATE_USER_FAIL(3211),
    NOT_RECOMMENDCODE(3212),

    /**
     * KMC 3300~3310
     */
    PHONE_VERIFICATION_SUCCESS(3300),   // 휴대폰 인증이 성공적으로 완료되었습니다. (PHONE_VERIFICATION_SUCCESS)
    PHONE_NUMBER_USED(3301),            // 이미 사용중인 번호입니다. (PHONE_NUMBER_USED)
    PHONE_VERIFICATION_FAIL_DIFFERENT(3302),    //회원가입 시 입력하신 이름으로 가입된 전화번호를 입력하여 주세요. (PHONE_VERIFICATION_FAIL_DIFFERENT)
    PHONE_VERIFICATION_FAIL_AGE(3303),  //한국식 나이로 20세 이상부터 유트랜스퍼 계정을 만들 수 있습니다. (PHONE_VERIFICATION_FAIL_AGE)
    PHONE_VERIFICATION_FAIL_SIGNUP(3304),   //고객님 명의의 계정이 이미 존재합니다.\n'아이디 찾기' 를 통해 계정을 찾아보세요. (PHONE_VERIFICATION_FAIL_SIGNUP)
    PHONE_NUMBER_COMPARE_FAIL(3305),    //입력하신 휴대폰번호와 인증하신 휴대폰번호가 일치하지 않습니다. (PHONE_NUMBER_COMPARE_FAIL)
    PHONE_VERIFICATION_SESSIONTIME_END(3306),   //휴대폰 본인인증 세션이 만료되었습니다. 재시도하여 주시기 바랍니다. (PHONE_VERIFICATION_SESSIONTIME_END)
    PHONE_VERIFICATION_FAIL(3399),  //본인인증에 실패했습니다. (PHONE_VERIFICATION_FAIL)
    /**
     * 비밀번호 에러 3400 - 3499
     */
    // 로그인 비밀번호
    LOGIN_PWD_EMPTY(3420), // 3420= 로그인 비밀번호 null
    LOGIN_PWD_ERROR(3421), // 3421= 로그인 비밀번호 에러
    LOGIN_PWD_FORMAT_ERROR(3422), // 3422= 로그인 비밀번호 규칙 에러
    // 비밀번호 공통사용
    PWD_CONFIRM_NOT_EQUAL(3430), // 3430 비밀번호 틀림.
    PWD_SETTING_ERROR(3431), // 3431  비밀번호 설정 실패
    PWD_NOT_EQUALS_ORIGINAL(3432), // 3432 새로운 비밀 번호와 기존 비밀번호는 같을 수 없음.

    /**
     * 비밀번호 변경 3500 ~ 3599
     */
    PWD_CHANGE_SUCCESS(3500),  //3500=비밀번호 변경이 완료되었습니다.
    PWD_CHANGE_ACCOUNT_EMPTY(3501),    //3501=등록된 계정정보를 확인하시기 바랍니다.
    PWD_CHANGE_ACCOUNT_NOT_EQUAL(3502),    //3502=본인명의의 등록된 이메일이 일치하지 않습니다.
    PWD_CHANGE_KMC_NOT_CERT(3503), //3503=본인인증 후 비밀번호 변경이 가능합니다.
    PWD_CHANGE_EQUALS_ORIGINAL(3504),   //3504=변경전 비밀번호와 일치합니다.
    PWD_CHANGE_ERROR(3599),    //3599=비밀번호 변경에 실패하였습니다.

    /**
     * 주소 변경 3600 ~ 3699
     */
     ADDRESS_CHANGE_ERROR(3699),    //3699=주소 변경 살패.

    /**
     * 주소 변경 3700 ~ 3799
     */
    ID_EXPIRATION_DATE(3700),
    ID_INVALID(3701),

    /**
     * 환전 3800 ~ 3899
     */
    GENERATE_BARCODE_ERROR(3800),
    NOT_MATCH_ERROR (3801), // 인증번호가 일치하지 않습니다.
    EXPIRED_SERIAL_NUMBER_ERROR (3802), // 인증번호 입력시간이 초과되었습니다.
    EXPIRED_BARCODE_ERROR (3803), // 바코드 유효시간이 초과되었습니다.

    /**
     * 오픈뱅킹 오류코드 4000 ~ 4099
     */
    OPENBANKING_REQUEST_ERROR(4000),                // 유효하지 않은 인증요청
    OPENBANKING_TOKEN_INSERT_ERROR(4001),           // 토큰저장오류

    /**
     * Common, 공통 오류
     */
    COMMON_USER_NOT_EXIST(3901), //3901 존재하지 않는 사용자
    SEARCH_ERROR(3902), //3902 조회 오류
    INSERT_ERROR(3903), //3903 입력 오류
    UPDATE_ERROR(3904), //3904 수정 오류
    DELETE_ERROR(3905), //3905 삭제 오류
    REGISTER_ERROR(3906), //3906 등록 오류
    CANCEL_ERROR(3907), //3907 취소 오류
    BAD_REQUEST_ERROR(3908), //3908 잘못된 요청 파라미터 오류
    UFIN_ERROR(3909), //3909 유핀 내부 로직 상 오류
    NOT_AUTHENTICATE_ERROR(3910), //3910 인증되지 않은 사용자 오류

    TRANSACTION_SUCCESS(1), SYSTEM_ERROR(0), SUCCESS(200), FAIL(-1),DEFAULT(9999),
    ;


    private final int code;

    BizErrorCodeEnum(final int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        String msg = "";
        try {
            msg = LocaleUtils.getMessage("error.code.biz." + this.code);
        } catch (NoSuchMessageException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String getMessage(List args) {
        String msg = "";
        try {
            msg = LocaleUtils.getMessage("error.code.biz." + this.code);
            for(int i=0; i < args.size(); i++){
                msg = msg.replace("{"+i+"}", args.get(i).toString() );
            }
        } catch (NoSuchMessageException e) {
            e.printStackTrace();
        }
        return msg;
    }

    @Override
    public String toString() {
        return "[" + this.getCode() + "]" + this.getMessage();
    }

    public static BizErrorCodeEnum getBizErrorCodeEnum(int code) {
        final BizErrorCodeEnum[] values = BizErrorCodeEnum.values();
        for (BizErrorCodeEnum e : values) {
            if (e != null && e.getCode() == code) {
                return e;
            }
        }
        return null;
    }
}