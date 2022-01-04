package cn.gson.oasys.model.dao.ethdao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import cn.gson.oasys.model.entity.eth.BatchRecordInfo;

public interface BatchRecordInfoDao extends PagingAndSortingRepository<BatchRecordInfo, Long>{
	
	@Query("select ai from BatchRecordInfo ai where ai.batchContent like %:val%")
	Page<BatchRecordInfo> findList(@Param("val")String val,Pageable pa);
	
	BatchRecordInfo findByBatchId(String batchId);
	
	BatchRecordInfo findByBatchNum(String batchNum);

}
