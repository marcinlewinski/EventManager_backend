package com.wildevent.WildEventMenager.event.repository;

import com.wildevent.WildEventMenager.event.model.Event;
import com.wildevent.WildEventMenager.location.model.Location;
import com.wildevent.WildEventMenager.user.model.WildUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findAllByOpenToPublicIsTrueAndStartsAtBetweenOrderByStartsAtAsc(LocalDateTime now, LocalDateTime endOfDay);
    // TODO: Consider whether, after removing content from MVP,
    //  we should still maintain the distinction between public and private events.
    //  This caused issues when updating events to public after the 'startsAt' time has passed.
    @Query("SELECT e FROM Event e WHERE (e.startsAt < :endOfDay OR e.startsAt = :endOfDay) AND e.endsAt > :startOfDay")
    List<Event> findAllEventsOnDay(@Param("startOfDay") LocalDateTime startOfDay,
                                   @Param("endOfDay") LocalDateTime endOfDay);

    @Modifying
    @Query("DELETE FROM Event e WHERE e.location = :location")
    void deleteAllEventsFromThisLocation(Location location);
}
