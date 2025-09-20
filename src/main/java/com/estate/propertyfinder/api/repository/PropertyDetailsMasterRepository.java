package com.estate.propertyfinder.api.repository;

import com.estate.propertyfinder.api.models.ImageMaster;
import com.estate.propertyfinder.api.models.PropertyDetailsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyDetailsMasterRepository extends JpaRepository<PropertyDetailsMaster, Long> {
}