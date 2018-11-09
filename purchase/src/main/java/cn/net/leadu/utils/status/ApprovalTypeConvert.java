package cn.net.leadu.utils.status;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengchao on 2017/6/1.
 */
public class ApprovalTypeConvert {
    public String typeConvert(String status){
        Map<String, String> map = new HashMap<>();
        map.put("000", "1000");
        map.put("0001", "100");
        map.put("0002", "1100");
        map.put("0003", "300");
        return map.get(status);
    }
}
