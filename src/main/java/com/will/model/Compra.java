package com.will.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compras")
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false)
    private LocalDate dataCompra;

    @Column(nullable = false)
    private double valor = 0;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    @ManyToOne
    private Cliente cliente;

    public void defineValor() {
        double valor = 0;
        for(Item item : items) {
            valor += item.getValor();
        }
        this.valor = valor;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}
