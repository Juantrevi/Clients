package com.example.datajp.Entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Integer cantidad;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id") //Aunque se va a relacionar solo, se lo puede dejar de forma explicita, pero se podria omitir
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Producto producto;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double calcularImporte(){
        return cantidad.doubleValue() * producto.getPrecio();
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
