package cn.net.leadu.controller;

import cn.net.leadu.dto.FinancePreApplyResultDto;
import cn.net.leadu.dto.FinancingPreApplyInfoDto;
import cn.net.leadu.dto.message.Message;
import cn.net.leadu.service.ApprovalService;
import cn.net.leadu.service.ContractService;
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

    @Autowired
    private ContractService contractService;


    /**
     *自动预审批
     * @param financingPreApplyInfoDto
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/autoFinancingPreApplySubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> autoFinancingPreApplySubmit(@RequestBody FinancingPreApplyInfoDto financingPreApplyInfoDto, String phoneNum){
        return approvalService.autoFinancingPreApplySubmit(financingPreApplyInfoDto, phoneNum);
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
     * 查询审批结果
     * @param
     * @return
     */
    @RequestMapping(value = "/queryFinancePreApplyResult", method = RequestMethod.GET)
    public ResponseEntity<Message> queryFinancePreApplyResult(){
        return  approvalService.queryFinancePreApplyResult();
    }

    /**
     * 合同审批日志查询
     * url:localhost:8081/approval/36157040/log
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/{phoneNum}/log", method = RequestMethod.GET)
    public ResponseEntity<Message> getContract(@PathVariable String phoneNum){
        return contractService.getContractMsg(phoneNum);
    }

    /**
     * DB中监听查看预审批结果
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/getApprovalStatus", method = RequestMethod.POST)
    public ResponseEntity<Message> getApprovalStatus(String phoneNum){
        return  approvalService.getApprovalStatus(phoneNum);
    }


}

