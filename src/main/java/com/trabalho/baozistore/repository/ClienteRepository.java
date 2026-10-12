package com.trabalho.baozistore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.trabalho.baozistore.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}