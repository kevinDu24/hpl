package cn.net.leadu.service;

import cn.net.leadu.config.PeriodProperties;
import cn.net.leadu.dao.PurchaseApplyInfoRepository;
import cn.net.leadu.dao.PurchaseApprovalRepository;
import cn.net.leadu.domain.PurchaseApplyInfo;
import cn.net.leadu.domain.PurchaseApproval;
import cn.net.leadu.dto.*;
import cn.net.leadu.dto.message.Message;
import cn.net.leadu.dto.message.MessageType;
import cn.net.leadu.utils.commons.ItemSerialize;
import cn.net.leadu.utils.status.ApprovalType;
import cn.net.leadu.utils.status.ApprovalTypeConvert;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by pengchao on 2017/6/30.
 */
@Service
public class ApprovalService {
    @Autowired
    private PeriodProperties periodProperties;

    @Autowired
    private PurchaseApplyInfoRepository purchaseApplyInfoRepository;

    @Autowired
    private PurchaseApprovalRepository purchaseApprovalRepository;

    @Autowired
    private ApprovalInterface approvalInterface;


    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 自动预审批提交
     * @param financingPreApplyInfoDto
     * @param phoneNum
     * @return
     */
    @Transactional
    public ResponseEntity<Message> autoFinancingPreApplySubmit(FinancingPreApplyInfoDto financingPreApplyInfoDto, String phoneNum){
        String period = periodProperties.getPeriod();
        PurchaseApplyInfo purchaseApplyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndPeriod(phoneNum, period);
        //若用户已提交预审批，不能再次提交
        if(purchaseApplyInfo != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败,您已经申请过订单，请勿重复提交！"), HttpStatus.OK);
        }
        List<PurchaseApproval> approvalListOld = new ArrayList();
        List<PurchaseApproval> approvalListNew = new ArrayList();
        approvalListOld = purchaseApprovalRepository.findFullInfo(phoneNum, phoneNum, period);
        try {
            approvalListNew =  ItemSerialize.serialize(financingPreApplyInfoDto, phoneNum, period, phoneNum);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "序列化异常"), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            purchaseApprovalRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()){
            purchaseApprovalRepository.save(approvalListNew);
        }

        //自动预审批提交
        AutoFinancingPreApplyInfoDto autoFinancingPreApplyInfoDto = new AutoFinancingPreApplyInfoDto();
        autoFinancingPreApplyInfoDto.setUserid(phoneNum);
        autoFinancingPreApplyInfoDto.setApplyId(phoneNum);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstCommitTime = sdf.format(new Date());
        autoFinancingPreApplyInfoDto.setFirstCommitTime(firstCommitTime);
        autoFinancingPreApplyInfoDto.setName(financingPreApplyInfoDto.getIdCardName());
        autoFinancingPreApplyInfoDto.setIdty(financingPreApplyInfoDto.getIdCardNum());
        autoFinancingPreApplyInfoDto.setMobile(financingPreApplyInfoDto.getMobile());
        autoFinancingPreApplyInfoDto.setContact1Name(financingPreApplyInfoDto.getContact1Name());
        autoFinancingPreApplyInfoDto.setContact1Mobile(financingPreApplyInfoDto.getContact1Mobile());
        autoFinancingPreApplyInfoDto.setContact1Relationship(financingPreApplyInfoDto.getContact1Relationship());
        autoFinancingPreApplyInfoDto.setContact2Name(financingPreApplyInfoDto.getContact2Name());
        autoFinancingPreApplyInfoDto.setContact2Mobile(financingPreApplyInfoDto.getContact2Mobile());
        autoFinancingPreApplyInfoDto.setContact2Relationship(financingPreApplyInfoDto.getContact2Relationship());
        autoFinancingPreApplyInfoDto.setMateName(financingPreApplyInfoDto.getMateName());
        // 配偶手机号
        String mateMobile = financingPreApplyInfoDto.getMateMobile();
        // 配偶身份证号
        String mateIdty = financingPreApplyInfoDto.getMateIdty();
        autoFinancingPreApplyInfoDto.setMateMobile(mateMobile);
        autoFinancingPreApplyInfoDto.setMateIdty(mateIdty);
        autoFinancingPreApplyInfoDto.setCompany(financingPreApplyInfoDto.getMateCompany());
        autoFinancingPreApplyInfoDto.setAddress(financingPreApplyInfoDto.getMateAddress());


        FinancePreApplyResultDto financePreApplyResultDto;
        try {
            JSONObject jsonObject = JSONObject.fromObject(autoFinancingPreApplyInfoDto);
            String param = jsonObject.toString();
            String result = approvalInterface.coreServer("autoFinancingPreApply",param);
            financePreApplyResultDto = objectMapper.readValue(result, FinancePreApplyResultDto.class);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统异常"), HttpStatus.OK);
        }
        // 更新申请状态为申请中
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
//        PurchaseApplyInfo purchaseApplyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndPeriod(phoneNum, period);
        PurchaseApplyInfo newApplyInfo = new PurchaseApplyInfo();
        if(purchaseApplyInfo == null){
            newApplyInfo.setApprovalUuid(phoneNum);
            newApplyInfo.setPeriod(period);
            newApplyInfo.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.getApplyResult()));
            newApplyInfo.setCreateTime(new Date());
            newApplyInfo.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
            newApplyInfo.setReason(financePreApplyResultDto.getApplyResultReason());
            newApplyInfo.setIsAutoApproval("0");
            purchaseApplyInfoRepository.save(newApplyInfo);
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
//        purchaseApplyInfo.setApprovalUuid(phoneNum);
//        purchaseApplyInfo.setPeriod(period);
//        purchaseApplyInfo.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.getApplyResult()));
//        purchaseApplyInfo.setUpdateTime(new Date());
//        purchaseApplyInfo.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
//        purchaseApplyInfo.setReason(financePreApplyResultDto.getApplyResultReason());
//        purchaseApplyInfo.setIsAutoApproval("0");
//        purchaseApplyInfoRepository.save(purchaseApplyInfo);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }
    /**
     * 接收主系统推送过来的审批结果
     * @param financePreApplyResultDto
     * @return
     */
    public ResponseEntity<Message> receiveFinancePreApplyResult(FinancePreApplyResultDto financePreApplyResultDto){
        String period = periodProperties.getPeriod();
        PurchaseApplyInfo applyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndPeriod(financePreApplyResultDto.getApplyId(), period);
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
        if(applyInfo != null){
            applyInfo.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
            applyInfo.setReason(financePreApplyResultDto.getApplyResultReason());
            applyInfo.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.getApplyResult()));
            applyInfo.setUpdateTime(new Date());
            purchaseApplyInfoRepository.save(applyInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return  new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "审批结果推送失败"), HttpStatus.OK);
    }


    /**
     * 查询审批结果
     */
    public ResponseEntity<Message> queryFinancePreApplyResult(){
        String period = periodProperties.getPeriod();
        List<PurchaseApplyInfo> toSubmitList = purchaseApplyInfoRepository.findByPeriodAndStatus(period, ApprovalType.SUBMIT.code());
        List<String> applyIdList = new ArrayList<>();
        for(PurchaseApplyInfo applyInfo : toSubmitList){
            applyIdList.add(applyInfo.getApprovalUuid());
        }

        List<Map> financePreApplyResultDtos =  new ArrayList<>();//主系统返回状态不在申请中的预审批
        FinancePreApplyResultListDto financePreApplyResultListDto = null;//主系统返回结果
        try {
            String financePreApplyResultList = null;
            QueryFinancePreApplyResultDto queryFinancePreApplyResultDto = new QueryFinancePreApplyResultDto();
            queryFinancePreApplyResultDto.setParam(applyIdList);
            JSONObject jsonObject = JSONObject.fromObject(queryFinancePreApplyResultDto);
            String param = jsonObject.toString();
            String result = approvalInterface.coreServer("qureyFinancePreApplyResult", param );
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到主系统结果"), HttpStatus.OK);
            }
            financePreApplyResultListDto = objectMapper.readValue(result, FinancePreApplyResultListDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统异常"), HttpStatus.OK);
        }
//        Boolean isSuccess = true;
//        if(financePreApplyResultListDto != null && isSuccess != (financePreApplyResultListDto.getResult().getIsSuccess())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, financePreApplyResultListDto.getResult().getResultMsg()), HttpStatus.OK);
//        }
        List<Map> financePreApplyResultList =  (List<Map>) financePreApplyResultListDto.getLr();
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
        if(financePreApplyResultList == null || financePreApplyResultList.size()== 0){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
        }
        for(Map financePreApplyResult : financePreApplyResultList){
            if(financePreApplyResult != null && financePreApplyResult.get("applyResult") != null && !ApprovalType.SUBMIT.code().equals(approvalTypeConvert.typeConvert(financePreApplyResult.get("applyResult").toString()))){
                financePreApplyResultDtos.add(financePreApplyResult);
            }
        }
        PurchaseApplyInfo applyInfo;
        List<PurchaseApplyInfo> applyInfoList = new ArrayList<>();
        for(Map financePreApplyResultDto : financePreApplyResultDtos){
            applyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndPeriod(financePreApplyResultDto.get("applyId").toString(), period);
            if(applyInfo != null){
                applyInfo.setApplyNum(financePreApplyResultDto.get("applyIdOfMaster").toString());
                applyInfo.setReason(financePreApplyResultDto.get("applyResultReason").toString());
                applyInfo.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.get("applyResult").toString()));
                applyInfo.setUpdateTime(new Date());
                applyInfoList.add(applyInfo);
            }
        }
        purchaseApplyInfoRepository.save(applyInfoList);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }
    /**
     * DB中监听查看预审批结果
     * @param phoneNum
     * @return
     */
    public ResponseEntity<Message> getApprovalStatus(String phoneNum){
        String period = periodProperties.getPeriod();
        PurchaseApplyInfo purchaseApplyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndPeriod(phoneNum,period);
        if(purchaseApplyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"此用户不存在"),HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,purchaseApplyInfo),HttpStatus.OK);
    }

}
