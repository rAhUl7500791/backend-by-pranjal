package com.estate.propertyfinder.api.repository;

import com.estate.propertyfinder.api.models.PropertyDetailsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyDetailsMasterRepository extends JpaRepository<PropertyDetailsMaster, Long> {
    List<PropertyDetailsMaster> findByUserId(Long userId);
}