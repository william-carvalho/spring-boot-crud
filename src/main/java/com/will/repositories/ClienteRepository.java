package com.will.repositories;

import com.will.model.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    List<Cliente> findAll();

    Optional<Cliente> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
