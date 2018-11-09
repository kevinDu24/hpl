package cn.net.leadu.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 17/5/16.
 */
@FeignClient(name = "approvalApply", url = "${request.coreApplyServerUrl}")
public interface ApprovalApplyInterface {
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String coreServer(@RequestParam(value = ".url", required = false) String url,
                      @RequestParam(value = "applyFromDto", required = false) String param);
}