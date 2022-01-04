package cn.gson.oasys.model.dao.ethdao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import cn.gson.oasys.model.entity.eth.BatchDetailedInfo;
import cn.gson.oasys.model.entity.user.User;

public interface BatchDetailedInfoDao extends PagingAndSortingRepository<BatchDetailedInfo, Long>{
	//根据DetailedId
	BatchDetailedInfo findByDetailedId(String detailedId);
	
	@Query("select bi from BatchDetailedInfo bi where  bi.address = ?1 ")
	List<BatchDetailedInfo> findByAddress(String fromAddress);
	
	//根据BatchNum
	@Query("select bi from BatchDetailedInfo bi where  bi.batchNum = ?1 ")
	Page<BatchDetailedInfo> findByBatchNum(String batchNum,Pageable page);

	@Query("select bi from BatchDetailedInfo bi where  bi.batchNum = ?1 ")
	List<BatchDetailedInfo> getByBatchNum(String batchNum);

	@Query("update BatchDetailedInfo bd set bd.balanceValue=?1 ,bd.balanceValue1=?2 where bd.detailedId=?3")
	@Modifying
	void updateBlances(String ethBlance, String erc20Balance, String detailedId);

}
