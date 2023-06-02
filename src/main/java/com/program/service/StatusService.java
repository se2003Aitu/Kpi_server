package com.program.service;

import java.util.List;

import com.program.exception.EventException;
import com.program.model.Status;
import com.program.exception.StatusException;


public interface StatusService {

	  List<Status>  getAllStatus() throws StatusException;

	  Status addNewStatus(Integer categoryId,Status status)throws StatusException;

	  Status getStatusById(Integer Id)throws StatusException;

	  void updateStatusById(Integer id, Status status) throws StatusException;

	  void deleteStatusById(Integer Id) throws StatusException, EventException;

}
