package com.program.repository;

import com.program.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface StatusRepository extends  JpaRepository<Status, Integer> {

	Status save(Integer productId);

	@Query("SELECT s FROM Status s WHERE s.category.categoryId = :categoryId")
	List<Status> findByCategoryId(Integer categoryId);

	@Query("SELECT s FROM Status s WHERE s.statusId = :statusId")
	Status findByStatusId(Integer statusId);

	@Query("SELECT s FROM Status s WHERE s.statusName = :statusName AND s.category.categoryId = :categoryId")
	Status findByStatusNameAndCategoryId(String statusName, Integer categoryId);

	@Query("SELECT s FROM Status s WHERE s.statusId = :statusId")
	List<Status> findByStatusesId(Integer statusId);

	@Modifying
	@Query("DELETE FROM Status s WHERE s.statusId = :statusId")
	void deleteByStatusId(Integer statusId);

	@Modifying
	@Query("DELETE FROM Status s WHERE s.category.categoryId = :categoryId")
	void deleteByCategoryId(Integer categoryId);

}
