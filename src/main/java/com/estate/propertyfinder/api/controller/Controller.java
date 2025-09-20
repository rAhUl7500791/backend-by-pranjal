package com.estate.propertyfinder.api.controller;

import com.estate.propertyfinder.api.dto.PropertyAddDto;
import com.estate.propertyfinder.api.models.PropertyDetailsMaster;
import com.estate.propertyfinder.api.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property")
public class Controller {

    private PropertyService propertyService;
    public Controller(PropertyService propertyService){
        this.propertyService = propertyService;
    }
    @PostMapping("/add")
    public ResponseEntity<String> register(@RequestBody PropertyAddDto propertyAddDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.addProperty(propertyAddDto));
    }

}
