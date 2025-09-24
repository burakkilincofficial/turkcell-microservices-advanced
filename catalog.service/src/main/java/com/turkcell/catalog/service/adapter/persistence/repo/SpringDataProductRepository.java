package com.turkcell.catalog.service.adapter.persistence.repo;

import com.turkcell.catalog.service.adapter.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, UUID> { }
