package com.realestate.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realestate.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order , String>{

	@Query("select o from Order o where o.orderStatus=:orderStatus ")
	public List<Order> findByOrderStatus(@Param("orderStatus") String orderStatus); 
	
	@Query("select o from Order o where o.material.name=:material ")
	public List<Order> findByMaterial(@Param("material") String material); 
	
	@Query("select o from Order o where o.orderId=:orderId ")
	public Order findByOrderId(@Param("orderId") String orderId); 
	
	@Query("select o from Order o where o.orderDate=:orderDate ")
	public List<Order> findByOrderDate(@Param("orderDate") Date orderDate); 
	
	@Query("select o from Order o where o.orderDate >= :fromDate and o.orderDate <= :toDate")
	public List<Order> findByTwoOrderDates(@Param("fromDate") Date fromDate , @Param("toDate") Date toDate);
	
	@Query("select o from Order o where o.firm_name = :firmName")
	public List<Order> findByFirmName(@Param("firmName") String firmName);
	
	@Query("select o from Order o where o.firm_name.business_email = :username")
	public List<Order> findByUserName(@Param("username") String username);

	@Query("select o from Order o where o.firm_name = :firmName AND  o.orderStatus=:orderStatus AND o.orderDate >= :fromDate and o.orderDate <= :toDate")
	public List<Order> finalSearch(@Param("firmName") String firmName , @Param("orderStatus") String orderStatus , @Param("fromDate") Date fromDate , @Param("toDate") Date toDate);
	
	
	/*@Query("select o from Order o where o.orderStatus.status=:status")
	public List<Order> findByDeliveredOderStatus(@Param("status")String status);
	*/
	@Query("select o from Order o where o.orderStatus.status=:status")
	public List<Order> findAllOrdersByStatus(@Param("status")String status);
	
	@Query("select o from Order o where o.orderStatus.status in :status")
	public List<Order> findAllOrdersByStatusList(@Param("status") List<String> status);
	
	/*@Query("select o from Order o where o.orderStatus.status=:status  AND o.firm_name.user_id.user_name=:username")
	public List<Order> findMyRejectedOderStatus(@Param("status")String status, @Param("username")String username);
	*/
	@Query("select o from Order o where o.orderStatus.status in :status  AND o.firm_name.user_id.user_name=:username")
	public List<Order> findMyOdersByStatus(@Param("username")String username,@Param("status")List<String> status);
	
	@Query("select o from Order o where o.vendor_firm=:vendorfirm")
	public List<Order> findVendorInvolvedOrders(@Param("vendorfirm")String vendorfirm);
			
	@Query("select o from Order o where o.orderStatus.status=:status and o.vendor_firm=:vendorfirm")
	public List<Order> findVendorInvolvedOrdersByStatus(@Param("vendorfirm")String vendorfirm , @Param("status")String status);
	
}
