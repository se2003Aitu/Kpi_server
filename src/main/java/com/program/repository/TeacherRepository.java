package com.program.repository;

import com.program.model.Category;
import com.program.model.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT t FROM Teacher t WHERE t.user.userId = :userId")
    Teacher findByUserId(Long userId);

    @Query("SELECT t FROM Teacher t WHERE t.teacherId = :teacherId")
    Teacher findByTeacherId(Long teacherId);

    @Query("SELECT t FROM Teacher t WHERE t.categoryName = :categoryName")
    List<Teacher> findByCategoryName(String categoryName);

    @Query("SELECT t FROM Teacher t WHERE t.categoryName = :categoryName and t.statusName = :statusName")
    List<Teacher> findByCategoryAndStatusName(String categoryName, String statusName);

//    @Modifying
//    @Query("UPDATE Teacher t SET t.statusName = null WHERE t.categoryName = :categoryName and t.statusName = :statusName")
//    void resetStatusName(String categoryName, String statusName);
//
//    @Modifying
//    @Query("UPDATE Teacher t SET t.categoryName = null, t.statusName = null WHERE t.categoryName = :categoryName")
//    void resetCategoryNameAndStatusName(String categoryName);


}
