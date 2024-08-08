package com.asr.project.repositories;

import com.asr.project.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findByUser_UserId(String userId, Pageable pageable);

    List<Order> findByUser_UserId(String userId);
}
