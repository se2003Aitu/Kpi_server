package com.program.repository;

import com.program.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.program.model.Category;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    @Query("SELECT c FROM Category c WHERE c.categoryId = :categoryId")
    Category findByCategoryId(Integer categoryId);

    @Query("SELECT c FROM Category c WHERE c.categoryName = :categoryName")
    Category findByCategoryName(String categoryName);

    @Modifying
    @Query("DELETE FROM Category c WHERE c.categoryId = :categoryId")
    void deleteByCategoryId(Integer categoryId);
}
