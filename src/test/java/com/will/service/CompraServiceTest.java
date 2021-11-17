package com.will.service;

import com.will.model.Cliente;
import com.will.model.Compra;
import com.will.model.Item;
import com.will.model.Produto;
import com.will.repositories.ClienteRepository;
import com.will.repositories.ItemRepository;
import com.will.repositories.CompraRepository;
import com.will.repositories.ProdutoRepository;
import com.will.validators.ValidacaoGeral;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CompraServiceTest {

    @Autowired
    private CompraService compraService;

    @MockBean
    private CompraRepository compraRepository;

    @MockBean
    private ProdutoRepository produtoRepository;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private ValidacaoGeral validacaoGeral;

    @MockBean
    private ItemRepository orderItemRepository;

    @Test
    @DisplayName("criarOrder - Quando o pedido não tem itens, deve lançar uma exceção\n")
    public void test01() {
        Compra compra = new Compra();
        compra.setItems(Collections.emptyList());

        Exception exception = assertThrows(Exception.class, () -> {
            compraService.criarCompra(compra);
        });

        Assertions.assertEquals(CompraService.EMPTY_ORDER_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("criarOrder - Quando o pedido tem itens, deve-se salvar o pedido")
    public void test02() throws Exception {
        int produtoQuantidadeInicial = 10;
        Produto produto = new Produto(1L, "code1", "nome1", "descricao1", 5, produtoQuantidadeInicial);
        doReturn(Optional.of(produto)).when(produtoRepository).findById(any());
        Cliente cliente = new Cliente(1L, "65432789032", "Jack", LocalDate.of(1958, 11, 11));
        doReturn(Optional.of(cliente)).when(clienteRepository).findById(any());

        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setItems(Collections.singletonList(
                new Item(1L, compra, produto, 1)
        ));

        compraService.criarCompra(compra);

        assertEquals(produto.getValor(), compra.getValor());
        assertEquals(produtoQuantidadeInicial - 1, produto.getQuantidade());
        verify(compraRepository, times(1)).save(compra);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    @DisplayName("deletaOrder - Quando o pedido não existe, deve lançar NoSuchElementException")
    public void test03() {
        long compraId = 1L;
        doReturn(false).when(compraRepository).existsById(compraId);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            compraService.deletaCompra(compraId);
        });

        verify(compraRepository, times(1)).existsById(compraId);
        verify(compraRepository, times(0)).deleteById(compraId);
    }

    @Test
    @DisplayName("deletaOrder - Quando o pedido existe, deve deleta")
    public void test04() throws NoSuchElementException {
        long compraId = 1L;
        doReturn(true).when(compraRepository).existsById(compraId);

        compraService.deletaCompra(compraId);

        verify(compraRepository, times(1)).existsById(compraId);
        verify(compraRepository, times(1)).deleteById(compraId);
    }

    @Test
    @DisplayName("findOrders - Deve retornar a lista de pedidos")
    public void test05() {
        List<Compra> orders = Arrays.asList(
                new Compra(1L, null, 4, null, null),
                new Compra(2L, null, 4, null, null)
        );
        doReturn(orders).when(compraRepository).findAll();

        List<Compra> result = compraService.buscaCompras();

        verify(compraRepository, times(1)).findAll();
        Assert.assertEquals(orders.size(), result.size());
    }

    @Test
    @DisplayName("addItems - Quando há itens a serem adicionados deve lançar exceção")
    public void test06() {
        List<Item> items = Collections.emptyList();

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            compraService.adicionaItens(1L, items);
        });

        Assertions.assertEquals(CompraService.EMPTY_ORDER_MESSAGE, ex.getMessage());
        verify(orderItemRepository, times(0)).save(any());
        verify(compraRepository, times(0)).save(any());
    }


    @Test
    @DisplayName("addItems - Quando não há itens a serem adicionados, deve-se adicionar itens")
    public void test07() throws Exception {
        Produto produto = new Produto(1L, "code1", "nome1", "descricao1", 5, 10);
        Compra compra = new Compra(1L, null, 2, new ArrayList<>(), null);
        List<Item> items = Collections.singletonList(
                new Item(1L, compra, produto, 1)
        );
        doReturn(Optional.of(compra)).when(compraRepository).findById(compra.getId());
        doReturn(Optional.of(produto)).when(produtoRepository).findById(produto.getId());
        doReturn(compra).when(compraRepository).save(compra);

        Compra compraFinal = compraService.adicionaItens(compra.getId(), items);

        assertEquals(1, compraFinal.getItems().size());
        verify(orderItemRepository, times(1)).save(items.get(0));
        verify(compraRepository, times(1)).save(compra);
    }

    @Test
    @DisplayName("deletaItems - Quando não há itens a serem removidos deve lançar exceção")
    public void test08() {
        List<Item> items = Collections.emptyList();

        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            compraService.deletaItens(1L, items);
        });

        Assertions.assertEquals(CompraService.EMPTY_ORDER_MESSAGE, ex.getMessage());
        verify(orderItemRepository, times(0)).save(any());
        verify(compraRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("deletaItems - Quando houver itens a serem removidos, deve-se remover os itens")
    public void test09() throws Exception {
        Produto produto = new Produto(1L, "code1", "nome1", "descricao1", 5, 10);
        Compra compra = new Compra(1L, null, 4, new ArrayList<>(), null);
        Item item = new Item(1L, compra, produto, 1);
        compra.addItem(item);

        doReturn(Optional.of(compra)).when(compraRepository).findById(compra.getId());
        doReturn(Optional.of(item)).when(orderItemRepository).findById(item.getId());
        doReturn(compra).when(compraRepository).save(compra);

        Compra result = compraService.deletaItens(compra.getId(),
                Collections.singletonList(item));

        assertEquals(0, result.getItems().size());
        verify(orderItemRepository, times(1)).delete(item);
        verify(compraRepository, times(1)).save(compra);
    }

}