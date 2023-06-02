package com.program.service.serviceImpl;

import com.program.exception.AssignException;
import com.program.model.*;
import com.program.model.approve.Approve;
import com.program.model.submission.Submission;
import com.program.model.submission.TeacherSubmission;
import com.program.model.teacher.Teacher;
import com.program.model.teacher.TeacherEvent;
import com.program.model.teacher.TeacherEventId;
import com.program.model.teacher.TeacherRate;
import com.program.payload.request.AssignRequest;
import com.program.repository.*;
import com.program.service.AssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AssignServiceImpl implements AssignService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherEventRepository teacherEventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TeacherRateRepository teacherRateRepository;

    @Autowired
    private ApproveRepository approveRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private TeacherSubmissionRepository teacherSubmissionRepository;

    @Override
    public void assignTeacherEvents(AssignRequest assignRequest) throws AssignException {
        User user = userRepository.findByEmail(assignRequest.getEmail());
        if (user!=null){
            if (user.isTeacher()) {
                Teacher teacher = teacherRepository.findByUserId(user.getUserId());
                if (teacher == null) {
                    teacher = new Teacher(user, 0);
                }

                if (Objects.equals(assignRequest.getCategoryName(), teacher.getCategoryName())
                        && Objects.equals(assignRequest.getStatusName(), teacher.getStatusName())
                        && Objects.equals(assignRequest.getDepartmentName(), teacher.getDepartmentName())
                        && Objects.equals(assignRequest.getRateName(), teacher.getTeacherRate())) {
                    throw new AssignException("This teacher already has assigned events!");
                }

                Category category = categoryRepository.findByCategoryName(assignRequest.getCategoryName());
                if (category != null) {
                    Status status = statusRepository.findByStatusNameAndCategoryId(assignRequest.getStatusName(), category.getCategoryId());
                    if (status != null) {
                        Department department = departmentRepository.findByDepartmentName(assignRequest.getDepartmentName());
                        if (department != null) {
                            TeacherRate teacherRate = teacherRateRepository.findTeacherRateByName(assignRequest.getRateName());
                            if (teacherRate != null) {
                                teacher.setDepartment(department);
                                teacher.setTeacherRate(teacherRate);

                                if (teacher.getCategoryName() != null && teacher.getStatusName() != null) {
                                    Category existingCategory = categoryRepository.findByCategoryName(teacher.getCategoryName());
                                    Status existingStatus = statusRepository.findByStatusNameAndCategoryId(teacher.getStatusName(), existingCategory.getCategoryId());
                                    if (existingStatus != status) {
                                        teacher.setKpiSum(0);
                                        teacherEventRepository.deleteByTeacherAndEventId(teacher.getTeacherId());
                                        List<Event> eventList = eventRepository.findByStatusId(existingStatus.getStatusId());
                                        for (Event event : eventList) {
                                            List<TeacherSubmission> teacherSubmissionList = teacherSubmissionRepository.findTeacherSubmissionsById(teacher.getTeacherId(), event.getEventId());
                                            if (!teacherSubmissionList.isEmpty()) {
                                                List<Submission> submissionList = new ArrayList<>();
                                                for (TeacherSubmission teacherSubmission : teacherSubmissionList) {
                                                    submissionList.add(submissionRepository.findBySubmissionId(teacherSubmission.getSubmissionId()));
                                                }
                                                teacherSubmissionRepository.deleteByEventId(event.getEventId());
                                                for (Submission submission : submissionList) {
                                                    submissionRepository.deleteBySubmissionId(submission.getSubmissionId());
                                                }
                                            }

                                        }
                                    }
                                }

                                teacher.setCategoryName(assignRequest.getCategoryName());
                                teacher.setStatusName(assignRequest.getStatusName());
                                teacherRepository.save(teacher);
                                List<Event> eventList = eventRepository.findByStatusId(status.getStatusId());
                                for (Event event : eventList) {
                                    TeacherEvent teacherEvent = teacherEventRepository.findEventAndTeacherId(teacher.getTeacherId(), event.getEventId());
                                    if (teacherEvent == null) {
                                        TeacherEventId teacherEventId = new TeacherEventId(teacher.getTeacherId(), event.getEventId());
                                        Approve approve = approveRepository.findApproveByName("none");
                                        teacherEvent = new TeacherEvent(teacherEventId, teacher, event, approve);
                                    }
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
                            } else
                                throw new AssignException("The " + assignRequest.getRateName() + " rate doesn't exist!");
                        } else
                            throw new AssignException("The department with name " + assignRequest.getDepartmentName() + " doesn't exist!");
                    } else
                        throw new AssignException("The status with name " + assignRequest.getStatusName() + " doesn't exist!");
                } else
                    throw new AssignException("The category with name " + assignRequest.getCategoryName() + " doesn't exist!");
            }else
                throw new AssignException("This user isn't a teacher!");
        }else
            throw new AssignException("User with email " + assignRequest.getEmail() + " doesn't exist!");
    }


}
