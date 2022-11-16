package com.green.project.repository;

import com.green.project.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long>, PlantRepositoryCustom {
}
