package com.estate.propertyfinder.api.repository;

import com.estate.propertyfinder.api.models.QueryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryMasterRepository extends JpaRepository<QueryMaster, Long> {
}