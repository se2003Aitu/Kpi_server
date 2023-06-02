package com.program.repository;

import com.program.model.teacher.TeacherRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TeacherRateRepository extends JpaRepository<TeacherRate, Integer> {

    @Query("SELECT t FROM TeacherRate t WHERE t.teacherRateName = :teacherRateName")
    TeacherRate findTeacherRateByName(String teacherRateName);

}
