package cn.gson.oasys.common;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.wallet.DeterministicSeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import com.google.common.collect.ImmutableList;

import sun.security.provider.SecureRandom;

public class AccountUtils {

	private Logger log = LoggerFactory.getLogger("AccountUtils");
	private String keystoreDir = "D:\\keystore";
	private String usdtaddress = "0x9Dac5E53e2C6dc7AC587dfFde8fEAbB2ba00c5ac";
	private String chainUrl = "https://data-seed-prebsc-1-s1.binance.org:8545/";
	
	private static final BigDecimal WEI = new BigDecimal("1000000000000000000");

	private Web3j web3j;
	private BigInteger gNoce;
	List<String> accounts;

	public AccountUtils() throws Exception{
//		web3j = Web3j.build(new HttpService("http://localhost:7545"));
		web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.binance.org:8545/"));
    	
//		accounts = web3j.ethAccounts().send().getAccounts();
	}

	/**
	 * path路径
	 */
	private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
			ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
					ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);

	private void logAccount(String[] tuple){
		System.out.println("Private Key: " + tuple[0]);
		System.out.println("Public Key: " + tuple[1]);
		System.out.println("address: " + tuple[2]);
	}

	private String[] getAccountTuple(ECKeyPair keyPair){
		return new String[]{
				keyPair.getPrivateKey().toString(16),
				keyPair.getPublicKey().toString(16),
				"0x"+Keys.getAddress(keyPair)
		};
	}

	public String[] newAccount() throws Exception{
		ECKeyPair keyPair = Keys.createEcKeyPair();
		String[] tuple = getAccountTuple(keyPair);
		//		String address = Keys.getAddress(keyPair.getPublicKey().toString(16));
		//		System.out.println("address:"+address);
		logAccount(tuple);
		return tuple;
	}

	public String[] importPrivateKey(String privateKey) throws Exception {
		BigInteger key = new BigInteger(privateKey,16);
		ECKeyPair keyPair = ECKeyPair.create(key);
		String address = Keys.getAddress(keyPair.getPublicKey().toString(16));
		System.out.println("address:"+address);

		String[] tuple = getAccountTuple(keyPair);
		logAccount(tuple);
		return tuple;
	}


	public String newWalletFile(String password) throws Exception {
		File dest = new File(keystoreDir);
		String walletFileName = WalletUtils.generateNewWalletFile(password,dest,true);
		System.out.println("Wallet file: " + walletFileName);
		return walletFileName;
	}

	public String[] loadWalletFile(String password,String walletFileName) throws Exception {
		String src = keystoreDir + "/" + walletFileName;
		Credentials credentials = WalletUtils.loadCredentials(password,src);

		ECKeyPair keyPair = credentials.getEcKeyPair();
		String[] tuple = getAccountTuple(keyPair);
		logAccount(tuple);
		return tuple;
	}

	/**
	 * 创建钱包
	 * @throws MnemonicException.MnemonicLengthException
	 */
	public static Map<String,Object> createAccount()  throws MnemonicException.MnemonicLengthException {

		Map<String,Object> resultMap = new LinkedHashMap<String,Object>();

		SecureRandom secureRandom = new SecureRandom();
		byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
		secureRandom.engineNextBytes(entropy);

		//生成12位助记词
		List<String>  str = MnemonicCode.INSTANCE.toMnemonic(entropy);

		//使用助记词生成钱包种子
		byte[] seed = MnemonicCode.toSeed(str, "");
		DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
		DeterministicKey deterministicKey = deterministicHierarchy
				.deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
		byte[] bytes = deterministicKey.getPrivKeyBytes();
		ECKeyPair keyPair = ECKeyPair.create(bytes);
		//通过公钥生成钱包地址
		String address = Keys.getAddress(keyPair.getPublicKey());

		System.out.println();
		System.out.println("助记词：");
		System.out.println(String.join(" ",str));
		System.out.println();
		System.out.println("地址：");
		System.out.println("0x"+address);
		System.out.println();
		System.out.println("私钥：");
		System.out.println("0x"+keyPair.getPrivateKey().toString(16));
		System.out.println();
		System.out.println("公钥：");
		System.out.println(keyPair.getPublicKey().toString(16));

		resultMap.put("mnemonic", String.join(" ",str));
		resultMap.put("privateKey", "0x"+keyPair.getPrivateKey().toString(16));
		resultMap.put("publicKey", keyPair.getPublicKey().toString(16));
		resultMap.put("address", "0x"+address);

		return resultMap;
	}

	/**
	 * 根据助记词导入
	 * @throws MnemonicException.MnemonicLengthException
	 */
	public Map<String,Object> importByMnemonic(String mnemonic)  throws MnemonicException.MnemonicLengthException {

		Map<String,Object> resultMap = new LinkedHashMap<String,Object>();

		List<String> mnemonicList = Arrays.asList(mnemonic.split(" "));

		//使用助记词生成钱包种子
		byte[] seed = MnemonicCode.toSeed(mnemonicList, "");

		DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
		DeterministicKey deterministicKey = deterministicHierarchy
				.deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
		byte[] bytes = deterministicKey.getPrivKeyBytes();
		ECKeyPair keyPair = ECKeyPair.create(bytes);
		//通过公钥生成钱包地址
		String address = Keys.getAddress(keyPair.getPublicKey());

		System.out.println();
		System.out.println("助记词：");
		System.out.println(mnemonic);
		System.out.println();
		System.out.println("地址：");
		System.out.println("0x"+address);
		System.out.println();
		System.out.println("私钥：");
		System.out.println("0x"+keyPair.getPrivateKey().toString(16));
		System.out.println();
		System.out.println("公钥：");
		System.out.println(keyPair.getPublicKey().toString(16));

		resultMap.put("mnemonic", mnemonic);
		resultMap.put("privateKey", "0x"+keyPair.getPrivateKey().toString(16));
		resultMap.put("publicKey", keyPair.getPublicKey().toString(16));
		resultMap.put("address", "0x"+address);

		return resultMap;
	}

	public void convertUnit() {
		BigDecimal oneEther = Convert.toWei("1",Convert.Unit.ETHER);
		System.out.println("1 ether = " + oneEther + " wei");
		BigDecimal oneWei = Convert.fromWei("1",Convert.Unit.ETHER);
		System.out.println("1 wei = " + oneWei + " ether");
	}

	public String[] getNodeAccounts() throws Exception {
		//		Web3j web3j = Web3j.build(new HttpService("http://localhost:7545"));
		List<String> accounts = web3j.ethAccounts().send().getAccounts();
		logAccounts(accounts);
		return accounts.toArray(new String[accounts.size()]);
	}

	private void logAccounts(List<String> accounts){
		for(int i=0;i<accounts.size();i++){
			System.out.println("account " + i + ": " + accounts.get(i));
		}
	}

	public void getNodeAccountBalance(int idx) throws Exception {
		String account = web3j.ethAccounts().send().getAccounts().get(idx);
		DefaultBlockParameter block = DefaultBlockParameterName.LATEST;
		//DefaultBlockParameter block = new DefaultBlockParameterNumber(0);
		BigInteger balance = web3j.ethGetBalance(account,block).send().getBalance();
		System.out.println("balance " + idx + " @" + block.getValue() + ": " + balance );
	}

	public void getNodeAccountBalance1(int idx) throws Exception {
		String account = web3j.ethAccounts().send().getAccounts().get(idx);
		//		DefaultBlockParameter block = DefaultBlockParameterName.LATEST;
		DefaultBlockParameter block = new DefaultBlockParameterNumber(0);
		BigInteger balance = web3j.ethGetBalance(account,block).send().getBalance();
		System.out.println("balance " + idx + " @" + block.getValue() + ": " + balance );
	}

	public String transactionOnly(int fromIndex,int toIndex,int amount) throws Exception {
		String from = accounts.get(fromIndex);
		String to = accounts.get(toIndex);
		BigInteger value = BigInteger.valueOf(amount);
		BigInteger gasPrice = null;
		BigInteger gasLimit = null;
		//	    DefaultBlockParameter block = DefaultBlockParameterName.LATEST;
		String data = "null";
		BigInteger nonce = null;
		Transaction tx = new Transaction(from,nonce,gasPrice,gasLimit,to,value,data);
		String txHash = web3j.ethSendTransaction(tx).send().getTransactionHash();
		System.out.println("tx hash: " + txHash);
		return txHash;
	}

	public void transactionWithReceipt(int fromIndex,int toIndex,int amount) throws Exception {
		String txHash = transactionOnly(fromIndex,toIndex,amount);  
		TransactionReceipt receipt = waitForTransactionReceipt(txHash,  2*1000);
		System.out.println("tx receipt =>");
		System.out.println("tx hash: " + receipt.getTransactionHash());
		System.out.println("tx index: " + receipt.getTransactionIndex());
		System.out.println("block hash: " + receipt.getBlockHash());
		System.out.println("block number: " + receipt.getBlockNumber());
		System.out.println("cumulativeGasUsed: " + receipt.getCumulativeGasUsed());
		System.out.println("gas used: " + receipt.getGasUsed());
		System.out.println("contractAddress: " + receipt.getContractAddress());
		System.out.println("root: " + receipt.getRoot());
		System.out.println("status: " + receipt.getStatus());
		System.out.println("from: " + receipt.getFrom());
		System.out.println("to: " + receipt.getTo());
		System.out.println("logs: " + receipt.getLogs());
		System.out.println("logsBloom: " + receipt.getLogsBloom());
	}

	private TransactionReceipt waitForTransactionReceipt(String txHash,long timeout) throws Exception {
		System.out.println("wait for receipt...");
		long t0 = System.currentTimeMillis();
		Optional<TransactionReceipt> receipt = null;
		while(true){
			receipt = web3j.ethGetTransactionReceipt(txHash).send().getTransactionReceipt();
			if(receipt.isPresent()) {
				System.out.println("got receipt");
				TransactionReceipt receipts = receipt.get();
				System.out.println("tx hash: " + receipts.getTransactionHash());
				System.out.println("tx index: " + receipts.getTransactionIndex());
				System.out.println("block hash: " + receipts.getBlockHash());
				System.out.println("block number: " + receipts.getBlockNumber());
				System.out.println("cumulativeGasUsed: " + receipts.getCumulativeGasUsed());
				System.out.println("gas used: " + receipts.getGasUsed());
				System.out.println("contractAddress: " + receipts.getContractAddress());
				System.out.println("root: " + receipts.getRoot());
				System.out.println("status: " + receipts.getStatus());
				System.out.println("from: " + receipts.getFrom());
				System.out.println("to: " + receipts.getTo());
				System.out.println("logs: " + receipts.getLogs());
				System.out.println("logsBloom: " + receipts.getLogsBloom());
				
				return receipts;
			}
			long t1 = System.currentTimeMillis();
			if((t1-t0) > timeout) {
				System.out.println("time out");
				return null;
			}
			Thread.sleep(100);
		}
	}

	public void transferMoney(int fromIndex,int toIndex,int amount) throws Exception {
		web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
		List<String> accounts = web3j.ethAccounts().send().getAccounts();
		String from = accounts.get(fromIndex);  
		String to = accounts.get(toIndex);
		BigDecimal value = BigDecimal.valueOf(amount);
		ClientTransactionManager ctm = new ClientTransactionManager(web3j,from);
		Transfer transfer = new Transfer(web3j,ctm);
		System.out.println("transfer...");
		TransactionReceipt receipt = transfer.sendFunds(to,value,Convert.Unit.WEI).send();
		System.out.println("receipt => " + receipt);
	}

	public void transferMoneyRaw(int toIndex,int amount) throws Exception {
		web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
		List<String> accounts = web3j.ethAccounts().send().getAccounts();
		Credentials credentials = WalletUtils.loadCredentials("123","./keystore/UTC--2018-06-18T08-33-26.840986000Z--7d06fc6d803ccd43b90429c2fcfb37f33bbd3309.json");
		String to = accounts.get(toIndex);
		BigDecimal value = BigDecimal.valueOf(amount);
		RawTransactionManager ctm = new RawTransactionManager(web3j,credentials);
		Transfer transfer = new Transfer(web3j,ctm);
		System.out.println("raw transfer...");
		TransactionReceipt receipt = transfer.sendFunds(to,value,Convert.Unit.WEI).send();
		System.out.println("receipt => " + receipt);
	}

//	public void transfer(String privateKey,BigInteger values) throws IOException, CipherException, ExecutionException, InterruptedException {
//		Web3j web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.binance.org:8545/"));
////		Credentials credentials = WalletUtils.loadCredentials("交易密码", "创建钱包地址时的json文件地址");
//		
//		//加载转账所需的凭证，用私钥
//        Credentials credentials = Credentials.create(privateKey);
//        String fromAddress = credentials.getAddress();
//        
//		EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
//				fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
//		BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//
//		//转账的目标地址
//		Address address = new Address("0x6222127b0660E1228776c3e59c77Af934C48423b");
//		Uint256 value = new Uint256(values);
//		List<Type> parametersList = new ArrayList<>();
//		parametersList.add(address);
//		parametersList.add(value);
//		List<TypeReference<?>> outList = new ArrayList<>();
//		Function function = new Function("transfer", parametersList, outList);
//		String encodedFunction = FunctionEncoder.encode(function);
//		System.out.println( DefaultGasProvider.GAS_PRICE);
//		System.out.println(DefaultGasProvider.GAS_LIMIT);
//		RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, DefaultGasProvider.GAS_PRICE,
//				new BigInteger("60000"), usdtaddress, encodedFunction);
//		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//		String hexValue = Numeric.toHexString(signedMessage);
//		EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
//		//这里可以判断交易的Hash是否为空来判断交易是否成功，具体可以调试代码查看
//		Object transactionHash = ethSendTransaction.getTransactionHash();
//		String result = ethSendTransaction.getResult();
//		System.out.println(transactionHash.toString());
//		System.out.println("result"+result);
//	}

	/**
	 * 新建钱包文件keyfile
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidAlgorithmParameterException
	 * @throws CipherException
	 * @throws IOException
	 */
	private static void creatAccount() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
		String walletFileName0="";//文件名
		String walletFilePath0="C:/temp/";
		//钱包文件保持路径，请替换位自己的某文件夹路径

		walletFileName0 = WalletUtils.generateNewWalletFile("123456", new File(walletFilePath0), false);
		System.out.println("walletName: "+walletFileName0);
	}

	/**
	 * 加载钱包文件
	 * @throws IOException
	 * @throws CipherException
	 */
	public static void loadWallet() throws IOException, CipherException {
		String walleFilePath="C:/temp/UTC--2019-03-16T14-46-47.160000000Z--bbdd7c6b983f05f451e83e177c59894f076d008a.json";
		String passWord="123456";
		Credentials credentials = WalletUtils.loadCredentials(passWord, walleFilePath);
		String address = credentials.getAddress();
		BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
		BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();

		System.out.println("address="+address);
		System.out.println("public key="+publicKey);
		System.out.println("private key="+privateKey);

	}

	/**
	 * 连接以太坊客户端
	 * @throws IOException
	 */
	public static void conectETHclient() throws IOException {
		//连接方式1：使用infura 提供的客户端
		Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/zmd7VgRt9go0x6qlJ2Mk"));// TODO: 2018/4/10 token更改为自己的
		//连接方式2：使用本地客户端
		//web3j = Web3j.build(new HttpService("127.0.0.1:7545"));
		//测试是否连接成功
		String web3ClientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
		System.out.println("version=" + web3ClientVersion);
	}

	/**
	 * 查询账户余额
	 * @throws IOException
	 */
	public String getBlanceOf(String address) throws IOException {
//		Web3j web3j = Web3j.build(new HttpService(mainHttp));// TODO: 2018/4/10 token更改为自己的
		if (web3j == null) {
			return null;
		}
		//第二个参数：区块的参数，建议选最新区块
		EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
		//格式转化 wei-ether
//		String blanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");
		String blanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString();
		
		System.out.println(blanceETH);
		return blanceETH;
	}


	/**
	 * 交易ETH
	 */
	public void transto(String to ,BigDecimal values,String privateKey) throws Exception {

		//加载转账所需的凭证，用私钥
        Credentials credentials = Credentials.create(privateKey);
        
		if (web3j == null) return;
		if (credentials == null) return;
		//开始发送0.01 =eth到指定地址
		//String address_to = "0x6222127b0660E1228776c3e59c77Af934C48423b";
		TransactionReceipt send = Transfer.sendFunds(web3j, credentials, to, values, Convert.Unit.ETHER).send();

		System.out.println("Transaction complete:");
		System.out.println("trans hash=" + send.getTransactionHash());
		System.out.println("from :" + send.getFrom());
		System.out.println("to:" + send.getTo());
		System.out.println("gas used=" + send.getGasUsed());
		System.out.println("status: " + send.getStatus());
	}
	
	/**
     * ERC-20Token交易
     *
     * @param from
     * @param to
     * @param value
     * @param privateKey
     * @param data       附加信息需要转换成16进制数
     * @return
     * @throws Exception
     */
    public String transferERC20Token(String from,
                                                 String to,
                                                 BigInteger value,
                                                 String privateKey,
                                                 String contractAddress) throws Exception {
    	//加载转账所需的凭证，用私钥
        Credentials credentials = Credentials.create(privateKey);
        //获取nonce，交易笔数
        BigInteger nonce = getNonce(from);
        if(gNoce == null) {
        	gNoce = nonce;
        }
        System.out.println("gNoce:"+gNoce);    
        //get gasPrice
        BigInteger gasPrice = requestCurrentGasPrice();
        BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;

        synchronized(AccountUtils.class) {
        	//创建RawTransaction交易对象
            Function function = new Function(
                    "transfer",
                    Arrays.asList(new Address(to), new Uint256(value)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));

            String encodedFunction = FunctionEncoder.encode(function);

            RawTransaction rawTransaction = RawTransaction.createTransaction(gNoce,
                    gasPrice,
                    gasLimit,
                    contractAddress, encodedFunction);
            
            gNoce = gNoce.add(new BigInteger("1"));
            
            //签名Transaction，这里要对交易做签名
            byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signMessage);
            //发送交易
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            
            System.out.println("transactionHash:"+ethSendTransaction.getTransactionHash());
            
            //waitForTransactionReceipt(ethSendTransaction.getTransactionHash(),  300000L);
            
            return ethSendTransaction.getTransactionHash();
        }
    }
    
    /**
     * ERC-20Token交易
     *
     * @param from
     * @param to
     * @param value
     * @param privateKey
     * @param gasPrice 
     * @param data       附加信息需要转换成16进制数
     * @return
     * @throws Exception
     */
    public String transferERC20Token(String from,
                                                 String to,
                                                 BigInteger value,
                                                 String privateKey,
                                                 String contractAddress,
                                                 BigInteger nonce, String gasPrice) throws Exception {
    	//加载转账所需的凭证，用私钥
        Credentials credentials = Credentials.create(privateKey);
        
        //get gasPrice
        BigInteger gPrice = installGasPrice(gasPrice);
//        BigInteger gPrice1 = requestCurrentGasPrice();
        
        BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;

        synchronized(AccountUtils.class) {
        	//创建RawTransaction交易对象
            Function function = new Function(
                    "transfer",
                    Arrays.asList(new Address(to), new Uint256(value)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));

            String encodedFunction = FunctionEncoder.encode(function);

            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce,
            		gPrice,
                    gasLimit,
                    contractAddress, encodedFunction);
            
            //签名Transaction，这里要对交易做签名
            byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signMessage);
            //发送交易
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            
            System.out.println("transactionHash:"+ethSendTransaction.getTransactionHash());
            
            return ethSendTransaction.getTransactionHash();
        }
    }
    
	/**
     * 获取nonce，交易笔数
     *
     * @param from
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public BigInteger getNonce(String from) throws Exception {
    	//    	Web3j web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.binance.org:8545/"));

    	EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
    	BigInteger nonce = transactionCount.getTransactionCount();

    	//System.out.println("transfer nonce : " + nonce);
    	return nonce;
    }
    
    /**
     * 获取当前以太坊网络中最近一笔交易的gasPrice
     */
    public BigInteger requestCurrentGasPrice() throws Exception {
        EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
        return ethGasPrice.getGasPrice();
    }
    
    public List<String> getGasPriceAndLimit() throws Exception {
    	List<String> gasList = new ArrayList<String>();
    	EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
    	BigInteger gasPrice = ethGasPrice.getGasPrice();
    	BigInteger gasVlue = new BigInteger("21000");
    	
    	BigInteger gas = gasPrice.multiply(gasVlue);
    	String blanceETH = Convert.fromWei(gas.toString(), Convert.Unit.ETHER).toPlainString();
		
    	gasList.add(blanceETH);
    	return gasList;
    }
    
    /**
     * 获取ERC-20 token指定地址余额
     *
     * @param address         查询地址
     * @param contractAddress 合约地址
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String getERC20Balance(String address, String contractAddress) throws ExecutionException, InterruptedException {
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address fromAddress = new Address(address);
        inputParameters.add(fromAddress);
 
        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(address, contractAddress, data);
 
        EthCall ethCall;
        BigDecimal balanceValue = BigDecimal.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//            Integer value = 0;
            BigInteger value = new BigInteger("0");
            if(results != null && results.size()>0){
                value = new BigInteger(String.valueOf(results.get(0).getValue()));
            }
            balanceValue = new BigDecimal(value).divide(WEI, 3, RoundingMode.HALF_DOWN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return balanceValue.toString();
    }
    
    public BigInteger installGasPrice(String gasPrice) {
    	BigInteger price = Convert.toWei(gasPrice,Convert.Unit.ETHER).toBigInteger();
    	BigInteger gasVlue = new BigInteger("21000");
    	
		return price.divide(gasVlue);
	}
    
    /**具体方法
    */
	/*
	 * public static Map<String, String> createAddress() { ECKey eCkey = new
	 * ECKey(random); String privateKey =
	 * ByteArray.toHexString(eCkey.getPrivKeyBytes()); byte[] addressBytes =
	 * eCkey.getAddress(); String hexAddress = ByteArray.toHexString(addressBytes);
	 * Map<String, String> addressInfo = new HashMap<>(3);
	 * addressInfo.put("address", toViewAddress(hexAddress));
	 * addressInfo.put("hexAddress", hexAddress); addressInfo.put("privateKey",
	 * privateKey); return addressInfo; }
	 */
}
