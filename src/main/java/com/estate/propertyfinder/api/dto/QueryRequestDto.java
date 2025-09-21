package com.estate.propertyfinder.api.dto;

public class QueryRequestDto {
    private String fullName;
    private String clientPhoneNumber;
    private String clientEmail;
    private String message;
    private Long agentUserId;
    private Long propertyDetailId;

    public Long getPropertyDetailId() {
        return propertyDetailId;
    }

    public void setPropertyDetailId(Long propertyDetailId) {
        this.propertyDetailId = propertyDetailId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(Long agentUserId) {
        this.agentUserId = agentUserId;
    }
}
