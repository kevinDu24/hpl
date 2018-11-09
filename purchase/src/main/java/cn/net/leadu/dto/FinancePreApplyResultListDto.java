package cn.net.leadu.dto;

import cn.net.leadu.dto.result.Res;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by pengchao on 2017/5/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancePreApplyResultListDto {
    private Res result;
    private FinancePreApplyResultDto applyInfo;
    private List<Map> lr;
}
