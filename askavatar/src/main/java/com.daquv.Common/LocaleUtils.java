package com.daquv.Common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Locale;

@Component
@Slf4j
public class LocaleUtils {

    private static MessageSource messageSource;

    @Autowired
    public LocaleUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String getMessage(String code) {
        String msg = "";
        try {
            //msg = messageSource.getMessage(code, null, Locale.KOREA);
        } catch (Exception e) {
            System.out.println("messages is null");
            //e.printStackTrace();
            //log.info(ExceptionUtils.getStackTrace(e));
        }
        return msg;
    }
}
