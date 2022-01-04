package cn.gson.oasys.common;
import java.math.BigInteger;
import java.util.concurrent.Callable;

import org.web3j.protocol.core.methods.response.EthSendTransaction;

public class ERC20Token implements Callable<String> {

	
    private String from;
	private String to;
	private BigInteger value;
	private String privateKey;
	private String contractAddress;
	private BigInteger nonce;
	private String gasPrice;

    public ERC20Token(String from,
            String to,
            BigInteger value,
            String privateKey,
            String contractAddress,
            BigInteger nonce,
            String gasPrice) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.privateKey = privateKey;
        this.contractAddress = contractAddress;
        this.nonce = nonce;
        this.gasPrice = gasPrice;
    }

    @Override
    public String call() throws Exception {
    	
    	AccountUtils au = new AccountUtils();
    	
    	String transactionHash = au.transferERC20Token(from, to, value, privateKey, contractAddress,nonce,gasPrice);
        
    	return transactionHash;
    }
}