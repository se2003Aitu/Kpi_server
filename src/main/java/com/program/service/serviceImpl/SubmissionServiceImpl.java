package com.program.service.serviceImpl;

import com.program.exception.SubmissionException;
import com.program.model.Event;
import com.program.model.submission.Submission;
import com.program.model.submission.TeacherSubmission;
import com.program.model.submission.TeacherSubmissionId;
import com.program.model.teacher.Teacher;
import com.program.model.teacher.TeacherEvent;
import com.program.repository.*;
import com.program.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private TeacherEventRepository teacherEventRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TeacherSubmissionRepository teacherSubmissionRepository;


    @Override
    public void saveSubmit(Long userId, Integer eventId, MultipartFile file, Date modifyDate) throws SubmissionException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if(fileName.contains("..")) {
                throw new SubmissionException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Teacher teacher = teacherRepository.findByUserId(userId);
            Event event = eventRepository.findEventById(eventId);
            TeacherEvent teacherEvent = teacherEventRepository.findEventAndTeacherId(teacher.getTeacherId(), eventId);

            if (teacherEvent!= null) {
                teacherEvent.setSubmissionStatus(true);
                teacherEvent.setModifyDate(modifyDate);
                teacherEventRepository.save(teacherEvent);

                Submission submission = new Submission(fileName, file.getContentType(),file.getSize(), file.getBytes());
                submissionRepository.save(submission);

                TeacherSubmissionId teacherSubmissionId = new TeacherSubmissionId(teacher.getTeacherId(),event.getEventId(),submission.getSubmissionId());
                TeacherSubmission teacherSubmission = new TeacherSubmission(teacherSubmissionId,teacher,event,submission);
                teacherSubmissionRepository.save(teacherSubmission);
            }else
                throw new SubmissionException("This teacher had no events related to this id: " + eventId);

        } catch (IOException ex) {
            throw new SubmissionException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Submission getSubmission(String id) throws Exception {
        return submissionRepository
                .findBySubmissionId(id);
//                .orElseThrow(
//                        () -> new Exception("File not found with Id: " + id));
    }

    @Override
    public List<Submission> getSubmissions(Long teacherId, Integer eventId) throws SubmissionException {
        List<TeacherSubmission> teacherSubmissionList = teacherSubmissionRepository.findTeacherSubmissionsById(teacherId,eventId);
        if (!teacherSubmissionList.isEmpty()) {
            List<Submission> submissions = new ArrayList<>();
            for (TeacherSubmission teacherSubmission : teacherSubmissionList) {
                Submission submission = submissionRepository.findBySubmissionId(teacherSubmission.getSubmissionId());
                submissions.add(submission);
            }
            return submissions;
        }else
            throw new SubmissionException("This teacher doesn't have submissions or events");
    }

    @Override
    public void deleteSubmission(Long userId,String submissionId, Date modifyDate) throws SubmissionException {
        Optional<Submission> optionalSubmission = submissionRepository.findById(submissionId);
        Teacher teacher = teacherRepository.findByUserId(userId);
        if(optionalSubmission.isPresent()){
            TeacherSubmission teacherSubmission = teacherSubmissionRepository.findTeacherBySubmissionId(teacher.getTeacherId(), submissionId);
            if (teacherSubmission!=null) {
                TeacherEvent teacherEvent = teacherEventRepository.findEventAndTeacherId(teacher.getTeacherId(), teacherSubmission.getEventId());

                teacherSubmissionRepository.deleteBySubmissionId(submissionId);
                submissionRepository.deleteBySubmissionId(submissionId);

                List<TeacherSubmission> teacherSubmissionList = teacherSubmissionRepository.findTeacherSubmissionsById(teacher.getTeacherId(),teacherSubmission.getEventId());
                if (teacherEvent!=null) {
                    teacherEvent.setModifyDate(modifyDate);
                    teacherEventRepository.save(teacherEvent);
                    if (teacherSubmissionList.isEmpty()){
                        teacherEvent.setSubmissionStatus(false);
                        teacherEventRepository.save(teacherEvent);
                    }
                }

            }else
                throw new SubmissionException("This user hasn't permission to delete other teacher's submission!");
        }else
            throw new SubmissionException("The file with id " + submissionId + " doesn't exist!" );
    }


}
