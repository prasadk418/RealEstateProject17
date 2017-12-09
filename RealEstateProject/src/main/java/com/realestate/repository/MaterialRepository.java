package com.realestate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realestate.domain.Material;
import com.realestate.domain.UOM;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer>{

		@Query("select m from Material m")
		public List<Material> findAllMaterials();
		
		/*@Query(value="select m.name from material m where m.id in ")
		public List<String> getMaterialNames();*/
		
		@Query("select m.id from Material m")
		public List<String> getMaterialId();
		@Query("select m.name from Material m where m.name=:name")
		public String findByMaterialByName(@Param("name")String name);
		
		@Query("select m.uom from Material m where m.id=:mid")
		public List<UOM> findUOMByMaterial(@Param("mid") Integer mid);
		
}
