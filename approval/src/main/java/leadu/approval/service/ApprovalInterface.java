package leadu.approval.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by LEO on 17/5/16.
 */
@FeignClient(name = "approval", url = "${request.coreServerUrl}")
public interface ApprovalInterface {
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String coreServer(@RequestParam(value = ".url", required = false) String url,
                      @RequestParam(value = "param", required = false) String param);
}