package cn.net.leadu.service.impl;

import cn.net.leadu.config.EtonenetParam;
import cn.net.leadu.dao.MessageLogRepository;
import cn.net.leadu.dao.RedisRepository;
import cn.net.leadu.domain.MessageLog;
import cn.net.leadu.dto.message.Message;
import cn.net.leadu.dto.message.MessageType;
import cn.net.leadu.service.MessageService;
import cn.net.leadu.utils.consts.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pengchao on 2017/2/18.
 */
@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private EtonenetParam etonenetParam;
    @Autowired
    MessageLogRepository messageLogRepository;
    /**
     * Hex编码字符组
     */
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    //集采用户登录发送验证码
    public ResponseEntity<Message> sendSingleMt(String phoneNum, String projectName, String serviceName, String classFunctionName) {
        if(containInRedis(phoneNum)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"一天内仅能获取一次短信验证码,请输入上次获得的短信验证码。"), HttpStatus.OK);
        }
        return sendCode(phoneNum, 24*60*60,projectName,serviceName,classFunctionName);
    }

    /**
     * 发送短信验证码
     * @param phoneNum
     * @return
     */
    public ResponseEntity<Message> sendCode(String phoneNum, Integer saveTime,String projectName,String serviceName,String classFunctionName){
        //发送http请求，并接收http响应
        String resStr = doGetRequest(makeSmsUrl(phoneNum, saveTime,projectName,serviceName,classFunctionName));
        //解析响应字符串
        HashMap pp = parseResStr(resStr);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 发送消息
     * @param phoneNums
     * @return
     */
    public ResponseEntity<Message> sendMessage(List<String> phoneNums, String content,String projectName,String serviceName,String classFunctionName) {
        phoneNums.forEach(phoneNum -> doGetRequest(setSmsParam(phoneNum, content,projectName,serviceName,classFunctionName)));
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> sendMessage(String phoneNum, String content,String projectName,String serviceName,String classFunctionName) {
        doGetRequest(setSmsParam(phoneNum, content,projectName,serviceName,classFunctionName));
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 判断手机号码是否已经发送过验证码
     * @param phoneNum
     * @return
     */
    private boolean containInRedis(String phoneNum){
        if(redisRepository.get(phoneNum) != null){
            return true;
        }
        return false;
    }

    /**
     * 构造发送短信Url
     * @param phoneNum
     * @return
     */
    private String makeSmsUrl(String phoneNum, Integer saveTime,String projectName,String serviceName,String classFunctionName){
        //目标号码，必填参数
        String da = "86"+phoneNum;
        //下行内容以及编码格式，必填参数
        int dc = 15;
        String code = "" + (int)(Math.random()*9000+1000); //
        String content = "短信验证码为:" + code +",有效期为一天,请勿删短信!";
        String sm = encodeHexStr(dc,content);//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式
        saveMessageLog(phoneNum,content,projectName,serviceName,classFunctionName);
        //组成url字符串
        String smsUrl = etonenetParam.getMtUrl() + "?command=" + etonenetParam.getCommand() + "&spid=" + etonenetParam.getSpid()
                + "&sppassword=" + etonenetParam.getSppassword() + "&spsc=" + etonenetParam.getSpsc() + "&sa=" + etonenetParam.getSa()
                + "&da=" + da + "&sm=" + sm + "&dc=" + dc;
        redisRepository.save(phoneNum, code, saveTime);
        return smsUrl;
    }

    private String setSmsParam(String phoneNum, String content,String projectName,String serviceName,String classFunctionName){
        //目标号码，必填参数
        String da = "86"+phoneNum;
        //下行内容以及编码格式，必填参数
        int dc = 15;
        saveMessageLog(phoneNum,content,projectName,serviceName,classFunctionName);
        String sm = encodeHexStr(dc, content);//下行内容进行Hex编码，此处dc设为15，即使用GBK编码格式
        //组成url字符串
        String smsUrl = etonenetParam.getMtUrl() + "?command=" + etonenetParam.getCommand() + "&spid=" + etonenetParam.getSpid()
                + "&sppassword=" + etonenetParam.getSppassword() + "&spsc=" + etonenetParam.getSpsc() + "&sa=" + etonenetParam.getSa()
                + "&da=" + da + "&sm=" + sm + "&dc=" + dc;
        return smsUrl;
    }

    //短信内容编码
    private String encodeHexStr(int dataCoding, String realStr) {

        String hexStr = null;

        if (realStr != null) {
            byte[] data = null;
            try {
                if (dataCoding == 15) {
                    data = realStr.getBytes("GBK");
                } else if ((dataCoding & 0x0C) == 0x08) {
                    data = realStr.getBytes("UnicodeBigUnmarked");
                } else {
                    data = realStr.getBytes("ISO8859-1");
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("短信内容编码error",e);
                System.out.println(e.toString());
            }

            if (data != null) {
                int len = data.length;
                char[] out = new char[len << 1];
                // two characters form the hex value.
                for (int i = 0, j = 0; i < len; i++) {
                    out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
                    out[j++] = DIGITS[0x0F & data[i]];
                }
                hexStr = new String(out);
            }
        }
        return hexStr;
    }

    /**
     * 发送http GET请求，并返回http响应字符串
     *
     * @param urlstr 完整的请求url字符串
     * @return
     */
    public static String doGetRequest(String urlstr) {
        String res = null;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "text/html; charset=GB2312");
            System.setProperty("sun.net.client.defaultConnectTimeout", "5000");//jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", "10000"); //jdk1.4换成这个,读操作超时
            //httpConn.setConnectTimeout(5000);//jdk 1.5换成这个,连接超时
            //httpConn.setReadTimeout(10000);//jdk 1.5换成这个,读操作超时
            httpConn.setDoInput(true);
            int rescode = httpConn.getResponseCode();
            if (rescode == 200) {
                BufferedReader bfw = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                res = bfw.readLine();
            } else {
                res = "Http request error code :" + rescode;
            }
        } catch (Exception e) {
            logger.error("发送http GET请求，并返回http响应字符串error",e);
            System.out.println(e.toString());
        }
        return res;
    }

    /**
     * 将 短信下行 请求响应字符串解析到一个HashMap中
     * @param resStr
     * @return
     */
    public static HashMap parseResStr(String resStr) {
        HashMap pp = new HashMap();
        try {
            String[] ps = resStr.split("&");
            for (int i = 0; i < ps.length; i++) {
                int ix = ps[i].indexOf("=");
                if (ix != -1) {
                    pp.put(ps[i].substring(0, ix), ps[i].substring(ix + 1));
                }
            }
        } catch (Exception e) {
            logger.error("将 短信下行 请求响应字符串解析到一个HashMap中error",e);
            System.out.println(e.toString());
        }
        return pp;
    }

    /**
     * 保存发送短信log
     * @param phoneNum
     * @param content
     * @param projectName
     * @param serviceName
     * @param classFunctionName
     */
    private void saveMessageLog(String phoneNum,String content,String projectName,String serviceName,String classFunctionName){
        Date nowDate = new Date();
        MessageLog messageLog = new MessageLog();
        messageLog.setPhone(phoneNum);
        messageLog.setContent(content);
        messageLog.setProjectName(projectName);
        messageLog.setServiceName(serviceName);
        messageLog.setClassFunctionName(classFunctionName);
        messageLog.setSendTime(nowDate);
        messageLog.setCreateUser(CommonUtil.MsgCreateUser);
        messageLog.setUpdateUser(CommonUtil.MsgUpdateUser);
        messageLog.setCreateTime(nowDate);
        messageLog.setUpdateTime(nowDate);
        messageLogRepository.save(messageLog);
    }


}
