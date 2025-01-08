package com.clara.discographyservice.adapter.out.jpa.repository;

import com.clara.discographyservice.application.domain.model.release.ReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;


interface ReleaseJpaRepository extends JpaRepository<ReleaseEntity, Long> {

}
