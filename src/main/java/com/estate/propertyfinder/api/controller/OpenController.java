package com.estate.propertyfinder.api.controller;

import com.estate.propertyfinder.api.dto.*;
import com.estate.propertyfinder.api.models.PropertyDetailsMaster;
import com.estate.propertyfinder.api.models.QueryMaster;
import com.estate.propertyfinder.api.service.PropertyService;
import com.estate.propertyfinder.auth.dto.RegisterUserDto;
import com.estate.propertyfinder.auth.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open")
public class OpenController {



    private PropertyService propertyService;
    public OpenController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    @PostMapping("/raise-query")
    public ResponseEntity<QueryMasterResponse> register(@RequestBody QueryRequestDto queryRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.raiseQuery(queryRequestDto));
    }
    @GetMapping("/property/getAll")
    public ResponseEntity<PageResponse<GetAllProperties>> getAllProperty(@RequestParam int page,@RequestParam int size) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.getAll(page,size));
    }
}
