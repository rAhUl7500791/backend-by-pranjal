package com.estate.propertyfinder.api.dto;

public class ImageDto {
    private Long id;
    private String imgbase64Format;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImgbase64Format() {
        return imgbase64Format;
    }

    public void setImgbase64Format(String imgbase64Format) {
        this.imgbase64Format = imgbase64Format;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
