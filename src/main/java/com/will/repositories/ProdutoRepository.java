package com.will.repositories;

import com.will.model.Produto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface ProdutoRepository extends CrudRepository<Produto, Long> {
    List<Produto> findAll();

    Optional<Produto> findByUnidadeDeArmazenamento(String unidadeDeArmazenamento);

    boolean existsByUnidadeDeArmazenamento(String unidadeDeArmazenamento);

}
