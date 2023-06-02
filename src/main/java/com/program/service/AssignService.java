package com.program.service;

import com.program.exception.AssignException;
import com.program.payload.request.AssignRequest;

public interface AssignService {

    void assignTeacherEvents(AssignRequest assignRequest) throws AssignException;

}
