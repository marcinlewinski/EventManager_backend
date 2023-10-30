package com.wildevent.WildEventMenager.location.service;

import com.wildevent.WildEventMenager.event.repository.EventRepository;
import com.wildevent.WildEventMenager.location.model.Location;
import com.wildevent.WildEventMenager.location.model.dto.*;
import com.wildevent.WildEventMenager.location.repository.LocationRepository;
import com.wildevent.WildEventMenager.location.service.dtoMappers.LocationDTOMapper;
import com.wildevent.WildEventMenager.map.model.Map;
import com.wildevent.WildEventMenager.map.repository.MapRepository;
import com.wildevent.WildEventMenager.user.model.WildUser;
import com.wildevent.WildEventMenager.user.repository.WildUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationDTOMapper locationDTOMapper;
    private final MapRepository mapRepository;

    private final EventRepository eventRepository;

    private final WildUserRepository wildUserRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, LocationDTOMapper locationDTOMapper, MapRepository mapRepository, EventRepository eventRepository, WildUserRepository wildUserRepository) {
        this.locationRepository = locationRepository;
        this.locationDTOMapper = locationDTOMapper;
        this.mapRepository = mapRepository;
        this.eventRepository = eventRepository;
        this.wildUserRepository = wildUserRepository;
    }

    @Override
    public List<LocationPointDTO> getLocationPoints() {
        List<Location> locations = locationRepository.findAll();
        return locationDTOMapper.getLocationPointsDtoFromLocation(locations);
    }

    @Override
    public Optional<LocationDTO> getLocationById(UUID id) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59);
        return locationRepository.findLocationWithEventsBetweenDates(id, now, endOfDay)
                .map(locationDTOMapper::getLocationDtoFromLocation);
    }

    @Override
    public List<LocationIdTitleDTO> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream()
                .map(locationDTOMapper::getLocationIdTitleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LocationWithCoordinateDTO saveLocation(ReceivedLocationDTO locationDTO) {
        Map map = mapRepository.findFirstByCurrentIsTrue();
        Location locationFromDTO = locationDTOMapper.getNewLocationFromDTO(locationDTO, map);
        Location savedLocation = locationRepository.save(locationFromDTO);
        return locationDTOMapper.getMapLocation(savedLocation);
    }

    @Override
    public LocationWithCoordinateDTO updateLocation(ReceivedLocationDTO locationDTO) {
        Optional<Location> oldLocation = locationRepository.findById(locationDTO.getId());

        if (oldLocation.isPresent()) {
            Location location = oldLocation.get();
            Location updatedLocation = locationDTOMapper.getUpdatedLocationFromReceivedDto(locationDTO, location);
            Location savedLocation = locationRepository.save(updatedLocation);
            return locationDTOMapper.getMapLocation(savedLocation);
        } else {
            throw new NoSuchElementException("Location not found with ID: " + locationDTO.getId());
        }
    }

    @Override
    @Transactional
    public boolean deleteLocationById(UUID id) {
        Optional<Location> locationById = locationRepository.findById(id);
        if (locationById.isPresent()) {
            Location location = locationById.get();
            eventRepository.deleteAllEventsFromThisLocation(location);
            for (WildUser wildUser : location.getWildUser()) {
                wildUser.getLocation().remove(location);
                wildUserRepository.save(wildUser);
            }
            locationRepository.delete(location);
            return true;
        }
        return false;
    }

    @Override
    public List<Location> mapLocationsFromIds(List<String> locationIds) {
        return locationIds.stream()
                .map(UUID::fromString)
                .flatMap(uuid -> locationRepository.findById(uuid).stream())
                .collect(Collectors.toList());
    }
}
