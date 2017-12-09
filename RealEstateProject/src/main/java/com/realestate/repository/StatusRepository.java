package com.realestate.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.realestate.domain.Status;

public interface StatusRepository extends CrudRepository<Status, String>{

	@Query("select s from Status s where s.status=:status1")
	public Status findByStatus(@Param("status1") String status1);


}
