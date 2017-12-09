package com.realestate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realestate.domain.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String>{
	
	@Query("select b from UserInfo b where b.firm_name=:firmname ")
	public UserInfo findUsersByFirmname(@Param("firmname") String firmname); 
	
	@Query("select b from UserInfo b where b.firm_name=:firmname and b.user_id.user_type=:usertype")
	public List<UserInfo> findUsersByFirmnameAndUsertype(@Param("firmname") String firmname , @Param("usertype") String usertype); 
		
	@Query("select b from UserInfo b where b.user_id.status.status=:status")
	public List<UserInfo> getUsersByStatus(@Param("status") String status);
	
	@Query("select b from UserInfo b where b.user_id.status.status=:status and b.user_id.user_type=:usertype")
	public List<UserInfo> getUsersByStatusAndUserType(@Param("status") String status, @Param("usertype") String usertype);
	
	@Query("select b from UserInfo b where b.user_id.status.status=:status and b.user_id.user_type=:usertype")
	public List<UserInfo> getUsersByStatusAndType(@Param("status") Integer status , @Param("usertype")String usertype);
	
	@Query("select b from UserInfo b where b.business_email=:username")
	public UserInfo findUserByUserName(@Param("username") String username);
	
	@Query(value="select * from users where users.user_id like :userid%", nativeQuery=true)
	public UserInfo searchUserById(@Param("userid") String userid);
	
	@Query(value="select * from users where users.full_name like :username%", nativeQuery=true)
	public UserInfo searchUserByName(@Param("username") String username);
	
	@Query(value="select distinct ui.firm_name , ui.mobile , us.vendor_materials from userinfo ui, users us where ui.firm_name like :firmname% and us.status='3' and us.user_type='vendor' and us.vendor_materials is not null", nativeQuery=true)
	public List<Object[]> searchUserByFirmName(@Param("firmname") String firmname);
	
	@Query("select b from UserInfo b where b.user_id.status.status='Approved' and b.user_id.vendor_materials like :material%")
	public List<UserInfo> vendorsByMaterial(@Param("material") String material);
	 //select distinct ui.firm_name , ui.mobile , us.vendor_materials from userinfo ui INNER JOIN users us on ui.business_email = us.user_name and  us.status='3' and us.user_type='vendor' and us.vendor_materials is not null
	@Query(value="select distinct ui.firm_name , ui.mobile , us.vendor_materials from userinfo ui INNER JOIN users us on ui.business_email = us.user_name and  us.status='3' and us.user_type='vendor' and us.vendor_materials is not null",  nativeQuery=true)
	public List<Object[]> getApprovedVendors();
	 
	
}
