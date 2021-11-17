package com.will.model;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompraItemTest {

    @Test
    @DisplayName("getValor - Deve retornar o preço de um OrderItem")
    public void test01() {
        Produto produto = new Produto(1L, "code", "nome", "descricao", 5, 10);
        Item item = new Item();
        item.setProduto(produto);
        item.setQuantidade(2);

        double expected = produto.getValor() * item.getQuantidade();
        double result = item.getValor();

        assertEquals(expected, result);
    }

}