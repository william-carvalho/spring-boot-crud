package com.will.service;

import com.will.model.Cliente;
import com.will.model.Compra;
import com.will.model.Item;
import com.will.model.Produto;
import com.will.repositories.ClienteRepository;
import com.will.repositories.CompraRepository;
import com.will.repositories.ItemRepository;
import com.will.repositories.ProdutoRepository;
import com.will.validators.ItemValidador;
import com.will.validators.ValidacaoGeral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CompraService {

    public static final String NOT_FOUND_MESSAGE = "Pedido não encontrado";
    public static final String EMPTY_ORDER_MESSAGE = "O pedido deve ter pelo menos um item";


    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemValidador itemValidador;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ValidacaoGeral validacaoGeral;

    public Compra criarCompra(Compra compra) throws Exception {
        validacaoGeral.validateIdZeroOrNull(compra.getId());
        if (compra.getItems().size() <= 0) throw new Exception(EMPTY_ORDER_MESSAGE);

        insereProdutoNaCompra(compra.getItems());
        itemValidador.validateThereIsEnoughStock(compra.getItems());

        insereClienteNaCompra(compra);
        compra.getItems().forEach(item -> {
            item.setCompra(compra);
        });

        compra.setDataCompra(LocalDate.now());
        compra.defineValor();
        diminuiQuantidade(compra.getItems());
        return compraRepository.save(compra);
    }

    private void insereClienteNaCompra(Compra compra) throws Exception {
        long clientId = compra.getCliente().getId();
        Cliente client = clienteRepository.findById(clientId)
                .orElseThrow(() -> new Exception("Cliente  " + clientId + " não existe"));
        compra.setCliente(client);

    }

    private void insereProdutoNaCompra(List<Item> items) throws Exception {
        for (Item item : items) {
            long produtoId = item.getProduto().getId();
            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new Exception("Produto " + produtoId + "não existe"));
            item.setProduto(produto);
        }
    }

    private void diminuiQuantidade(List<Item> items) {
        for (Item item : items) {
            Produto produto = item.getProduto();
            produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
            produtoRepository.save(produto);
        }
    }

    public void deletaCompra(long id) {
        if (compraRepository.existsById(id))
            compraRepository.deleteById(id);
        else
            throw new NoSuchElementException(NOT_FOUND_MESSAGE);
    }

    public List<Compra> buscaCompras() {
        return compraRepository.findAll();
    }

    public Compra editaDataCompra(Compra compra) {
        Compra compraPersist = compraRepository.findById(compra.getId())
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));

        boolean modified = false;
        if (!compra.getDataCompra().isEqual(compraPersist.getDataCompra())) {
            modified = true;
            compraPersist.setDataCompra(compra.getDataCompra());
        }

        return modified ? compraRepository.save(compra) : compraPersist;
    }

    public Compra adicionaItens(long compraId, List<Item> items) throws Exception {
        if (items.size() <= 0) throw new Exception(EMPTY_ORDER_MESSAGE);
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));
        insereProdutoNaCompra(items);
        itemValidador.validateThereIsEnoughStock(items);
        items.forEach(item -> {
            item.setCompra(compra);
            compra.addItem(item);
            itemRepository.save(item);
        });
        compra.defineValor();
        return compraRepository.save(compra);
    }

    public Compra deletaItens(long compraId, List<Item> items) throws Exception {
        if (items.size() <= 0) throw new Exception(EMPTY_ORDER_MESSAGE);
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));
        for (Item item : items) {
            Item itemPersist = itemRepository.findById(item.getId())
                    .orElseThrow(() -> new Exception("Item " + item.getProduto().getNome() + " não existe"));
            compra.removeItem(itemPersist);
            itemRepository.delete(itemPersist);
        }
        compra.defineValor();
        return compraRepository.save(compra);
    }

}
