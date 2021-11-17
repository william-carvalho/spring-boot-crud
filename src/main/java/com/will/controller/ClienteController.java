package com.will.controller;

import com.will.model.Cliente;
import com.will.service.ClienteService;
import com.will.utils.CustomErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    ClienteService clientService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findAll() {
        List<Cliente> clients = clientService.findClientes();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping(value = "/{cpf}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findCliente(@PathVariable String cpf) {
        Cliente client;
        try {
            client = clientService.findCliente(cpf);
        } catch(NoSuchElementException ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.OK);
        }

        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> criarCliente(@RequestBody Cliente client) {
        try {
            Cliente registeredCliente = clientService.criarCliente(client);
            return new ResponseEntity<>(registeredCliente, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deletaCliente(@PathVariable("id") long id) {
        try {
            clientService.deletaCliente(id);
        } catch(NoSuchElementException ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editaCliente(@RequestBody Cliente client) {
        Cliente modifiedCliente;
        try {
            modifiedCliente = clientService.editaCliente(client);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(new CustomErrorType(ex.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(modifiedCliente, HttpStatus.OK);
    }
}
