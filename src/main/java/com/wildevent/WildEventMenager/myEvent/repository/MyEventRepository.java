package com.wildevent.WildEventMenager.myEvent.repository;

import com.wildevent.WildEventMenager.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MyEventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByOrganizer_Id(UUID loggedInUserId);
}

