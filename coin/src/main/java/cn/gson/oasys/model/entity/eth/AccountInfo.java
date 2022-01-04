package cn.gson.oasys.model.entity.eth;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import cn.gson.oasys.model.entity.user.User;

@Entity
@Table(name="aoa_account_info")
//账户信息表
public class AccountInfo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="account_id")
	private Long accountId; //逻辑主键
	
	@Column(name="type")
	private String type;//类型
	
	@Column(name="deal")
	private String deal;//协议
	
	@Column(name="status")
	private Long status;//状态（是否可用）
	
	@ManyToOne
	@JoinColumn(name="account_num_user_id")
	private User accountUserId;//用户id
	
	@Column(name="mnemonic")
	private String mnemonic;//助记词
	
	@Column(name="private_key",nullable=false)
	@NotEmpty(message="私钥不能为空")
	private String privateKey;//私钥
	
	@Column(name="public_key")
	private String publicKey;//公钥
	
	@Column(name="address",nullable=false)
	@NotEmpty(message="地址不能为空")
	private String address;//地址
	
	@Column(name="hexAddress")
	private String hexAddress;//地址
	
	@Column(name="account_file_path")
	private String accountFilePath;//文件路径
	
	@Column(name="password")
	private String password;//密码
	
	@Column(name="create_time")
	private Date createTime;//创建时间
	
	@Column(name="des")
	private String des;//账号信息备注
	
	@Column(name="balance_value")
	private String balanceValue;//eth 数量
	
	@Column(name="balance_value1")
	private String balanceValue1;//eth 数量
	
	@Column(name="balance_value2")
	private String balanceValue2;//usdt 数量
	
	@Column(name="balance_value3")
	private String balanceValue3;//
	
	@Column(name="balance_value4")
	private String balanceValue4;//
	
	@Column(name="remake")
	private String remake;//
	
	public AccountInfo(){}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public User getAccountUserId() {
		return accountUserId;
	}

	public void setAccountUserId(User accountUserId) {
		this.accountUserId = accountUserId;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccountFilePath() {
		return accountFilePath;
	}

	public void setAccountFilePath(String accountFilePath) {
		this.accountFilePath = accountFilePath;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
	
	public String getBalanceValue() {
		return balanceValue;
	}

	public void setBalanceValue(String balanceValue) {
		this.balanceValue = balanceValue;
	}

	public String getBalanceValue1() {
		return balanceValue1;
	}

	public void setBalanceValue1(String balanceValue1) {
		this.balanceValue1 = balanceValue1;
	}

	public String getBalanceValue2() {
		return balanceValue2;
	}

	public void setBalanceValue2(String balanceValue2) {
		this.balanceValue2 = balanceValue2;
	}

	public String getBalanceValue3() {
		return balanceValue3;
	}

	public void setBalanceValue3(String balanceValue3) {
		this.balanceValue3 = balanceValue3;
	}

	public String getBalanceValue4() {
		return balanceValue4;
	}

	public void setBalanceValue4(String balanceValue4) {
		this.balanceValue4 = balanceValue4;
	}

	public String getRemake() {
		return remake;
	}

	public void setRemake(String remake) {
		this.remake = remake;
	}
	
	public String getDeal() {
		return deal;
	}

	public void setDeal(String deal) {
		this.deal = deal;
	}

	public String getHexAddress() {
		return hexAddress;
	}

	public void setHexAddress(String hexAddress) {
		this.hexAddress = hexAddress;
	}

	@Override
	public String toString() {
		return "AccountInfo [accountId=" + accountId + ", type=" + type + ", deal=" + deal + ", status=" + status
				+ ", accountUserId=" + accountUserId + ", mnemonic=" + mnemonic + ", privateKey=" + privateKey
				+ ", publicKey=" + publicKey + ", address=" + address + ", hexAddress=" + hexAddress
				+ ", accountFilePath=" + accountFilePath + ", password=" + password + ", createTime=" + createTime
				+ ", des=" + des + ", balanceValue=" + balanceValue + ", balanceValue1=" + balanceValue1
				+ ", balanceValue2=" + balanceValue2 + ", balanceValue3=" + balanceValue3 + ", balanceValue4="
				+ balanceValue4 + ", remake=" + remake + "]";
	}

}
