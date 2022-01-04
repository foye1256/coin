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
@Table(name="aoa_batch_detailed_info")
//明细信息表
public class BatchDetailedInfo {

	@Id
	@Column(name="detailed_id")
	private String detailedId; //逻辑主键
	
	@Column(name="detailed_num")
	private String detailedNum;//明细编号
	
	@Column(name="batch_num")
	private String batchNum;//批次编号
	
	@Column(name="address")
	private String address;//地址
	
	@Column(name="balance_value")
	private String balanceValue;//金额数量
	
	@Column(name="balance_value1")
	private String balanceValue1;//金额数量
	
	@Column(name="balance_value2")
	private String balanceValue2;//金额数量
	
	@Column(name="balance_value3")
	private String balanceValue3;//金额数量
	
	@Column(name="balance_value4")
	private String balanceValue4;//金额数量
	
	@Column(name="user_name")
	private String userName;//创建人
	
	@Column(name="create_time")
	private Date createTime;//创建时间
	
	@Column(name="status")
	private Long status;//状态（是否可用）
	
	@Column(name="remake")
	private String remake;//备注
	
	@Column(name="spare_field1")
	private String spareField1;//备用字段
	
	@Column(name="spare_field2")
	private String spareField2;//
	
	@Column(name="spare_field3")
	private String spareField3;//
	
	@Column(name="spare_field4")
	private String spareField4;//
	
	@Column(name="spare_field5")
	private String spareField5;//
	
	@Column(name="type")
	private String type;//
	
	public BatchDetailedInfo(){}


	public String getDetailedId() {
		return detailedId;
	}


	public void setDetailedId(String detailedId) {
		this.detailedId = detailedId;
	}


	public String getDetailedNum() {
		return detailedNum;
	}


	public void setDetailedNum(String detailedNum) {
		this.detailedNum = detailedNum;
	}


	public String getBatchNum() {
		return batchNum;
	}


	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
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


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Long getStatus() {
		return status;
	}


	public void setStatus(Long status) {
		this.status = status;
	}


	public String getRemake() {
		return remake;
	}


	public void setRemake(String remake) {
		this.remake = remake;
	}


	public String getSpareField1() {
		return spareField1;
	}


	public void setSpareField1(String spareField1) {
		this.spareField1 = spareField1;
	}


	public String getSpareField2() {
		return spareField2;
	}


	public void setSpareField2(String spareField2) {
		this.spareField2 = spareField2;
	}


	public String getSpareField3() {
		return spareField3;
	}


	public void setSpareField3(String spareField3) {
		this.spareField3 = spareField3;
	}


	public String getSpareField4() {
		return spareField4;
	}


	public void setSpareField4(String spareField4) {
		this.spareField4 = spareField4;
	}


	public String getSpareField5() {
		return spareField5;
	}
	
	public void setSpareField5(String spareField5) {
		this.spareField5 = spareField5;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	@Override
	public String toString() {
		return "BatchDetailedInfo [detailedId=" + detailedId + ", detailedNum=" + detailedNum + ", batchNum=" + batchNum
				+ ", address=" + address + ", balanceValue=" + balanceValue + ", balanceValue1=" + balanceValue1
				+ ", balanceValue2=" + balanceValue2 + ", balanceValue3=" + balanceValue3 + ", balanceValue4="
				+ balanceValue4 + ", userName=" + userName + ", createTime=" + createTime + ", status=" + status
				+ ", remake=" + remake + ", spareField1=" + spareField1 + ", spareField2=" + spareField2
				+ ", spareField3=" + spareField3 + ", spareField4=" + spareField4 + ", spareField5=" + spareField5
				+ ", type=" + type + "]";
	}
	
}
