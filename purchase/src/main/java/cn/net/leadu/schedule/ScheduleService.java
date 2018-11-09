package cn.net.leadu.schedule;

import cn.net.leadu.service.ApprovalService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by LEO on 16/9/1.
 */
@Service
public class ScheduleService {


    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private ApprovalService approvalService;

    @Scheduled(cron = "00 00 */1 * * ?")
    @Transactional
    public void queryFinancePreApplyResult() {
        approvalService.queryFinancePreApplyResult();
    }
}
