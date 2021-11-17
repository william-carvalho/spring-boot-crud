package com.will.exceptions;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(String produtoName, int amountAvailable) {
        super("Não há estoque suficiente para o produto " + produtoName + ". O estoque atual deste" +
                "produto é " + amountAvailable);
    }
}
