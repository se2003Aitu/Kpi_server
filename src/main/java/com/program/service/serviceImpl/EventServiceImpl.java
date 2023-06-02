package com.program.service.serviceImpl;

import com.program.model.Event;
import com.program.exception.EventException;
import com.program.model.Status;
import com.program.model.approve.Approve;
import com.program.model.submission.Submission;
import com.program.model.submission.TeacherSubmission;
import com.program.model.teacher.Teacher;
import com.program.model.teacher.TeacherEvent;
import com.program.model.teacher.TeacherEventId;
import com.program.repository.*;
import com.program.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TeacherEventRepository teacherEventRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherSubmissionRepository teacherSubmissionRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ApproveRepository approveRepository;

    @Override
    public List<Event> getAllEvents() throws EventException {
        return eventRepository.findAll();
    }

    @Override
    public Event addNewEvent(Integer statusId, Event event) throws EventException {
        if (event!=null) {
            Status status = statusRepository.findByStatusId(statusId);

            if (status != null) {
                event.setStatus(status);
                eventRepository.save(event);
                List<Teacher> teacherList = teacherRepository.findByCategoryAndStatusName(status.getCategoryName(),status.getStatusName());
                for (Teacher teacher : teacherList) {
                    TeacherEventId teacherEventId = new TeacherEventId(teacher.getTeacherId(), event.getEventId());
                    Approve approve = approveRepository.findApproveByName("none");
                    TeacherEvent teacherEvent = new TeacherEvent(teacherEventId, teacher, event, approve);

                    String input = event.getEventRates();
                    boolean hasSlash = input.contains("/");
                    if (hasSlash) {
                        String[] parts = input.split("/");
                        String numberBeforeSlash = parts[teacher.getTeacherRateId() - 1];
                        teacherEvent.setEventRate(numberBeforeSlash);
                    } else {
                        teacherEvent.setEventRate(input);
                    }
                    teacherEventRepository.save(teacherEvent);
                }
            }
            else {
                throw new EventException("Status that you indicated doesn't exist! Status Id: " + statusId);
            }
        }
        else {
            throw new EventException("Category details is Empty...");
        }
        return event;
    }

    @Override
    public Event getEventById(Integer eventId) throws EventException {
        Optional<Event> opt= eventRepository.findById(eventId);
        if(opt.isPresent()) {
            return opt.get();

        }
        else {
            throw new EventException("Event does not exist with Id : "+eventId);

        }
    }

    @Override
    public void updateEventById(Integer id, Event event) throws EventException {
        if (event == null) {
            throw new EventException("Event details is Empty...");
        }

        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new EventException("Event with ID " + id + " does not exist.");
        }
        Event existingEvent = optionalEvent.get();

        List<TeacherEvent> teacherEventList = teacherEventRepository.findTeachersByEventId(id);
        if (!teacherEventList.isEmpty()) {
            for (TeacherEvent teacherEvent : teacherEventList) {
                Teacher teacher = teacherRepository.findByTeacherId(teacherEvent.getTeacherId());
                if (!Objects.equals(existingEvent.getEventRates(), event.getEventRates())) {
                    String rateInput = event.getEventRates();
                    boolean hasSlash = rateInput.contains("/");
                    if (hasSlash) {
                        String[] parts = rateInput.split("/");
                        String numberBeforeSlash = parts[teacher.getTeacherRateId() - 1];
                        teacherEvent.setEventRate(numberBeforeSlash);
                    } else {
                        teacherEvent.setEventRate(rateInput);
                    }
                    teacherEventRepository.save(teacherEvent);
                }

                if (!Objects.equals(existingEvent.getEventPercentage(), event.getEventPercentage())) {
                    if (teacherEvent.isAccept()) {
                        Integer percent = existingEvent.getEventPercentage() - event.getEventPercentage();
                        Integer sum = teacher.getKpiSum() - percent;
                        teacher.setKpiSum(sum);
                        teacherRepository.save(teacher);
                    }
                }
            }
        }

//      Setter
        existingEvent.setEventName(event.getEventName());
        existingEvent.setEventPercentage(event.getEventPercentage());
        existingEvent.setEventRates(event.getEventRates());

        eventRepository.save(existingEvent);
    }

    @Override
    public void deleteEventById(Integer id) throws EventException {
        Optional<Event> opt= eventRepository.findById(id);
        if(opt.isPresent()) {

            List<TeacherEvent> teacherEventList = teacherEventRepository.findTeachersByEventId(id);
            if (!teacherEventList.isEmpty()){
                for (TeacherEvent teacherEvent : teacherEventList) {
                    if (teacherEvent.isAccept()){
                        Teacher teacher = teacherRepository.findByTeacherId(teacherEvent.getTeacherId());
                        Integer sum = teacher.getKpiSum() - teacherEvent.getEventPercentage();
                        teacher.setKpiSum(sum);
                        teacherRepository.save(teacher);
                    }
                }
            }

            List<TeacherSubmission> teacherSubmissionList = teacherSubmissionRepository.findByEventId(id);
            if (!teacherSubmissionList.isEmpty()){
                List<Submission> submissionList = new ArrayList<>();
                for (TeacherSubmission teacherSubmission : teacherSubmissionList){
                    submissionList.add(submissionRepository.findBySubmissionId(teacherSubmission.getSubmissionId()));
                }
                teacherSubmissionRepository.deleteByEventId(id);
                for (Submission submission : submissionList){
                    submissionRepository.deleteBySubmissionId(submission.getSubmissionId());
                }
            }

            teacherEventRepository.deleteByEventId(id);
            eventRepository.deleteByEventId(id);
        }
        else {
            throw new EventException("Event does not exist with Id : "+ id);

        }
    }

}
