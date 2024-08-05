package com.asr.project.repositories;

import com.asr.project.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Page<Category> findByTitleContaining(String keyword, Pageable pageable);

}
