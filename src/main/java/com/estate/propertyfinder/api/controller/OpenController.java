package com.estate.propertyfinder.api.controller;

import com.estate.propertyfinder.api.dto.PropertyAddDto;
import com.estate.propertyfinder.api.dto.QueryRequestDto;
import com.estate.propertyfinder.api.service.PropertyService;
import com.estate.propertyfinder.auth.dto.RegisterUserDto;
import com.estate.propertyfinder.auth.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open")
public class OpenController {



    private PropertyService propertyService;
    public OpenController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    @PostMapping("/raise-query")
    public ResponseEntity<String> register(@RequestBody QueryRequestDto queryRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.raiseQuery(queryRequestDto));
    }
}
