package com.program.repository;

import com.program.model.submission.TeacherSubmissionId;
import com.program.model.submission.TeacherSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TeacherSubmissionRepository extends JpaRepository<TeacherSubmission, TeacherSubmissionId> {

    @Query("SELECT t FROM TeacherSubmission t WHERE t.teacher.teacherId =:teacherId AND t.event.eventId =:eventId")
    List<TeacherSubmission> findTeacherSubmissionsById(Long teacherId,Integer eventId);

    @Query("SELECT t FROM TeacherSubmission t WHERE t.event.eventId =:eventId")
    List<TeacherSubmission> findByEventId(Integer eventId);

    @Query("SELECT t FROM TeacherSubmission t WHERE t.teacher.teacherId =:teacherId AND t.submission.submissionId =:submissionId")
    TeacherSubmission findTeacherBySubmissionId(Long teacherId,String submissionId);

    @Modifying
    @Query("DELETE FROM TeacherSubmission t WHERE t.submission.submissionId = :submissionId")
    void deleteBySubmissionId(String submissionId);

    @Modifying
    @Query("DELETE FROM TeacherSubmission t WHERE t.event.eventId = :eventId")
    void deleteByEventId(Integer eventId);

}
