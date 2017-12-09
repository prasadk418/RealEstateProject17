package com.realestate.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realestate.domain.Invoice;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice , Integer>{

	@Query("select o from Invoice o where o.invoiceDate >= :fromDate and o.invoiceDate <= :toDate")
	public List<Invoice> findByTwoInvoiceDates(@Param("fromDate") Date fromDate , @Param("toDate") Date toDate);
	
	
	@Query("select o from Invoice o where o.invoiceId = :invoiceId")
	public Invoice findInvoiceById(@Param("invoiceId") Integer invoiceId);
	
	@Query("select o from Invoice o where o.orderId.firm_name.user_id.user_name = :username")
	public List<Invoice> findInvoiceByUsername(@Param("username") String username);
	
	
}
