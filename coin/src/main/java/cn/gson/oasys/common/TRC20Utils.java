package cn.gson.oasys.common;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.google.common.collect.ImmutableList;
//import com.google.protobuf.Any;
//
//import org.apache.commons.codec.binary.Hex;
//import org.apache.commons.lang3.StringUtils;
//import org.bitcoinj.crypto.ChildNumber;
//import org.bitcoinj.crypto.DeterministicHierarchy;
//import org.bitcoinj.crypto.DeterministicKey;
//import org.bitcoinj.crypto.HDKeyDerivation;
//import org.bitcoinj.crypto.MnemonicCode;
//import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException;
//import org.bitcoinj.wallet.DeterministicSeed;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.http.*;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import org.tron.api.GrpcAPI;
//import org.tron.common.crypto.ECKey;
//import org.tron.common.crypto.SignInterface;
//import org.tron.common.crypto.SignUtils;
//import org.tron.common.utils.ByteArray;
//import org.tron.common.utils.Utils;
//import org.tron.protos.Protocol;
//import org.tron.protos.contract.BalanceContract;
//import org.tron.protos.contract.SmartContractOuterClass;
//import org.tron.walletserver.WalletApi;
//import org.web3j.crypto.ECKeyPair;
//import org.web3j.crypto.Keys;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;

import sun.security.provider.SecureRandom;
import java.util.*;

public class TRC20Utils {
//	 private static String privateKey;
//
//	 private static String trxAddress;
//
//	 private static String http;
//
//	 private static String walletSolidityHttp;
//
//	 private static String privateHttp;
//
//	 private static Long blockNum;
//
//	 private static Long blockDeep;
//
//	 private static Long fee;
//
//	 private static Map<String, String> symbolMap;
//
//	 private static Map<String, String> contractMap;
//
//	 private static Map<String, Integer> weiMap;
//
//	 private BigInteger currentBlock = BigInteger.ZERO;
//
//	 @Autowired
//	 private Environment environment;
//	 
//	 //??????
//	 private static final String tronUrl = "https://api.trongrid.io";
//	 
//	 private static SecureRandom random = new SecureRandom();
//	 
//	 /**
//		 * path??????
//		 */
//	private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
//			ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
//					ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);
//
//	 @PostConstruct
//	 public void initService() {
//	     privateKey = "";
//	     trxAddress = "";
//	     String symbol = "LIUJH#TW2VVpjBCZVEAGVrTHftjGPFMHLLWSEV4z";
//	     String wei = "TRX#6,LIUJH#18";
//	     http = "https://api.trongrid.io";
//	     walletSolidityHttp = "";
//	     blockDeep = Long.valueOf(Objects.requireNonNull(100));
//
//	     fee = Long.valueOf(Objects.requireNonNull(1000000));
//
//	     String[] symbols = symbol.split(",");
//
//	     symbolMap = new HashMap<>();
//	     contractMap = new HashMap<>();
//
//	     for (String s : symbols) {
//	         symbolMap.put(s.split("#")[0], s.split("#")[1]);
//	         contractMap.put(s.split("#")[1], s.split("#")[0]);
//	     }
//
//	     String[] weis = wei.split(",");
//	     weiMap = new HashMap<>();
//	     for (String s : weis) {
//	         weiMap.put(s.split("#")[0], Integer.valueOf(s.split("#")[1]));
//
//	     }
//	 }
//
//	 /**
//	  * ????????????????????????
//	  **/
//	 public static String createAddress() {
////	     String url = http + "/wallet/generateaddress";
//	     SignInterface sign = SignUtils.getGeneratedRandomSign(Utils.getRandom(), true);
//	     byte[] priKey = sign.getPrivateKey();
//	     byte[] address = sign.getAddress();
//	     String priKeyStr = Hex.encodeHexString(priKey);
//	     String base58check = WalletApi.encode58Check(address);
//	     String hexString = ByteArray.toHexString(address);
//	     JSONObject jsonAddress = new JSONObject();
//	     jsonAddress.put("address", base58check);
//	     jsonAddress.put("hexAddress", hexString);
//	     jsonAddress.put("privateKey", priKeyStr);
//	     return jsonAddress.toJSONString();
//	 }
//	 
//	 /**
//	  * ????????????????????????
//	 * @throws MnemonicLengthException 
//	  **/
//	 public static Map<String,Object> createTRC20Address() throws MnemonicLengthException {
//
//		 SecureRandom secureRandom = new SecureRandom();
//		 byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
//		 secureRandom.engineNextBytes(entropy);
//
//		 //??????12????????????
//		 List<String>  str = MnemonicCode.INSTANCE.toMnemonic(entropy);
//
//		 //?????????????????????????????????
//		 byte[] seed = MnemonicCode.toSeed(str, "");
//		 DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
//		 DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
//		 DeterministicKey deterministicKey = deterministicHierarchy
//				 .deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
//		 byte[] bytes = deterministicKey.getPrivKeyBytes();
//		 ECKeyPair keyPair = ECKeyPair.create(bytes);
//		 //??????????????????????????????
//		 String hexAddress ="41" + Keys.getAddress(keyPair.getPublicKey());
//		 String privatekey = keyPair.getPrivateKey().toString(16);
//		 
//		 if(privatekey.length() < 64){
//	        for(int i = 0 ; i < 64 - privatekey.length() ; i++){
//	            privatekey = "0" + privatekey;
//	        }
//		 }
//		 String publickey = keyPair.getPublicKey().toString(16);
//		 String address = fromHexString(hexAddress);
//		 
//		 Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
//		 resultMap.put("mnemonic", String.join(" ",str));
//		 resultMap.put("privateKey", privatekey);
//		 resultMap.put("publicKey", publickey);
//		 resultMap.put("address", address);
//		 resultMap.put("hexAddress", hexAddress);
//		 
//		 return resultMap;
//	 }
//
////	 /**
////	  * ???????????????TGtiQ5uQzw3Z3ni3EC ?????? 41 ???????????????
////	  *
////	  * @param addressT
////	  * @return
////	  */
////	 public static String toHexString(String addressT) {
////	     return ByteArray.toHexString(WalletApi.decodeFromBase58Check(addressT));
////	 }
////
////	 /**
////	  * ???????????????415694e6807bf4685fb6 ?????? TGtiQ5uQzw3Z3ni3EC ???????????????
////	  *
////	  * @param addressStart41
////	  * @return
////	  */
//	 public static String fromHexString(String addressStart41) {
//	     return WalletApi.encode58Check(ByteArray.fromHexString(addressStart41));
//	 }
////	 
////	 /**
////	  * ????????????
////	  *
////	  * @param address
////	  * @return
////	  */
////	 public static String createAccount(String address) {
////	     String url = http + "/wallet/createaccount";
////	     Map<String, Object> map = new HashMap<>();
////	     map.put("owner_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(trxAddress)));
////	     map.put("account_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(address)));
////	     String param = JSON.toJSONString(map);
////	     return signAndBroadcast(postForEntity(url, param).getBody(), privateKey);
////	 }
////
////
////	 /**
////	  * ??????TRX????????????
////	  *
////	  * @param address
////	  * @return
////	  */
////	 public static String getAccount(String address) {
////	     String url = http + "/wallet/getaccount";
////	     Map<String, Object> map = new HashMap<>();
////	     map.put("address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(address)));
////	     String param = JSON.toJSONString(map);
////	     return postForEntity(url, param).getBody();
////	 }
////
////	 /**
////	  * ????????????????????????
////	  *
////	  * @param symbol  ??????
////	  * @param address ??????
////	  * @return
////	  */
////	 public static String getTrc20Account(String symbol, String address) {
////	     String url = http + "/wallet/triggerconstantcontract";
////	     Map<String, Object> map = new HashMap<>();
////	     address = TransformUtil.addZeroForNum(ByteArray.toHexString(WalletApi.decodeFromBase58Check(address)), 64);
////	     map.put("contract_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(symbolMap.get(symbol))));
////	     map.put("function_selector", "balanceOf(address)");
////	     map.put("parameter", address);
////	     map.put("owner_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(trxAddress)));
////	     String param = JSON.toJSONString(map);
////	     return postForEntity(url, param).getBody();
////	 }
////
////	 /**
////	  * trc20 ??????
////	  *
////	  * @param symbol    ????????????
////	  * @param toAddress ??????
////	  * @param amount    ??????
////	  * @return
////	  */
////	 public static String trc20Transaction(String symbol, String toAddress, BigDecimal amount) {
////	     //????????????
////	     String url = http + "/wallet/triggersmartcontract";
////
////	     Map<String, Object> map = new HashMap<>();
////
////	     String to_address = ByteArray.toHexString(WalletApi.decodeFromBase58Check(toAddress));
////	     to_address = TransformUtil.addZeroForNum(to_address, 64);
////	     amount = amount.multiply(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get(symbol))));
////	     String uint256 = TransformUtil.addZeroForNum(amount.toBigInteger().toString(16), 64);
////
////	     map.put("owner_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(trxAddress)));
////	     map.put("contract_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(symbolMap.get(symbol))));
////	     map.put("function_selector", "transfer(address,uint256)");
////	     map.put("parameter", to_address + uint256);
////	     map.put("call_value", 0);
////	     map.put("fee_limit", fee);
////
////	     String param = JSON.toJSONString(map);
////
////	     ResponseEntity<String> stringResponseEntity = postForEntity(url, param);
////
////	     return signAndBroadcast(JSON.parseObject(stringResponseEntity.getBody()).getString("transaction"), privateKey);
////	 }
////
////
////
////	 /**
////	  * trc20 ??????????????????
////	  *
////	  * @param symbol      ??????
////	  * @param fromAddress ??????
////	  * @param privateKey  ??????
////	  * @param toAddress   ??????
////	  * @param amount      ??????
////	  * @return
////	  */
////	 private static String trc20Transaction(String symbol, String fromAddress, String privateKey, String toAddress, BigDecimal amount) {
////	     //????????????
////	     String url = http + "/wallet/triggersmartcontract";
////
////	     Map<String, Object> map = new HashMap<>();
////
////	     String to_address = ByteArray.toHexString(WalletApi.decodeFromBase58Check(toAddress));
////	     to_address = TransformUtil.addZeroForNum(to_address, 64);
////	     amount = amount.multiply(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get(symbol))));
////	     String uint256 = TransformUtil.addZeroForNum(amount.toBigInteger().toString(16), 64);
////
////	     map.put("owner_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(fromAddress)));
////	     map.put("contract_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(symbolMap.get(symbol))));
////	     map.put("function_selector", "transfer(address,uint256)");
////	     map.put("parameter", to_address + uint256);
////	     map.put("call_value", 0);
////	     map.put("fee_limit", fee);
////
////	     String param = JSON.toJSONString(map);
////
////	     ResponseEntity<String> stringResponseEntity = postForEntity(url, param);
////
////	     //??????
////	     url = http + "/wallet/gettransactionsign";
////	     map = new HashMap<>();
////	     map.put("transaction", JSON.parseObject(stringResponseEntity.getBody()).get("transaction"));
////	     map.put("privateKey", privateKey);
////	     param = JSON.toJSONString(map);
////	     stringResponseEntity = postForEntity(url, param);
////
////	     //??????
////	     url = http + "/wallet/broadcasttransaction";
////	     stringResponseEntity = postForEntity(url, stringResponseEntity.getBody());
////
////	     return stringResponseEntity.getBody();
////	 }
////
////
////
////	 /**
////	  * ????????????
////	  *
////	  * @param transaction ????????????
////	  * @return
////	  */
////	 private static String signAndBroadcast(String transaction, String privateKey) {
////
////	     //??????
////	     String url = http + "/wallet/gettransactionsign";
////	     Map<String, Object> map = new HashMap<>();
////	     map.put("transaction", transaction);
////	     map.put("privateKey", privateKey);
////	     String param = JSON.toJSONString(map);
////	     ResponseEntity<String> stringResponseEntity = postForEntity(url, param);
////
////	     //??????
////	     url = http + "/wallet/broadcasttransaction";
////	     stringResponseEntity = postForEntity(url, stringResponseEntity.getBody());
////
////
////	     return stringResponseEntity.getBody();
////	 }
////
////
////	 /**
////	  * trx ??????
////	  *
////	  * @param toAddress ??????
////	  * @param amount    ??????
////	  */
////	 public static String trxTransaction(String toAddress, BigDecimal amount) {
////	     String url = http + "/wallet/easytransferbyprivate";
////	     Map<String, Object> map = new HashMap<>();
////	     map.put("privateKey", privateKey);
////	     map.put("toAddress", ByteArray.toHexString(WalletApi.decodeFromBase58Check(toAddress)));
////	     amount = amount.multiply(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get("TRX"))));
////	     map.put("amount", amount.toBigInteger());
////	     String param = JSON.toJSONString(map);
////	     return postForEntity(url, param).getBody();
////	 }
////
////	 /**
////	  * ?????? transaction ??????
////	  *
////	  * @param toAddress ??????
////	  * @param amount    ??????
////	  * @return
////	  */
////	 public static String transaction(String toAddress, BigDecimal amount) {
////	     String url = http + "/wallet/createtransaction";
////	     Map<String, Object> map = new HashMap<>();
////	     map.put("owner_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(trxAddress)));
////	     map.put("to_address", ByteArray.toHexString(WalletApi.decodeFromBase58Check(toAddress)));
////	     amount = amount.multiply(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get("TRX"))));
////	     map.put("amount", amount.toBigInteger());
////	     String param = JSON.toJSONString(map);
////	     return signAndBroadcast(postForEntity(url, param).getBody(), privateKey);
////	 }
////
////
////	 /**
////	  * https://cn.developers.tron.network/docs/%E4%BA%A4%E6%98%9311#%E4%BA%A4%E6%98%93%E7%A1%AE%E8%AE%A4%E6%96%B9%E6%B3%95
////	  * ???????????????????????????
////	  *
////	  * @param txId ??????id
////	  * @return
////	  */
////	 public static String getTransactionById(String txId) {
////	     String url = walletSolidityHttp + "/walletsolidity/gettransactionbyid";
////	     Map<String, Object> map = new HashMap<>();
////	     map.put("value", txId);
////	     String param = JSON.toJSONString(map);
////	     return postForEntity(url, param).getBody();
////	 }
////
////	 /**
////	  * ??????????????? Info ??????, ??????????????? fee ??????, ????????????, ????????? log ???.
////	  *
////	  * @param txId ??????id
////	  * @return
////	  */
////	 public static String getTransactionInfoById(String txId) {
////	     String url = http + "/wallet/gettransactioninfobyid";
////	     Map<String, Object> map = new HashMap<>();
////	     map.put("value", txId);
////	     String param = JSON.toJSONString(map);
////	     return postForEntity(url, param).getBody();
////	 }
////
////	 /**
////	  * ????????????????????????????????? Info ??????
////	  *
////	  * @param num ??????
////	  * @return
////	  */
////	 public static String getTransactionInfoByBlockNum(BigInteger num) {
////	     String url = http + "/wallet/gettransactioninfobyblocknum";
////	     Map<String, Object> map = new HashMap<>();
////	     map.put("num", num);
////	     String param = JSON.toJSONString(map);
////	     return postForEntity(url, param).getBody();
////	 }
////
////
////	 /**
////	  * ??????????????????
////	  *
////	  * @return
////	  */
////	 public static String getNowBlock() {
////	     String url = http + "/wallet/getnowblock";
////	     return getForEntity(url);
////	 }
////
////
////	 /**
////	  * ?????? post ??????
////	  *
////	  * @param url   url
////	  * @param param ????????????
////	  * @return
////	  */
////	 private static ResponseEntity<String> postForEntity(String url, String param) {
////	     RestTemplate restTemplate = new RestTemplate();
////	     HttpHeaders headers = new HttpHeaders();
////	     headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
////	     HttpEntity<String> request = new HttpEntity<>(param, headers);
////	     ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);
//////	     System.out.println("url:" + url + ",param:" + param + ",result:" + result.getBody());
////	     return result;
////	 }
////
////	 /**
////	  * ?????? get ??????
////	  *
////	  * @param url url
////	  * @return
////	  */
////	 private static String getForEntity(String url) {
////	     RestTemplate restTemplate = new RestTemplate();
////	     ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
//////	     System.out.println("url:" + url + ",result:" + result.getBody());
////	     return result.getBody();
////	 }
////
////	 public void monitorCoinListener() {
////
////	     //??????????????????????????????
////	     List<String> addressList = new ArrayList<>();
////	     addressList.add("xxxx");
////
////	     /*Long block = Long.valueOf(Objects.requireNonNull(stringRedisTemplate.opsForValue().get("tron_block")));
////	     if (currentBlock.longValue() > block) {
////	         // rpcTransactionInfo(addressList, Long.valueOf(tron_block));
////	         httpTransactionInfo(addressList, block);
////	         System.out.println(currentBlock + "===========tron_block=======" + block);
////	         stringRedisTemplate.opsForValue().set("tron_block", new BigInteger(block.toString()).add(BigInteger.ONE).toString());
////	     }*/
////
////	     int end = currentBlock.intValue();
////	     for (int i = end; i > 0; i--) {
////	         if (end - i > blockDeep) {
////	             break;
////	         }
////	         //Wallet rpc ??????
//////	         rpcTransactionInfo(addressList, (long) i);
////	         // http ??????
////	         httpTransactionInfo(addressList, (long) i);
////	     }
////	 }
////
////	 /**
////	  * ?????? txId ????????????????????????
////	  *
////	  * @param txId ??????id
////	  * @return
////	  */
////	 private boolean transactionStatus(String txId) {
////	     JSONObject parseObject = JSON.parseObject(getTransactionById(txId));
////	     if (StringUtils.isEmpty(parseObject.toJSONString())) {
////	         return false;
////	     }
////	     String contractRet = parseObject.getJSONArray("ret").getJSONObject(0).getString("contractRet");
////	     return "SUCCESS".equals(contractRet);
////	 }
////
////
////	 private void httpTransactionInfo(List<String> addressList, Long num) {
////	     String transactionInfoByBlockNum = getTransactionInfoByBlockNum(BigInteger.valueOf(num));
////	     JSONArray parseArray = JSON.parseArray(transactionInfoByBlockNum);
////	     if (parseArray.size() > 0) {
////	         parseArray.forEach(e -> {
////	             try {
////	                 String txId = JSON.parseObject(e.toString()).getString("id");
////	                 //?????? ????????? txId ??? ????????????????????????
////
////	                 JSONObject parseObject = JSON.parseObject(getTransactionById(txId));
////	                 String contractRet = parseObject.getJSONArray("ret").getJSONObject(0).getString("contractRet");
////	                 //????????????
////	                 if ("SUCCESS".equals(contractRet)) {
////	                     String type = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getString("type");
////	                     if ("TriggerSmartContract".equals(type)) {
////	                         //??????????????????
////	                         triggerSmartContract(addressList, txId, parseObject);
////
////	                     } else if ("TransferContract".equals(type)) {
////	                         //trx ??????
////	                         transferContract(parseObject);
////	                     }
////	                 }
////	             } catch (Exception exception) {
////	                 exception.printStackTrace();
////	             }
////	         });
////	     }
////	 }
////
////
////	 private void rpcTransactionInfo(List<String> addressList, Long num) {
////	     try {
////	         Optional<GrpcAPI.TransactionInfoList> optional = WalletApi.getTransactionInfoByBlockNum(num);
////	         if (!optional.isPresent()) {
////	             return;
////	         }
////
////	         List<Protocol.TransactionInfo> transactionInfoList = optional.get().getTransactionInfoList();
////	         for (Protocol.TransactionInfo transactionInfo : transactionInfoList) {
////	             String txId = ByteArray.toHexString(transactionInfo.getId().toByteArray());
////	             //?????? ????????? txId ??? ????????????????????????
////
////	             Optional<Protocol.Transaction> transaction = WalletApi.getTransactionById(txId);
////	             if (!transaction.isPresent()) {
////	                 continue;
////	             }
////
////	             List<Protocol.Transaction.Result> retList = transaction.get().getRetList();
////	             Protocol.Transaction.Result.contractResult contractRet = retList.get(0).getContractRet();
////	             if (!Protocol.Transaction.Result.contractResult.SUCCESS.name().equals(contractRet.name())) {
////	                 continue;
////	             }
////
////	             Protocol.Transaction.Contract.ContractType type = transaction.get().getRawData().getContract(0).getType();
////	             Any contractParameter = transaction.get().getRawData().getContract(0).getParameter();
////
////	             if (Protocol.Transaction.Contract.ContractType.TriggerSmartContract.name().equals(type.name())) {
////	                 // trc20 ??????
////
////	                 SmartContractOuterClass.TriggerSmartContract deployContract = contractParameter
////	                         .unpack(SmartContractOuterClass.TriggerSmartContract.class);
////
////	                 String owner_address = WalletApi.encode58Check(ByteArray.fromHexString(ByteArray.toHexString(deployContract.getOwnerAddress().toByteArray())));
////	                 String contract_address = WalletApi.encode58Check(ByteArray.fromHexString(ByteArray.toHexString(deployContract.getContractAddress().toByteArray())));
////
////	                 String dataStr = ByteArray.toHexString(deployContract.getData().toByteArray()).substring(8);
////	                 List<String> strList = TransformUtil.getStrList(dataStr, 64);
////	                 if (strList.size() != 2) {
////	                     continue;
////	                 }
////
////	                 String to_address = TransformUtil.delZeroForNum(strList.get(0));
////	                 if (!to_address.startsWith("41")) {
////	                     to_address = "41" + to_address;
////	                 }
////
////	                 to_address = WalletApi.encode58Check(ByteArray.fromHexString(to_address));
////
////	                 String amountStr = TransformUtil.delZeroForNum(strList.get(1));
////	                 if (amountStr.length() > 0) {
////	                     amountStr = new BigInteger(amountStr, 16).toString(10);
////	                 }
////
////	                 BigDecimal amount = BigDecimal.ZERO;
////	                 //????????????????????????
////	                 if (!contractMap.containsKey(contract_address)) {
////	                     continue;
////	                 }
////	                 //????????????
////	                 String symbol = contractMap.get(contract_address);
////	                 if (StringUtils.isNotEmpty(amountStr)) {
////	                     amount = new BigDecimal(amountStr).divide(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get(symbol))));
////	                 }
////	                 for (String address : addressList) {
////	                     if (address.equals(to_address)) {
////	                         System.out.println("===to_address:" + to_address + "===amount:" + amount);
////	                     }
////	                 }
////
////	             } else if (Protocol.Transaction.Contract.ContractType.TransferContract.name().equals(type.name())) {
////	                 // trx ??????
////	                 BalanceContract.TransferContract deployContract = contractParameter
////	                         .unpack(BalanceContract.TransferContract.class);
////	                 String owner_address = WalletApi.encode58Check(ByteArray.fromHexString(ByteArray.toHexString(deployContract.getOwnerAddress().toByteArray())));
////	                 String to_address = WalletApi.encode58Check(ByteArray.fromHexString(ByteArray.toHexString(deployContract.getToAddress().toByteArray())));
////	                 BigDecimal amount = new BigDecimal(deployContract.getAmount());
////	                 amount = amount.divide(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get("TRX"))));
////
////	             }
////	         }
////	     } catch (Exception e) {
////	         e.printStackTrace();
////	     }
////	 }
////
////
////	 private void transferContract(JSONObject parseObject) {
////	     //??????
////	     BigDecimal amount = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getBigDecimal("amount");
////
////	     //???????????????
////	     String owner_address = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("owner_address");
////	     owner_address = WalletApi.encode58Check(ByteArray.fromHexString(owner_address));
////
////	     //????????????
////	     String to_address = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("to_address");
////	     to_address = WalletApi.encode58Check(ByteArray.fromHexString(to_address));
////
////	     amount = amount.divide(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get("TRX"))));
////
////
////	 }
////
////	 private void triggerSmartContract(List<String> addressList, String txId, JSONObject parseObject) {
////	     //????????????
////	     String data = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("data");
////	     // ???????????????
////	     String owner_address = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("owner_address");
////	     owner_address = WalletApi.encode58Check(ByteArray.fromHexString(owner_address));
////	     // ????????????
////	     String contract_address = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value").getString("contract_address");
////	     contract_address = WalletApi.encode58Check(ByteArray.fromHexString(contract_address));
////
////	     String dataStr = data.substring(8);
////	     List<String> strList = TransformUtil.getStrList(dataStr, 64);
////
////	     if (strList.size() != 2) {
////	         return;
////	     }
////
////	     String to_address = TransformUtil.delZeroForNum(strList.get(0));
////	     if (!to_address.startsWith("41")) {
////	         to_address = "41" + to_address;
////	     }
////
////	     to_address = WalletApi.encode58Check(ByteArray.fromHexString(to_address));
////
////	     String amountStr = TransformUtil.delZeroForNum(strList.get(1));
////
////	     if (amountStr.length() > 0) {
////	         amountStr = new BigInteger(amountStr, 16).toString(10);
////	     }
////
////	     BigDecimal amount = BigDecimal.ZERO;
////	     //????????????????????????
////	     if (!contractMap.containsKey(contract_address)) {
////	         return;
////	     }
////
////	     //??????
////	     String symbol = contractMap.get(contract_address);
////	     if (StringUtils.isNotEmpty(amountStr)) {
////	         amount = new BigDecimal(amountStr).divide(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get(symbol))));
////	     }
////
////	     for (String address : addressList) {
////	         if (address.equals(to_address)) {
////	             System.out.println("===to_address:" + to_address + "===amount:" + amount);
////	         }
////
////	     }
////
////	 }
////
////
////	 /**
////	  * ??????????????????
////	  */
////	 public void blockDeepListener() {
////	     try {
////	         //??????????????????
////	         currentBlock = JSON.parseObject(getNowBlock()).getJSONObject("block_header").getJSONObject("raw_data").getBigInteger("number");
////	     } catch (Exception e) {
////	         System.out.println(e.getMessage());
////	     }
////	 }
////
////
////	 public void collectionTrc20Listener() {
////	     try {
////	         //??????????????????????????????
////	         Map<String, String> addressMap = new HashMap<>();
////	         addressMap.put("xxx", "xxxx");
////	         //??????????????????
////	         String toAddress = "xxx";
////	         String fromAddress = null;
////	         String privateKey = null;
////
////	         for (String symbol : symbolMap.keySet()) {
////	             for (String key : addressMap.keySet()) {
////	                 fromAddress = key;
////	                 privateKey = addressMap.get(key);
////	                 String trc20Account = getTrc20Account(symbol, fromAddress);
////	                 JSONObject jsonObject = JSON.parseObject(trc20Account);
////	                 String constant_result = jsonObject.getString("constant_result");
////
////	                 if (StringUtils.isEmpty(constant_result)) {
////	                     continue;
////	                 }
////
////	                 List<String> strings = JSON.parseArray(constant_result.toString(), String.class);
////
////	                 String data = strings.get(0).replaceAll("^(0+)", "");
////	                 if (data.length() == 0) {
////	                     continue;
////	                 }
////
////	                 String amountStr = new BigInteger(data, 16).toString();
////	                 BigDecimal amount = new BigDecimal(amountStr).divide(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get(symbol))));
////
////	                 if (amount.compareTo(BigDecimal.ONE) < 0) {
////	                     continue;
////	                 }
////
////	                 String account = getAccount(fromAddress);
////	                 String accountBalance = JSON.parseObject(account).getString("balance");
////	                 BigDecimal balance = BigDecimal.ZERO;
////
////	                 if (StringUtils.isNotEmpty(accountBalance)) {
////	                     balance = new BigDecimal(accountBalance).divide(new BigDecimal(1 + TransformUtil.getSeqNumByLong(0L, weiMap.get("TRX"))));
////	                 }
////
////	                 if (balance.compareTo(new BigDecimal("0.5")) < 0) {
////	                     // ???????????????
////	                     String transaction = transaction(fromAddress, new BigDecimal("0.5"));
////	                     continue;
////	                 }
////
////	                 // ?????? ??????
////	                 String transaction = trc20Transaction(symbol, fromAddress, privateKey, toAddress, amount);
////
////	             }
////	         }
////	     } catch (Exception e) {
////	         e.printStackTrace();
////	     }
////	 }
}

