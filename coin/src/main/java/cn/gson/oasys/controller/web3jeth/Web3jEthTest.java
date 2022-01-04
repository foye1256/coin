package cn.gson.oasys.controller.web3jeth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import cn.gson.oasys.common.AccountUtils;
import cn.gson.oasys.common.DateTimeUtil;
import cn.gson.oasys.common.ERC20Token;
import cn.gson.oasys.common.PancakeFactory;
import cn.gson.oasys.common.PancakeRouter;
import cn.gson.oasys.common.TRC20AccountUtils;
import cn.gson.oasys.common.TRC20Utils;
import cn.gson.oasys.common.Usdt;

public class Web3jEthTest {
	public static void main(String[] args) throws Exception {
		//		new Web3jEth().run();

//		AccountUtils AU = new AccountUtils();
		//创建密钥对
		//		String[] tuple = AU.newAccount();

		//导入私钥
		//metaMask
		//	    String privateKey = "cad62962febe17804ed2b78acf9e29803d7ab9f886e2b5a6e4011111c396a1d2";
//		String privateKey = "f6ab9b30b32c0c8fa0759faff06aed499f7179cbd5aad6e02b9c3fd53fa0ae3f";
		//	    String privateKey = "133be114715e5fe528a1b8adf36792160601a2d63ab59d1fd454275b31328791";
//		AU.importPrivateKey(privateKey);

		//创建钱包文件
//		String walletFileName = AU.newWalletFile("zaq1xsw2qwerasdf");
		
		//载入钱包文件，创建账户凭证
//		AU.loadWalletFile("zaq1xsw2qwerasdf","UTC--2021-10-20T03-10-33.803000000Z--02cfa43d6dbb381ec69a1b7825c65bc1edd7a33e.json");
		
		//创建钱包
//		AU.createAccount();
		
//		String mnemonic = "drama worth announce ticket abandon dragon verify wolf review august reopen dry";
//		String mnemonic = "output route album degree around fit tip panda palace dust candy slogan jungle track glue strong random solid layer gold acid blouse siege squirrel";
//		AU.importByMnemonic(mnemonic);
		
		//查看节点账户
//		AU.getNodeAccounts();
		
		//查看节点管理账户第一个的当前余额。
//		AU.getNodeAccountBalance(0);
		//查看节点的第6个账户在初始时刻的余额
//		AU.getNodeAccountBalance1(5);
		
//		AU.convertUnit();
		
//		AU.transactionOnly(2, 4, 100);
		//等待交易收据
//		AU.transactionWithReceipt(0,1,100);
		
		//使用Transfer类转账
//		AU.transferMoney(3,4,100);
	    //使用裸交易管理器
//		AU.transferMoneyRaw(6,100);
		String from = "0x2dDaAC942c07903a52cc590166d1b9a59055bcD6";
		String privateKey = "67275d25fd82ee3800100156cd151efa038233010fa92f587d054e89cd90dc01";
		BigInteger values = new BigInteger("100000000000000000000");
		
		String to = "0x6222127b0660E1228776c3e59c77Af934C48423b";
		String contractAddress = "0x9Dac5E53e2C6dc7AC587dfFde8fEAbB2ba00c5ac";
		
		
//		BigDecimal values = new BigDecimal("0.01");
		
		String address_to = "0x6222127b0660E1228776c3e59c77Af934C48423b";
//		AU.transfer(privateKey,values);
		
//		AU.transto(to,values,privateKey);
		
//		AU.getBlanceOf(to);
		
//		AU.transferERC20Token(from, to, values, privateKey, contractAddress);
		
//		String code = AU.getERC20Balance(to,contractAddress);
//		
//        System.out.printf("查询出来的余额："+code);

//		ExecutorService executor =   
//	                Executors.newFixedThreadPool(1); //使用线程池 
//		FutureTask<String> futureTask =   
//                new FutureTask<String>(new ERC20Token(from,to,values,privateKey,contractAddress));  
        
        //执行FutureTask，相当于上例中的client.request("name")发送请求  
//        executor.submit(futureTask);  

        //在处理这些业务逻辑过程中，ERC20Token也正在创建，从而充分了利用等待时间  
//        Thread.sleep(2000);  
        //使用真实数据  
        //如果call()没有执行完成依然会等待  
//        System.out.println("数据=" + futureTask.get()); 
//        executor.shutdown();
		
//		AU.getGasPriceAndLimit();
		
//		for(int i = 1;i< 10;i++) {
//			TRC20Utils trc = new TRC20Utils();
//			
//			Map<String, Object> createTRC20Address = trc.createTRC20Address();
//			System.out.println("address:"+createTRC20Address.toString());
//		}
		
		//创建web3j对象
		Web3j web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.binance.org:8545/"));
		
		//获取当前gas价格：
		BigInteger gasPrice = web3j.ethGasPrice().sendAsync().get().getGasPrice();
		
		//加载转账所需的凭证，用私钥
        Credentials credentials = Credentials.create(privateKey);
        
        String tokenAddress1 = "0x9Dac5E53e2C6dc7AC587dfFde8fEAbB2ba00c5ac";
        String tokenAddress2 = "0x2d036DEb76D4AE54778603cDbA8D32E657C556cE";
//        Usdt usdt = Usdt.load(usdtAddress, web3j, credentials, gasPrice,BigInteger.valueOf(3000000));
//		
//        RemoteCall<TransactionReceipt> approve = usdt.approve(from, values);
//        TransactionReceipt transactionReceipt = approve.sendAsync().get();
//        System.out.println("TransactionHash:"+transactionReceipt.getTransactionHash());
        
        String factoryAddress = "0xcA143Ce32Fe78f1f7019d7d551a6402fC5350c73";
        PancakeFactory pancakeFactory = PancakeFactory.load(factoryAddress, web3j, credentials, gasPrice, BigInteger.valueOf(3000000));
//        RemoteCall<String> pair = pancakeFactory.getPair(usdtAddress, factoryAddress);
        
        
        String routerAddress = "0x10ED43C718714eb63d5aA57B78B54704E256024E";
        PancakeRouter router = PancakeRouter.load(routerAddress, web3j, credentials, gasPrice, BigInteger.valueOf(3000000));
        
        String stringStamp = "2021-12-22 00:00:00";
        String dateTimestamp = DateTimeUtil.getDateTimestamp(stringStamp);
        BigInteger deadline = new BigInteger(dateTimestamp);
        List<String> path = new ArrayList<String>();
        path.add(tokenAddress1);
        path.add(tokenAddress2);
        RemoteCall<TransactionReceipt> swapETHForExactTokens = router.swapETHForExactTokens(values, path, from, deadline);
//        TransactionReceipt transactionReceipt2 = router.factory().sendAsync().get();
//        System.out.println("transactionReceipt2:"+transactionReceipt2.getTransactionHash());
        
//        router.getAmountOut(gasPrice, values, gasPrice);
//        router.addLiquidity(to, to, gasPrice, gasPrice, gasPrice, values, address_to, gasPrice);
        
	}

}
