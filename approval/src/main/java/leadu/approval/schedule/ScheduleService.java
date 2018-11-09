package leadu.approval.schedule;

import leadu.approval.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by pengchao on 2017/7/26.
 */
@Service
public class ScheduleService {

    @Autowired
    private ApprovalService approvalService;

    @Scheduled(cron = "00 */10 * * * ?")
    @Transactional
    //定时同步预审批主系统状态
    public void queryFinancePreApplyResult() {
        approvalService.queryFinancePreApplyResult();
    }
}
