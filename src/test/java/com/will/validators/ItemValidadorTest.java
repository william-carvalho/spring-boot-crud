package com.will.validators;

import com.will.exceptions.NotEnoughStockException;
import com.will.model.Compra;
import com.will.model.Item;
import com.will.model.Produto;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemValidadorTest {

    @Autowired
    private ItemValidador itemValidador;

    private Compra compra = new Compra();
    private Produto produto1 = new Produto(1L, "code1", "nome1", "descricao1", 3.50, 9);
    private Produto produto2 = new Produto(2L, "cod2", "nome2", "descricao2", 4, 5);

    @Test
    @DisplayName("validarSeHaEstoqueSuficiente - Quando nenhum dos itens exceder a quantidade em estoque " +
            "não deveria lançar exceção")
    public void test01() throws Exception {
        List<Item> orderItems = Arrays.asList(
                new Item(1L, compra, produto1, 1),
                new Item(2L, compra, produto2, 1)
        );

        itemValidador.validateThereIsEnoughStock(orderItems);
    }

    @Test
    @DisplayName("validarSeHaEstoqueSuficiente - Quando algum dos itens exceder a quantidade em estoque " +
            "não deveria lançar exceção")
    public void test02() {
        List<Item> orderItems = Arrays.asList(
                new Item(1L, compra, produto1, 1),
                new Item(2L, compra, produto2, 3)
        );

        assertThrows(NotEnoughStockException.class, () -> {
            itemValidador.validateThereIsEnoughStock(orderItems);
        });
    }

}