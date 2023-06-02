package com.program.controller;

import com.program.exception.EventException;
import com.program.model.Event;
import com.program.repository.EventRepository;
import com.program.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class EventController {

    @Autowired
    public EventService eventService;

    @Autowired
    public EventRepository eventRepository;


    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OBSERVER')")
    public ResponseEntity<List<Event>> getAllEvents() throws EventException {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<List<Event>>(events, HttpStatus.OK);
    }

    @PostMapping("status/{Id}/event/add")
    @PreAuthorize("hasRole('ADMIN')")
    public HttpEntity<? extends Object> addNewEvent(@PathVariable("Id") Integer id, @RequestBody Event event) {
        try{
            Event newEvent = eventService.addNewEvent(id,event);
            return new ResponseEntity<Event>(newEvent, HttpStatus.OK);
        }catch (EventException ex) {
            String errorMessage = "Error setting: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("event/getById/{Id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OBSERVER')")
    public ResponseEntity getEventById(@PathVariable("Id") Integer id ) {
        try {
            Event event = eventService.getEventById(id);
            return new ResponseEntity<Event>(event,HttpStatus.OK);
        }catch (EventException ex) {
            String errorMessage = "Error setting: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("event/update/{Id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateEventById(@PathVariable("Id") Integer id ){
        try{
            Event event = eventService.getEventById(id);
            return new ResponseEntity<Event>(event,HttpStatus.OK);
        }catch (EventException ex) {
            String errorMessage = "Error setting with update: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("event/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateEventById(@PathVariable Integer id,@RequestBody Event event) {
        try {
            eventService.updateEventById(id, event);
            return new ResponseEntity<>("event updated", HttpStatus.OK);
        } catch (EventException ex) {
            String errorMessage = "Error setting: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("event/delete/{Id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteEventById(@PathVariable ("Id") Integer id ) {
        try {
            eventService.deleteEventById(id);
            return new ResponseEntity<>("Event with this Id deleted",HttpStatus.OK);
        }catch (EventException ex) {
            String errorMessage = "Error setting: " + ex.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
