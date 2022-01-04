package cn.gson.oasys.model.dao.ethdao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import cn.gson.oasys.model.entity.eth.AccountInfo;
import cn.gson.oasys.model.entity.user.User;

public interface AccountInfoDao extends PagingAndSortingRepository<AccountInfo, Long>{
	//根据状态和user来找account
	Page<AccountInfo> findByAccountUserId(User user,Pageable page);

	//根据用户和type排序account
	Page<AccountInfo> findByAccountUserIdOrderByType(User user,Pageable page);

	//根据用户和status排序account
	Page<AccountInfo> findByAccountUserIdOrderByStatus(User user,Pageable page);

	//根据用户和创建时间排序account
	Page<AccountInfo> findByAccountUserIdOrderByCreateTimeDesc(User user,Pageable page);

	//根据address模糊查找account
	@Query("select ai from AccountInfo ai where  ai.address like %:val% ")
	Page<AccountInfo> findByAddressLike(@Param("val")String val,Pageable page);

	List<AccountInfo> findByStatusAndAccountUserId(Long status,User u);

	@Query("select ai from AccountInfo ai where  ai.type =?1 ")
	Page<AccountInfo> findListByType(Pageable pa);

	@Query("select ai from AccountInfo ai where  ai.type = ?1 ")
	Page<AccountInfo> findAccount(String type, Pageable pa);

	@Query("select ai from AccountInfo ai where ai.type = :type and ai.remake like %:val% ")
	Page<AccountInfo> findAccounts(@Param("type")String type,@Param("val")String val, Pageable pa);

	List<AccountInfo> findByTypeOrderByCreateTimeDesc(String type);

	AccountInfo findByAccountId(long parseLong);

	AccountInfo findByAddress(String fromAddress);

	//更改备注
	@Query("update AccountInfo a set a.balanceValue=?1 ,a.balanceValue1=?2,a.remake=?3  where a.accountId=?4")
	@Modifying
	Integer updateBlance(String ethBlance,String usdtBlance,String remake,long accountId);

	@Query("select ai from AccountInfo ai where ai.type = :type and ai.status = :status ")
	Page<AccountInfo> findAccountsBystatus(@Param("type")String type, @Param("status")Long statusId, Pageable pa);

	@Query("update AccountInfo a set a.balanceValue=?1 ,a.balanceValue1=?2 where a.accountId=?3")
	@Modifying
	void updateBlances(String ethBlance, String erc20Balance, Long accountId);

	@Query("select ai from AccountInfo ai where ai.type = :type and ai.status = :status and ai.remake like %:val% ")
	Page<AccountInfo> findAccountsBystatus1(@Param("type")String type, @Param("val")String val, @Param("status")long parseLong, Pageable pa);

	@Query("update AccountInfo a set a.balanceValue=?1 ,a.remake=?2  where a.accountId=?3")
	@Modifying
	void updateBlance(String ethBlance, String nremake, Long accountId);
	
	@Query("update AccountInfo a set a.balanceValue1=?1 ,a.remake=?2  where a.accountId=?3")
	@Modifying
	void updateTokenBlance(String tokenBlance, String nremake, Long accountId);

}
