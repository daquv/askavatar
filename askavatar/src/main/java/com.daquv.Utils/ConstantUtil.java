package com.daquv.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class ConstantUtil {
    public static final BigDecimal maxYearInbound = new BigDecimal(49500);
    public static final BigDecimal maxYearOutbound = new BigDecimal(49500);
}
