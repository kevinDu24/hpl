package leadu.approval.controller;

import leadu.approval.dto.FinancePreApplyResultDto;
import leadu.approval.dto.FinancingPreApplyInfoDto;
import leadu.approval.dto.message.Message;
import leadu.approval.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Created by zcHu on 17/5/10.
 */
@RestController
@RequestMapping("/approval")
public class ApprovalController {
    @Autowired
    private ApprovalService approvalService;



    /**
     *自动预审批
     * @param financingPreApplyInfoDto
     * @return
     */
    @RequestMapping(value = "/autoFinancingPreApplySubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> autoFinancingPreApplySubmit(@RequestBody FinancingPreApplyInfoDto financingPreApplyInfoDto){
        return approvalService.autoFinancingPreApplySubmit(financingPreApplyInfoDto);
    }
    /**
     * 接收审批结果
     * @param financePreApplyResultDto
     * @return
     */
    @RequestMapping(value = "/receiveFinancePreApplyResult", method = RequestMethod.POST)
    public ResponseEntity<Message> receiveFinancePreApplyResult(@RequestBody FinancePreApplyResultDto financePreApplyResultDto){
        return  approvalService.receiveFinancePreApplyResult(financePreApplyResultDto);
    }

    /**
     * DB中监听查看预审批结果
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getApprovalStatus", method = RequestMethod.POST)
    public ResponseEntity<Message> getApprovalStatus(String applyNum){
        return  approvalService.getApprovalStatus(applyNum);
    }

    /**
     * 查询审批结果
     * @param
     * @return
     */
    @RequestMapping(value = "/queryFinancePreApplyResult", method = RequestMethod.GET)
    public ResponseEntity<Message> queryFinancePreApplyResult(){
        return  approvalService.queryFinancePreApplyResult();
    }


}

