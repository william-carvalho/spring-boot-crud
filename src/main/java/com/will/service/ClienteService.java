package com.will.service;

import com.will.exceptions.EntityAlreadyRegisteredException;
import com.will.model.Cliente;
import com.will.repositories.ClienteRepository;
import com.will.validators.ValidacaoGeral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ValidacaoGeral validacaoGeral;

    public static final String CPF_ALREADY_REGISTERED_MESSAGE = "CPF já registrado";
    public static final String NOT_FOUND_MESSAGE = "Cliente não encontrado";

    public Cliente criarCliente(Cliente cliente) throws Exception {
        validacaoGeral.validateIdZeroOrNull(cliente.getId());
        if(clienteRepository.existsByCpf(cliente.getCpf()))
            throw new EntityAlreadyRegisteredException(CPF_ALREADY_REGISTERED_MESSAGE);
        return clienteRepository.save(cliente);
    }

    public void deletaCliente(long id) throws NoSuchElementException{
        if(clienteRepository.existsById(id))
            clienteRepository.deleteById(id);
        else
            throw new NoSuchElementException(NOT_FOUND_MESSAGE);
    }

    public List<Cliente> findClientes() {
        return clienteRepository.findAll();
    }

    public Cliente findCliente(String cpf) throws NoSuchElementException {
        Optional<Cliente> optCliente = clienteRepository.findByCpf(cpf);
        if(!optCliente.isPresent())
            throw new NoSuchElementException(NOT_FOUND_MESSAGE);
        return optCliente.get();
    }

    public Cliente editaCliente(Cliente cliente) throws NoSuchElementException {
        Cliente persistentCliente = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));

        boolean wasModified = false;
        if(!cliente.getCpf().equals(persistentCliente.getCpf())) {
            persistentCliente.setCpf(cliente.getCpf());
            wasModified = true;
        }
        if(!cliente.getNome().equals(persistentCliente.getNome())){
            persistentCliente.setNome(cliente.getNome());
            wasModified = true;
        }
        if(!cliente.getDataNascimento().equals(persistentCliente.getDataNascimento())) {
            persistentCliente.setDataNascimento(cliente.getDataNascimento());
            wasModified = true;
        }

        return wasModified ? clienteRepository.save(persistentCliente) : persistentCliente;
    }

}
