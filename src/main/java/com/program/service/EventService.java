package com.program.service;

import com.program.exception.StatusException;
import com.program.model.Event;
import com.program.exception.EventException;
import com.program.model.Status;

import java.util.List;

public interface EventService {

    List<Event> getAllEvents() throws EventException;

    Event addNewEvent(Integer statusId, Event event) throws EventException;

    Event getEventById(Integer eventId) throws EventException;

    void updateEventById(Integer id, Event event) throws EventException;

    void deleteEventById(Integer id) throws EventException;

}
