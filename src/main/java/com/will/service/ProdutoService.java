package com.will.service;

import com.will.exceptions.EntityAlreadyRegisteredException;
import com.will.model.Produto;
import com.will.repositories.ProdutoRepository;
import com.will.validators.ValidacaoGeral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProdutoService {

    public static final String NOT_FOUND_MESSAGE = "Produto não encontrado";
    public static final String CODE_ALREADY_REGISTERED_MESSAGE = "Código de barras já existe";

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    private ValidacaoGeral validacaoGeral;

    public Produto criarProduto(Produto produto) throws Exception {
        validacaoGeral.validateIdZeroOrNull(produto.getId());
        if(produtoRepository.existsByUnidadeDeArmazenamento(produto.getUnidadeDeArmazenamento()))
            throw new EntityAlreadyRegisteredException(CODE_ALREADY_REGISTERED_MESSAGE);
        return produtoRepository.save(produto);
    }

    public void deletaProduto(long id) {
        if(produtoRepository.existsById(id))
            produtoRepository.deleteById(id);
        else
            throw new NoSuchElementException(NOT_FOUND_MESSAGE);
    }

    public List<Produto> findProdutos() {
        return produtoRepository.findAll();
    }

    public Produto findProduto(String unidadeDeArmazenamento) {
        Optional<Produto> optProduto = produtoRepository.findByUnidadeDeArmazenamento(unidadeDeArmazenamento);
        if(!optProduto.isPresent())
            throw new NoSuchElementException(NOT_FOUND_MESSAGE);
        return optProduto.get();
    }

    public Produto editaProduto(Produto produto) {
        Produto persistentProduto = produtoRepository.findById(produto.getId())
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));

        boolean wasModified = false;
        if(!produto.getUnidadeDeArmazenamento().equals(persistentProduto.getUnidadeDeArmazenamento())) {
            persistentProduto.setUnidadeDeArmazenamento(produto.getUnidadeDeArmazenamento());
            wasModified = true;
        }
        if(!produto.getNome().equals(persistentProduto.getNome())) {
            persistentProduto.setNome(produto.getNome());
            wasModified = true;
        }
        if(!produto.getDescricao().equals(persistentProduto.getDescricao())) {
            persistentProduto.setDescricao(produto.getDescricao());
            wasModified = true;
        }
        if(produto.getValor() != persistentProduto.getValor()) {
            persistentProduto.setValor(produto.getValor());
            wasModified = true;
        }
        if(produto.getQuantidade() != persistentProduto.getQuantidade()) {
            persistentProduto.setQuantidade(produto.getQuantidade());
            wasModified = true;
        }

        return wasModified ? produtoRepository.save(persistentProduto) : persistentProduto;
    }

}
