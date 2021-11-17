package com.will.model;

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
@Table(name = "produtos")
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false, unique = true, length = 50)
    private String unidadeDeArmazenamento;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 300)
    private String descricao;

    @Column(name = "valor", nullable = false)
    private double valor;

    @Column(nullable = false)
    private int quantidade;

}
