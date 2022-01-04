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
@Table(name="aoa_batch_record_info")
//批次信息表
public class BatchRecordInfo {

	@Id
	@Column(name="batch_id")
	private String batchId; //逻辑主键
	
	@Column(name="batch_num")
	private String batchNum;//批次编号
	
	@Column(name="batch_content")
	private String batchContent;//内容
	
	@Column(name="user_name")
	private String userName;//创建人
	
	@Column(name="create_time")
	private Date createTime;//创建时间
	
	@Column(name="type")
	private String type;//类型
	
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
	
	
	public BatchRecordInfo(){}


	public String getBatchId() {
		return batchId;
	}


	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}


	public String getBatchNum() {
		return batchNum;
	}


	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}


	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getBatchContent() {
		return batchContent;
	}

	public void setBatchContent(String batchContent) {
		this.batchContent = batchContent;
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

	@Override
	public String toString() {
		return "BatchRecordInfo [batchId=" + batchId + ", batchNum=" + batchNum + ", batchContent=" + batchContent
				+ ", userName=" + userName + ", createTime=" + createTime + ", type=" + type + ", status=" + status
				+ ", remake=" + remake + ", spareField1=" + spareField1 + ", spareField2=" + spareField2
				+ ", spareField3=" + spareField3 + ", spareField4=" + spareField4 + ", spareField5=" + spareField5
				+ "]";
	}
	
}
