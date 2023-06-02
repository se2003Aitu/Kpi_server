package com.program.repository;

import com.program.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface EventRepository extends JpaRepository<Event, Integer> {

    Event save(Integer eventId);

    @Query("SELECT e FROM Event e WHERE e.status.statusId = :statusId")
    List<Event> findByStatusId(Integer statusId);

    @Query("SELECT e FROM Event e WHERE e.eventId = :eventId")
    Event findEventById(Integer eventId);

    @Modifying
    @Query("UPDATE Event e SET e.status = null WHERE e.status.statusId = :statusId")
    void resetStatusByStatusId(Integer statusId);

    @Modifying
    @Query("DELETE FROM Event e WHERE e.eventId = :eventId")
    void deleteByEventId(Integer eventId);



}
