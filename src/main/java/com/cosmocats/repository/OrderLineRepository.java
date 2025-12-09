package com.cosmocats.repository;

import com.cosmocats.entity.OrderLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLineEntity, Long> {
}
