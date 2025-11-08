package com.estate.propertyfinder.api.service;

import com.estate.propertyfinder.api.dto.*;
import com.estate.propertyfinder.api.models.ImageMaster;
import com.estate.propertyfinder.api.models.PropertyDetailsMaster;
import com.estate.propertyfinder.api.models.QueryMaster;
import com.estate.propertyfinder.api.repository.PropertyDetailsMasterRepository;
import com.estate.propertyfinder.api.repository.QueryMasterRepository;
import com.estate.propertyfinder.auth.models.User;
import com.estate.propertyfinder.auth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PropertyService {

    private PropertyDetailsMasterRepository propertyDetailsMasterRepository;
    private UserRepository userRepository;
    private QueryMasterRepository queryMasterRepository;
    private RestTemplate restTemplate;

    public PropertyService(PropertyDetailsMasterRepository propertyDetailsMasterRepository,UserRepository userRepository,QueryMasterRepository queryMasterRepository,RestTemplate restTemplate){
        this.propertyDetailsMasterRepository = propertyDetailsMasterRepository;
        this.userRepository = userRepository;
        this.queryMasterRepository = queryMasterRepository;
        this.restTemplate = restTemplate;
    }



    public GetAllProperties addProperty(PropertyAddDto dto) {
        checkAuthenticity(dto.getUserId());
        User user = getUser(dto.getUserId());
        PropertyDetailsMaster property = mapDtoToEntity(dto, user);
        PropertyDetailsMaster saved = propertyDetailsMasterRepository.save(property);
        GetAllProperties res = maptoRes(saved);
        return res;
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
                    img.setImageUrl(saveImageToCloud(imgDto.getImgbase64Format()));
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
        queryMaster.setQueryText(dto.getQueryText());
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
    private String saveImageToCloud(String base64Fromat){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", base64Fromat);
        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);
        ResponseEntity<UploadResponse> responseEntity = restTemplate.postForEntity(
                "https://api.imgbb.com/1/upload?expiration=0&key=7e911fda5c06a9699c0df57d6cf27e16",
                requestEntity,
                UploadResponse.class
        );
        return responseEntity.getBody().getData().getUrl();
    }
    public PageResponse<GetAllProperties> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PropertyDetailsMaster> propertyPage = propertyDetailsMasterRepository.findAll(pageable);

        // Map entities to DTOs
        List<GetAllProperties> dtoList = propertyPage.getContent().stream()
                .map(property -> {
                    GetAllProperties dto=  maptoRes(property);
                    return dto;
                })
                .toList();

        // Prepare PageResponse
        PageResponse<GetAllProperties> response = new PageResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(propertyPage.getNumber());
        response.setPageSize(propertyPage.getSize());
        response.setTotalElements(propertyPage.getTotalElements());
        response.setTotalPages(propertyPage.getTotalPages());
        response.setLast(propertyPage.isLast());

        return response;
    }

    private GetAllProperties maptoRes(PropertyDetailsMaster property){
        GetAllProperties dto = new GetAllProperties();
        dto.setId(property.getId());
        dto.setPropertyName(property.getPropertyName());
        dto.setPropertyType(property.getPropertyType());
        dto.setPrice(property.getPrice());
        dto.setBedrooms(property.getBedrooms());
        dto.setBathrooms(property.getBathrooms());
        dto.setDimension(property.getDimension());
        dto.setStatus(property.getStatus());
        dto.setDiscription(property.getDiscription());
        dto.setLocation(property.getLocation());
        dto.setCreatedAt(property.getCreatedAt());
        dto.setUpdatedAt(property.getUpdatedAt());

        if (property.getUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(property.getUser().getId());
            userDto.setFullName(property.getUser().getFullName());
            userDto.setEmail(property.getUser().getEmail());
            dto.setUser(userDto);
        }

        if (property.getImages() != null) {
            dto.setImages(
                    property.getImages().stream()
                            .map(image -> {
                                ImageDto imageDto = new ImageDto();
                                imageDto.setId(image.getId());
                                imageDto.setImageUrl(image.getImageUrl());
                                return imageDto;
                            })
                            .toList()
            );
        }
        if (property.getQueries() != null) {
            dto.setQueries(
                    property.getQueries().stream()
                            .map(query -> {
                                QueryRequestDto queryRequestDto = new QueryRequestDto();
                                queryRequestDto.setId(query.getId());
                                queryRequestDto.setClientEmail(query.getClientEmail());
                                queryRequestDto.setQueryText(query.getQueryText());
                                queryRequestDto.setClientPhoneNumber(query.getClientPhoneNumber());
                                queryRequestDto.setFullName(query.getClientFullName());
                                return queryRequestDto;
                            })
                            .toList()
            );
        }
        return dto;
    }
}
