package cn.net.leadu.controller;

import cn.net.leadu.dto.message.Message;
import cn.net.leadu.service.MessageService;
import cn.net.leadu.service.SystemService;
import cn.net.leadu.utils.consts.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pengchao on 2017/5/25.
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private MessageService messageService;

    /**
     * 获取短信验证码
     * @param phoneNum:需要获取短信验证码的手机号
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public ResponseEntity<Message> sendCode(String phoneNum){
        return messageService.sendSingleMt(phoneNum, CommonUtil.projectName,"集采人员登录获取短信验证码"," cn.net.leadu.controller.SystemController.sendCode");
    }


    /**
     * 登录验证接口
     * @param phoneNum
     * @param code
     * @return
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
    public ResponseEntity<Message> verifyCode(String phoneNum, String code){
        return systemService.verifyCode(phoneNum, code);
    }



}
