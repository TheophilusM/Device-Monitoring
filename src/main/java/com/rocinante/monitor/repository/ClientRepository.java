package com.rocinante.monitor.repository;

import com.rocinante.monitor.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}