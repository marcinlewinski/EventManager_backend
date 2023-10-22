package com.wildevent.WildEventMenager.myEvent.service.dtoMapper;

import com.wildevent.WildEventMenager.event.model.Event;
import com.wildevent.WildEventMenager.myEvent.model.MyEventDTO;

public interface MyEventDTOMapper {
    MyEventDTO getEventDtoFromEvent(Event event);
}
