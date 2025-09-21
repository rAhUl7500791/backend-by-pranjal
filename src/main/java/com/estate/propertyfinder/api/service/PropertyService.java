package com.estate.propertyfinder.api.service;

import com.estate.propertyfinder.api.dto.ImageDto;
import com.estate.propertyfinder.api.dto.PropertyAddDto;
import com.estate.propertyfinder.api.dto.QueryRequestDto;
import com.estate.propertyfinder.api.models.ImageMaster;
import com.estate.propertyfinder.api.models.PropertyDetailsMaster;
import com.estate.propertyfinder.api.models.QueryMaster;
import com.estate.propertyfinder.api.repository.PropertyDetailsMasterRepository;
import com.estate.propertyfinder.api.repository.QueryMasterRepository;
import com.estate.propertyfinder.auth.models.User;
import com.estate.propertyfinder.auth.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private PropertyDetailsMasterRepository propertyDetailsMasterRepository;
    private UserRepository userRepository;
    private QueryMasterRepository queryMasterRepository;

    public PropertyService(PropertyDetailsMasterRepository propertyDetailsMasterRepository,UserRepository userRepository,QueryMasterRepository queryMasterRepository){
        this.propertyDetailsMasterRepository = propertyDetailsMasterRepository;
        this.userRepository = userRepository;
        this.queryMasterRepository = queryMasterRepository;
    }



    public String addProperty(PropertyAddDto dto) {
        checkAuthenticity(dto.getUserId());
        User user = getUser(dto.getUserId());
        PropertyDetailsMaster property = mapDtoToEntity(dto, user);
        PropertyDetailsMaster saved = propertyDetailsMasterRepository.save(property);
        return "Success";
    }

    private void checkAuthenticity(Long userId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailOrUsername = auth.getName(); // depends on your auth setup
        User currentUser = userRepository.findByEmail(emailOrUsername)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        if (userId != null && !userId.equals(currentUser.getId())) {
            throw new RuntimeException("You cannot add property for another user!");
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private PropertyDetailsMaster mapDtoToEntity(PropertyAddDto dto, User user) {
        PropertyDetailsMaster property = new PropertyDetailsMaster();
        property.setPropertyName(dto.getPropertyName());
        property.setPropertyType(dto.getPropertyType());
        property.setPrice(dto.getPrice());
        property.setBedrooms(dto.getBedrooms());
        property.setBathrooms(dto.getBathrooms());
        property.setDimension(dto.getDimension());
        property.setStatus(dto.getStatus());
        property.setDiscription(dto.getDiscription());
        property.setLocation(dto.getLocation());
        property.setUser(user);

        property.setImages(mapImages(dto.getImages(), property));

        return property;
    }

    private List<ImageMaster> mapImages(List<ImageDto> imagesDto, PropertyDetailsMaster property) {
        if (imagesDto == null) return List.of();
        return imagesDto.stream()
                .map(imgDto -> {
                    ImageMaster img = new ImageMaster();
                    img.setImageUrl(imgDto.getImageUrl());
                    img.setProperty(property);
                    return img;
                })
                .toList();
    }

    public String raiseQuery(QueryRequestDto queryRequestDto){
        User user = getUser(queryRequestDto.getAgentUserId());
        PropertyDetailsMaster propertyDetailsMaster = mapDtoToEntity(queryRequestDto.getPropertyDetailId());
        queryMasterRepository.save(mapDtoToEntity(queryRequestDto,propertyDetailsMaster,user));
        return "Success";
    }
    private QueryMaster mapDtoToEntity(QueryRequestDto dto, PropertyDetailsMaster propertyDetailsMaster, User user) {
        QueryMaster queryMaster = new QueryMaster();
        queryMaster.setProperty(propertyDetailsMaster);
        queryMaster.setUser(user);
        queryMaster.setQueryText(dto.getMessage());
        queryMaster.setStatus("OPEN");
        queryMaster.setClientPhoneNumber(dto.getClientPhoneNumber());
        queryMaster.setClientEmail(dto.getClientEmail());
        queryMaster.setClientFullName(dto.getFullName());
        return queryMaster;
    }
    private PropertyDetailsMaster mapDtoToEntity(Long propertyDetailId){
        PropertyDetailsMaster propertyDetailsMaster = new PropertyDetailsMaster();
        propertyDetailsMaster.setId(propertyDetailId);
        return  propertyDetailsMaster;
    }
}
