package cn.gson.oasys.feign;

import com.alibaba.fastjson.JSONObject;
import cn.gson.oasys.feign.dt.GetTransactionSign;
import cn.gson.oasys.feign.dt.TriggerSmartContract;
import cn.gson.oasys.feign.dt.EasyTransferByPrivate;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

//@FeignClient(url = "{tron.trc20.url}", name = "tron-node", configuration = {JacksonEncoder.class, JacksonDecoder.class})
@FeignClient(url = "https://api.nileex.io", name = "tron-node", configuration = {JacksonEncoder.class, JacksonDecoder.class})
public interface TronFullNodeFeign {

    /**
     * 只能合约调用接口
     *
     * @param param
     * @return
     */
    @PostMapping("/wallet/triggersmartcontract")
    TriggerSmartContract.Result triggerSmartContract(@RequestBody TriggerSmartContract.Param param);


    /**
     * 使用私钥签名交易.（存在安全风险，trongrid已经关闭此接口服务，请使用离线方式或者自己部署的节点）
     *
     * @param param
     * @return
     */
    @PostMapping("/wallet/gettransactionsign")
    JSONObject getTransactionSign(@RequestBody GetTransactionSign.Param param);


    /**
     * 广播签名后的交易.
     *
     * @param rawBody
     * @return
     */
    @PostMapping("/wallet/broadcasttransaction")
    JSONObject broadcastTransaction(@RequestBody Object rawBody);



    @PostMapping("/wallet/easytransferbyprivate")
    JSONObject easyTransferByPrivate(@RequestBody EasyTransferByPrivate.Param param);


    /**
     * 创建地址
     *
     * @return
     */
    @PostMapping("/wallet/generateaddress")
    JSONObject generateAddress();

    /**
     * 获取账号信息
     *
     * @param param
     * @return
     */
    @PostMapping("/wallet/getaccount")
    JSONObject getAccount(@RequestBody Map<String, Object> param);
}
