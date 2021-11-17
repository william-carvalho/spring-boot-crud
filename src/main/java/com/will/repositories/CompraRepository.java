package com.will.repositories;

import com.will.model.Compra;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CompraRepository extends CrudRepository<Compra, Long>  {
    List<Compra> findAll();
}
