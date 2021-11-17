package com.will.controller;

import com.will.model.Produto;
import com.will.service.ProdutoService;
import com.will.utils.CustomErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/produto")
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> criarProduto(@RequestBody Produto produto) {
        try {
            Produto registeredCliente = produtoService.criarProduto(produto);
            return new ResponseEntity<>(registeredCliente, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deletaProduto(@PathVariable("id") long id) {
        try {
            produtoService.deletaProduto(id);
        } catch(Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findAll() {
        List<Produto> produtos = produtoService.findProdutos();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{code}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findProduto(@PathVariable String sku) {
        Produto produto;
        try {
            produto = produtoService.findProduto(sku);
            return new ResponseEntity<>(produto, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editaProduto(@RequestBody Produto produto) {
        Produto modifiedProduto;
        try {
            modifiedProduto = produtoService.editaProduto(produto);
            return new ResponseEntity<>(modifiedProduto, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
