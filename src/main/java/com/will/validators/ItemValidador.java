package com.will.validators;

import com.will.exceptions.NotEnoughStockException;
import com.will.model.Item;
import com.will.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemValidador {

    @Autowired
    private ProdutoRepository produtoRepository;

    public void validateThereIsEnoughStock(List<Item> orderItems) throws Exception {
        for(Item orderItem: orderItems) {
            if(orderItem.getQuantidade() > orderItem.getProduto().getQuantidade()) {
                throw new NotEnoughStockException(orderItem.getProduto().getNome(), orderItem.getProduto().getQuantidade());
            }
        }
    }
}
