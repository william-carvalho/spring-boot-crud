package com.will.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "itens")
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JsonIgnore
    private Compra compra;

    @ManyToOne
    private Produto produto;

    @Column(nullable = false)
    private int quantidade;

    public double getValor() {
        return produto.getValor() * quantidade;
    }

}
