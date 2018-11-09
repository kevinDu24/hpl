package leadu.approval.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import leadu.approval.domain.ApplyDetail;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import leadu.approval.dto.AutoFinancingPreApplyInfoDto;
import leadu.approval.dto.FinancingPreApplyInfoDto;
import leadu.approval.dto.FinancePreApplyResultDto;
import leadu.approval.dto.message.Message;
import leadu.approval.dto.message.MessageType;
import leadu.approval.utils.ApprovalTypeConvert;
import leadu.approval.domain.ApplyInfo;
import leadu.approval.config.PeriodProperties;
import leadu.approval.dao.PurchaseApplyInfoRepository;
import leadu.approval.dto.FinancePreApplyResultListDto;
import leadu.approval.dto.QueryFinancePreApplyResultDto;
import leadu.approval.utils.ApprovalType;
import leadu.approval.dao.ApplyDetailRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Map;

/**
 * Created by yuanzhenxia on 2017/7/19.
 */
@Service
public class ApprovalService {
    @Autowired
    private PurchaseApplyInfoRepository  purchaseApplyInfoRepository;
    @Autowired
    private ApplyDetailRepository applyDetailRepository;
    @Autowired
    private PeriodProperties periodProperties;
    @Autowired
    private ApprovalInterface approvalInterface;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 自动预审批提交
     * @param financingPreApplyInfoDto
     * @return
     */
    @Transactional
    public ResponseEntity<Message> autoFinancingPreApplySubmit(FinancingPreApplyInfoDto financingPreApplyInfoDto){
        String phoneNum = UUID.randomUUID().toString().replace("-", "");
        String period = periodProperties.getPeriod();
        ApplyInfo applyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndVersion(phoneNum, period);
        //若用户已提交预审批，不能再次提交
        if(applyInfo != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败,您已经申请过订单，请勿重复提交！"), HttpStatus.OK);
        }
        // 申请信息保存到本地DB中
        ApplyDetail applyDetail = new ApplyDetail();
        applyDetail.setApprovalUuid(phoneNum);
        applyDetail.setApplicantName(financingPreApplyInfoDto.getApplicantName());
        applyDetail.setIdCardNum(financingPreApplyInfoDto.getIdCardNum());
        applyDetail.setApplicantConmany(financingPreApplyInfoDto.getApplicantConmany());
        applyDetail.setHomeAddress(financingPreApplyInfoDto.getHomeAddress());
        applyDetail.setYearlySalary(financingPreApplyInfoDto.getYearlySalary());
        applyDetail.setApplicantPhoneNum(financingPreApplyInfoDto.getApplicantPhoneNum());
        applyDetail.setContact1Name(financingPreApplyInfoDto.getContact1Name());
        applyDetail.setContact1Mobile(financingPreApplyInfoDto.getContact1Mobile());
        applyDetail.setContact1Relationship(financingPreApplyInfoDto.getContact1Relationship());
        applyDetail.setContact2Name(financingPreApplyInfoDto.getContact2Name());
        applyDetail.setContact2Mobile(financingPreApplyInfoDto.getContact2Mobile());
        applyDetail.setContact2Relationship(financingPreApplyInfoDto.getContact2Relationship());
        applyDetail.setMateName(financingPreApplyInfoDto.getMateName());
        // 配偶手机号
        applyDetail.setMateMobile(financingPreApplyInfoDto.getMateMobile());
        // 配偶身份证号
        applyDetail.setMateIdty(financingPreApplyInfoDto.getMateIdty());
        applyDetailRepository.save(applyDetail);
        //自动预审批提交
        AutoFinancingPreApplyInfoDto autoFinancingPreApplyInfoDto = new AutoFinancingPreApplyInfoDto();
        autoFinancingPreApplyInfoDto.setUserid(phoneNum);
        autoFinancingPreApplyInfoDto.setApplyId(phoneNum);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstCommitTime = sdf.format(new Date());
        autoFinancingPreApplyInfoDto.setFirstCommitTime(firstCommitTime);
        autoFinancingPreApplyInfoDto.setName(financingPreApplyInfoDto.getApplicantName());
        autoFinancingPreApplyInfoDto.setIdty(financingPreApplyInfoDto.getIdCardNum());
        autoFinancingPreApplyInfoDto.setMobile(financingPreApplyInfoDto.getApplicantPhoneNum());
        autoFinancingPreApplyInfoDto.setWorkUnit(financingPreApplyInfoDto.getApplicantConmany());
        autoFinancingPreApplyInfoDto.setDomicile(financingPreApplyInfoDto.getHomeAddress());
        autoFinancingPreApplyInfoDto.setSalary(financingPreApplyInfoDto.getYearlySalary());
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
        FinancePreApplyResultDto financePreApplyResultDto;
        try {
            JSONObject jsonObject = JSONObject.fromObject(autoFinancingPreApplyInfoDto);
            String param = jsonObject.toString();
            String result = approvalInterface.coreServer("autoFinancingPreApplyH5",param);
            financePreApplyResultDto = objectMapper.readValue(result, FinancePreApplyResultDto.class);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，请稍后再试！"), HttpStatus.OK);
        }
        // 更新申请状态为申请中
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
        ApplyInfo newApplyInfo = new ApplyInfo();
        if(applyInfo == null){
            newApplyInfo.setApprovalUuid(phoneNum);
            newApplyInfo.setVersion(period);
            newApplyInfo.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.getApplyResult()));
            //newApplyInfo.setStatus("100");
            newApplyInfo.setCreateTime(new Date());
            newApplyInfo.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
            newApplyInfo.setReason(financePreApplyResultDto.getApplyResultReason());
            //newApplyInfo.setReason("申请中");
            newApplyInfo.setIsAutoApproval("0");
            purchaseApplyInfoRepository.save(newApplyInfo);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,phoneNum), HttpStatus.OK);
    }
    /**
     * 接收主系统推送过来的审批结果
     * @param financePreApplyResultDto
     * @return
     */
    public ResponseEntity<Message> receiveFinancePreApplyResult(FinancePreApplyResultDto financePreApplyResultDto){
        String period = periodProperties.getPeriod();
        ApplyInfo applyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndVersion(financePreApplyResultDto.getApplyId(), period);
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
     * DB中监听查看预审批结果
     * @param phoneNum
     * @return
     */
    public ResponseEntity<Message> getApprovalStatus(String phoneNum){
        String period = periodProperties.getPeriod();
        ApplyInfo applyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndVersion(phoneNum,period);
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"未查询到订单提交信息"),HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, applyInfo),HttpStatus.OK);
    }
    /**
     * 查询审批结果
     */
    public ResponseEntity<Message> queryFinancePreApplyResult(){
        String period = periodProperties.getPeriod();
        List<ApplyInfo> toSubmitList = purchaseApplyInfoRepository.findByVersionAndStatus(period, ApprovalType.SUBMIT.code());
        List<String> applyIdList = new ArrayList<>();
        for(ApplyInfo applyInfo : toSubmitList){
            applyIdList.add(applyInfo.getApprovalUuid());
        }
        List<Map> financePreApplyResultDtos =  new ArrayList<>();//主系统返回状态不在申请中的预审批
        FinancePreApplyResultListDto financePreApplyResultListDto = null;//主系统返回结果
        try {
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
        ApplyInfo applyInfo;
        List<ApplyInfo> applyInfoList = new ArrayList<>();
        for(Map financePreApplyResultDto : financePreApplyResultDtos){
            applyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndVersion(financePreApplyResultDto.get("applyId").toString(), period);
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

}
