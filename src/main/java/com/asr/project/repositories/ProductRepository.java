package com.asr.project.repositories;

import com.asr.project.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByLiveTrue(Pageable pageable);
    Page<Product> findByTitleContaining(String keyword, Pageable pageable);
}
