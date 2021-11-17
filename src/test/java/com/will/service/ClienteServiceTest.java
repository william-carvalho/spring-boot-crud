package com.will.service;

import com.will.exceptions.EntityAlreadyRegisteredException;
import com.will.model.Cliente;
import com.will.repositories.ClienteRepository;
import com.will.validators.ValidacaoGeral;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClienteServiceTest {

    @Autowired
    private ClienteService clientService;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private ValidacaoGeral validacaoGeral;

    @Test
    @DisplayName("criarCliente - Quando o CPF não estiver cadastrado deve salvar o Cliente")
    public void test01() throws Exception {
        Cliente client = new Cliente(1L, "65432789032", "Bob", LocalDate.of(1984, 11, 25));

        clientService.criarCliente(client);

        verify(clienteRepository, times(1)).existsByCpf(client.getCpf());
        verify(clienteRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("criarCliente - Quando o CPF é registrado deve lançar exceção")
    public void test02() throws EntityAlreadyRegisteredException {
        Cliente client = new Cliente(1L, "65432789032", "Marley", LocalDate.of(1986, 6, 10));
        doReturn(true).when(clienteRepository).existsByCpf(client.getCpf());

        Assertions.assertThrows(EntityAlreadyRegisteredException.class, () -> {
            clientService.criarCliente(client);
        });

        verify(clienteRepository, times(0)).save(client);
    }

    @Test
    @DisplayName("deletaCliente - Quando o cliente existe deve deleta")
    public void test03() throws NoSuchElementException {
        long clientId = 1L;
        doReturn(true).when(clienteRepository).existsById(clientId);

        clientService.deletaCliente(clientId);

        verify(clienteRepository, times(1)).existsById(clientId);
        verify(clienteRepository, times(1)).deleteById(clientId);
    }

    @Test
    @DisplayName("deletaCliente - Quando o cliente não existe, deve lançar NoSuchElementException")
    public void test04() throws NoSuchElementException {
        long clientId = 1L;
        doReturn(false).when(clienteRepository).existsById(clientId);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            clientService.deletaCliente(clientId);
        });

        verify(clienteRepository, times(1)).existsById(clientId);
        verify(clienteRepository, times(0)).deleteById(clientId);
    }

    @Test
    @DisplayName("findClientes - Deve retornar a lista de clientes")
    public void test05() {
        List<Cliente> clients = Arrays.asList(
                new Cliente(1L, "65432789032", "Alan", LocalDate.of(1962, 5, 9)),
                new Cliente(2L, "65432789032", "Turing", LocalDate.of(1970, 12, 22))
        );
        doReturn(clients).when(clienteRepository).findAll();

        List<Cliente> result = clientService.findClientes();

        verify(clienteRepository, times(1)).findAll();
        assertEquals(clients.size(), result.size());
    }

    @Test
    @DisplayName("findCliente - Quando o cliente não existe, não deve ocorrer SuchElementException")
    public void test06() {
        doReturn(Optional.empty()).when(clienteRepository).findByCpf(any());
        String cpf = "75695674852";

        Assertions.assertThrows(NoSuchElementException.class, () -> {
           clientService.findCliente(cpf);
        });

        verify(clienteRepository, times(1)).findByCpf(cpf);
    }

    @Test
    @DisplayName("findCliente - Quando o cliente existe, deve-se encontrar o cliente")
    public void test07() {
        Cliente client = new Cliente(1L, "75695674852", "jack", LocalDate.of(1958, 2, 9));
        doReturn(Optional.of(client)).when(clienteRepository).findByCpf(client.getCpf());

        Cliente result = clientService.findCliente(client.getCpf());

        verify(clienteRepository, times(1)).findByCpf(client.getCpf());
        assertEquals(client.getId(), result.getId());
    }

    @Test
    @DisplayName("editaCliente - Quando o cliente não existe, deve lançar NoSuchElementException")
    public void test08() {
        long clientId = 1L;
        Cliente client = new Cliente(clientId, "75695674852", "daniels", LocalDate.of(1978, 3, 9));
        doReturn(Optional.empty()).when(clienteRepository).findById(clientId);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            clientService.editaCliente(client);
        });

        verify(clienteRepository, times(1)).findById(clientId);
        verify(clienteRepository, times(0)).save(client);
    }

    @Test
    @DisplayName("editaCliente - Quando o cliente não foi modificado, não deve salvar o cliente\n")
    public void test09() {
        Cliente client = new Cliente(1L, "75695674852", "daniels", LocalDate.of(1978, 6, 11));
        doReturn(Optional.of(client)).when(clienteRepository).findById(client.getId());

        clientService.editaCliente(client);

        verify(clienteRepository, times(1)).findById(client.getId());
        verify(clienteRepository, times(0)).save(client);
    }

    @Test
    @DisplayName("editaCliente - Quando o cliente é modificado deve salvar o cliente com novas informações\n")
    public void test10() {
        Cliente client = new Cliente(1L, "75695674852", "Silvio", LocalDate.of(1938, 9, 06));
        Cliente alteredCliente = new Cliente(1L, "75695674852", "Santos", LocalDate.of(1958, 10, 06));
        doReturn(Optional.of(client)).when(clienteRepository).findById(client.getId());

       clientService.editaCliente(alteredCliente);

       verify(clienteRepository, times(1)).findById(client.getId());
       verify(clienteRepository, times(1)).save(client);
    }

}