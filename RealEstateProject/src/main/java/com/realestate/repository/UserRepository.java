package com.realestate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realestate.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, String>{

	@Query("select u from User u where u.user_name=:username ")
	public User findByUsername(@Param("username") String username); 
		
	/*@Query("select u from User u where u.user_name=:username ")
	public User checkStatus(@Param("username") String username);
	*/
	@Query("select u from User u where u.status=:status and u.user_type != 'admin'")
	public List<User> getBuildersByStatus(@Param("status") String status);
	
	
	
	@Modifying
	@Query("update User u set u.password=:password where u.user_name=:username")
	public Integer changePassword(@Param("password") String password , @Param("username") String username); 
	
	
	@Modifying
	@Query("update User u set u.status.status=:status where u.user_name=:username")
	public Integer changeStatus(@Param("status") String status , @Param("username") String username); 
	
	
	
	/*@Modifying
	@Query("update User u set u.sfindByUsernametatus=:status where u.user_name=:username")
	public Integer updateStatus(@Param("status") String status , @Param("username") String username); 
	*/
}
