package com.program.service.serviceImpl;

import com.program.exception.TeacherEventException;
import com.program.exception.TeacherException;
import com.program.model.approve.Approve;;
import com.program.model.teacher.Teacher;
import com.program.model.teacher.TeacherEvent;
import com.program.repository.ApproveRepository;
import com.program.repository.TeacherEventRepository;
import com.program.repository.TeacherRepository;
import com.program.service.TeacherEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TeacherEventServiceImpl implements TeacherEventService {


    @Autowired
    private TeacherEventRepository teacherEventRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ApproveRepository approveRepository;


    @Override
    public List<TeacherEvent> getAllTeacherEvent() throws TeacherEventException {
        return teacherEventRepository.findAll();
    }

    @Override
    public List<TeacherEvent> getTeachersByEvent(Integer eventId) throws TeacherEventException {
        List<TeacherEvent> teacherEvents = teacherEventRepository.findTeachersByEventId(eventId);
        if (!teacherEvents.isEmpty()) {
            List<TeacherEvent> newTeacherEvent = new ArrayList<>();
            for (TeacherEvent teacherEvent : teacherEvents){
                newTeacherEvent.add(teacherEvent);
            }
            return newTeacherEvent;
        }
        return null;
    }

    @Override
    public void setEventApprove(Long teacherId, Integer eventId, Approve approve) throws TeacherEventException{
        TeacherEvent existingTeacherEvent = teacherEventRepository.findEventAndTeacherId(teacherId,eventId);
        Teacher existingTeacher = teacherRepository.findByTeacherId(teacherId);
        Approve existingApprove = approveRepository.findApproveByName(approve.getApproveName());
        if (existingApprove == null){
            throw new TeacherEventException("This approve status doesn't exist");
        }
        if (existingTeacherEvent!=null) {
            if (!Objects.equals(existingApprove.getApproveName(), existingTeacherEvent.getApproveName())) {

                if (existingTeacherEvent.isAccept()){
                    existingTeacherEvent.setApprove(existingApprove);
                    teacherEventRepository.save(existingTeacherEvent);
                    if (existingTeacherEvent.isReject() || existingTeacherEvent.isNone()) {
                        if (existingTeacher.getKpiSum() > 0) {
                            if (existingTeacher.getKpiSum() >= existingTeacherEvent.getEventPercentage()) {
                                Integer sum = existingTeacher.getKpiSum() - existingTeacherEvent.getEventPercentage();
                                existingTeacher.setKpiSum(sum);
                            }
                        }
                    }
                }

                if (existingTeacherEvent.isNone() || existingTeacherEvent.isReject()){
                    existingTeacherEvent.setApprove(existingApprove);
                    teacherEventRepository.save(existingTeacherEvent);
                    if (existingTeacherEvent.isAccept()) {
                        Integer sum = existingTeacher.getKpiSum() + existingTeacherEvent.getEventPercentage();
                        existingTeacher.setKpiSum(sum);
                    }
                        }

                    } else
                         throw new TeacherEventException("This approve status already set!");
                }else
                    throw new TeacherEventException("An error related to the teacher's id or event's id ");

                teacherRepository.save(existingTeacher);
    }


    @Override
    public void addComment(Long userId, Integer eventId, String comment) throws TeacherEventException {
        Teacher teacher = teacherRepository.findByUserId(userId);
        TeacherEvent existingTeacherEvent = teacherEventRepository.findEventAndTeacherId(teacher.getTeacherId(),eventId);
        if (existingTeacherEvent==null){
            throw new TeacherEventException("This teacher' event doesn't exist");
        }
        existingTeacherEvent.setComment(comment);
        teacherEventRepository.save(existingTeacherEvent);
    }

    @Override
    public void deleteComment(Long userId, Integer eventId) throws TeacherEventException {
        Teacher teacher = teacherRepository.findByUserId(userId);
        TeacherEvent existingTeacherEvent = teacherEventRepository.findEventAndTeacherId(teacher.getTeacherId(),eventId);
        if (existingTeacherEvent==null){
            throw new TeacherEventException("This teacher' event doesn't exist");
        }
        existingTeacherEvent.setComment(null);
        teacherEventRepository.save(existingTeacherEvent);
    }


}
