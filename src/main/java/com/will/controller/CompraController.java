package com.will.controller;

import com.will.model.Compra;
import com.will.model.Item;
import com.will.service.CompraService;
import com.will.utils.CustomErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/compra")
public class CompraController {

    @Autowired
    CompraService compraService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> criarCompra(@RequestBody Compra compra) {
        try {
            compraService.criarCompra(compra);
            return new ResponseEntity<>(compra, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletaCompra(@PathVariable long id) {
        try {
            compraService.deletaCompra(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.OK);
        }
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findAll() {
        List<Compra> compras = compraService.buscaCompras();
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @PutMapping(value = "/editaPurchaseDate", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editaPurchaseDate(@RequestBody Compra compra) {
        try {
            Compra compraFinal = compraService.editaDataCompra(compra);
            return new ResponseEntity<>(compraFinal, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/adicionaItens/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> adicionaItens(@PathVariable("id") long compraId, @RequestBody List<Item> items) {
        try {
            Compra compra = compraService.adicionaItens(compraId, items);
            return new ResponseEntity<>(compra, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/deletaItems/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deletaItems(@PathVariable("id") long id, @RequestBody List<Item> items) {
        try {
            Compra compra = compraService.deletaItens(id, items);
            return new ResponseEntity<>(compra, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.OK);
        }
    }
}
