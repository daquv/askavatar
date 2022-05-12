package com.daquv.Config;


import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ThreadInfo;
import java.util.*;

@Slf4j
@Component
public class ScheduleFactory {
   

    
    @Scheduled(cron = "0 */1 * * * *")
    public void foreignBankUrlExpireSchedule() {
        try{
 //           foreignBankUrlService.foreignBankUrlExpire();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
