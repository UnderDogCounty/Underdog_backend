package com.underdogCounty.underdogCountyProject.domain.application.repository;

import com.underdogCounty.underdogCountyProject.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
}
