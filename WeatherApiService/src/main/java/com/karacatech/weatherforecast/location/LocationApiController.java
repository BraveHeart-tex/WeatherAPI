package com.karacatech.weatherforecast.location;

import com.karacatech.weatherforecast.common.Location;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/locations")
public class LocationApiController {
    private final LocationService locationService;
    private final ModelMapper modelMapper;

    @Autowired
    public LocationApiController(LocationService locationService,
                                 ModelMapper modelMapper) {
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<LocationDTO> add(@RequestBody @Valid LocationDTO dto) {
        Location addedLocation = locationService.add(dtoToEntity(dto));
        URI uri = URI.create("api/v1/locations/" + addedLocation.getCode());

        return ResponseEntity.created(uri).body(entityToDTO(addedLocation));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Location> locations = locationService.getAll();

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listEntityToListDTO(locations));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocation(@PathVariable String code) {
        Location location = locationService.getByCode(code);

        return ResponseEntity.ok(entityToDTO(location));
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO DTO) {
        Location updatedLocation = locationService.update(dtoToEntity(DTO));

        return ResponseEntity.ok((entityToDTO(updatedLocation)));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable String code) {
        locationService.delete(code);
        return ResponseEntity.noContent().build();
    }

    private List<LocationDTO> listEntityToListDTO(List<Location> listEntity) {
        return listEntity.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    private LocationDTO entityToDTO(Location entity) {
        return modelMapper.map(entity, LocationDTO.class);
    }

    private Location dtoToEntity(LocationDTO dto) {
        return modelMapper.map(dto, Location.class);
    }
}
