package com.will.service;

import com.will.exceptions.EntityAlreadyRegisteredException;
import com.will.model.Produto;
import com.will.repositories.ProdutoRepository;
import com.will.validators.ValidacaoGeral;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProdutoServiceTest {

    @Autowired
    private ProdutoService produtoService;

    @MockBean
    private ProdutoRepository produtoRepository;

    @MockBean
    private ValidacaoGeral validacaoGeral;

    @Test
    @DisplayName("criarProduto - Quando a unidade de armazenamento não estiver registrado deve salvar o produto")
    public void test01() throws Exception {
        Produto produto = new Produto(1L, "code", "nome", "descricao", 5, 20);

        produtoService.criarProduto(produto);

        verify(produtoRepository, times(1)).existsByUnidadeDeArmazenamento(produto.getUnidadeDeArmazenamento());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    @DisplayName("criarProduto - Quando a unidade de armazenamento é registrado deve lançar exceção")
    public void test02() throws EntityAlreadyRegisteredException {
        Produto produto = new Produto(1L, "code", "nome", "descricao", 5, 20);
        doReturn(true).when(produtoRepository).existsByUnidadeDeArmazenamento(produto.getUnidadeDeArmazenamento());

        Assertions.assertThrows(EntityAlreadyRegisteredException.class, () -> {
            produtoService.criarProduto(produto);
        });

        verify(produtoRepository, times(0)).save(produto);
    }

    @Test
    @DisplayName("deletaProduto - Quando o produto existe deve deletar")
    public void test03() throws NoSuchElementException {
        long produtoId = 1L;
        doReturn(true).when(produtoRepository).existsById(produtoId);

        produtoService.deletaProduto(produtoId);

        verify(produtoRepository, times(1)).existsById(produtoId);
        verify(produtoRepository, times(1)).deleteById(produtoId);
    }

    @Test
    @DisplayName("deletaProduto - Quando o produto não existe, deve lançar NoSuchElementException")
    public void test04() throws NoSuchElementException {
        long produtoId = 1L;
        doReturn(false).when(produtoRepository).existsById(produtoId);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            produtoService.deletaProduto(produtoId);
        });

        verify(produtoRepository, times(1)).existsById(produtoId);
        verify(produtoRepository, times(0)).deleteById(produtoId);
    }

    @Test
    @DisplayName("findProdutos - Deve retornar a lista de produtos")
    public void test05() {
        List<Produto> produtos = Arrays.asList(
                new Produto(1L, "code", "nome", "descricao2", 5.50, 15),
                new Produto(2L, "code22", "nome3", "descricao4", 3.50, 10)
        );
        doReturn(produtos).when(produtoRepository).findAll();

        List<Produto> result = produtoService.findProdutos();

        verify(produtoRepository, times(1)).findAll();
        assertEquals(produtos.size(), result.size());
    }

    @Test
    @DisplayName("findProduto - Quando o produto não existe, não deve ocorrer SuchElementException")
    public void test06() {
        doReturn(Optional.empty()).when(produtoRepository).findByUnidadeDeArmazenamento(any());
        String sku = "sku";

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            produtoService.findProduto(sku);
        });

        verify(produtoRepository, times(1)).findByUnidadeDeArmazenamento(sku);
    }

    @Test
    @DisplayName("findProduto - Quando o produto existir, devemos encontrar o produto")
    public void test07() {
        Produto produto = new Produto(1L, "code22", "nome3", "descricao22", 4.50, 15);
        doReturn(Optional.of(produto)).when(produtoRepository).findByUnidadeDeArmazenamento(produto.getUnidadeDeArmazenamento());

        Produto produtoFinal = produtoService.findProduto(produto.getUnidadeDeArmazenamento());

        verify(produtoRepository, times(1)).findByUnidadeDeArmazenamento(produto.getUnidadeDeArmazenamento());
        assertEquals(produto.getId(), produtoFinal.getId());
    }

    @Test
    @DisplayName("editaProduto - Quando o produto não existe, deve lançar NoSuchElementException")
    public void test08() {
        long produtoId = 1L;
        Produto produto = new Produto(produtoId, "code22", "nome3", "descricao22", 6.50, 12);
        doReturn(Optional.empty()).when(produtoRepository).findById(produtoId);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            produtoService.editaProduto(produto);
        });

        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, times(0)).save(produto);
    }

    @Test
    @DisplayName("editaProduto - Quando o produto não foi modificado, não deve salvar o produto")
    public void test09() {
        Produto produto = new Produto(1L, "code22", "nome3", "descricao22", 7.50, 15);
        doReturn(Optional.of(produto)).when(produtoRepository).findById(produto.getId());

        produtoService.editaProduto(produto);

        verify(produtoRepository, times(1)).findById(produto.getId());
        verify(produtoRepository, times(0)).save(produto);
    }

    @Test
    @DisplayName("editaProduto - Quando o produto é modificado deve salvar o produto com novas informações")
    public void test10() {
        Produto produto = new Produto(1L, "code22", "nome3", "descricao22", 3.50, 20);
        Produto alteredProduto = new Produto(1L, "code222", "nome33", "descricao2222", 4.50, 22);
        doReturn(Optional.of(produto)).when(produtoRepository).findById(produto.getId());

        produtoService.editaProduto(alteredProduto);

        verify(produtoRepository, times(1)).findById(produto.getId());
        verify(produtoRepository, times(1)).save(produto);
    }
}