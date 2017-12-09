package com.realestate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realestate.domain.UOM;
@Repository
public interface UOMRepository extends JpaRepository<UOM , Integer>{

	/*@Query("select o from UOM o where o.material.id=:material")
	public Iterable<UOM>  findByMaterial(@Param("material") Integer material); 
	*/
	@Query("select o from UOM o where o.name=:name")
	public String findByUOM(String name);
}
