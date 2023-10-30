package com.wildevent.WildEventMenager.location.service;

import com.wildevent.WildEventMenager.location.model.Location;
import com.wildevent.WildEventMenager.location.model.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Service
public interface LocationService {
    List<LocationPointDTO> getLocationPoints();
    Optional<LocationDTO> getLocationById(UUID id);

    List<Location> mapLocationsFromIds(List<String> locationIds);
    List<LocationIdTitleDTO> getAllLocations();

    LocationWithCoordinateDTO saveLocation(ReceivedLocationDTO locationDTO);

    LocationWithCoordinateDTO updateLocation(ReceivedLocationDTO locationDTO);

    boolean deleteLocationById(UUID id);
}


