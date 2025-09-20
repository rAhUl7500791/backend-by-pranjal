package com.estate.propertyfinder.api.repository;

import com.estate.propertyfinder.api.models.ImageMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMasterRepository extends JpaRepository<ImageMaster, Long> {
}
