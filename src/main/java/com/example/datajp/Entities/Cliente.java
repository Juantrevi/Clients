package com.example.datajp.Entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clientes")
//Opcional, para darle nombre a la tabla, normalmente va en plural como nomenclatura (tabla en minuscula y en plural)
//Es un estandard de JAVA, las clases van en singular y mayuscula
public class Cliente implements Serializable {
    //Es recomendado implementar Serializable porque muchas veces se trabaja con serializacion, que es el
    //proceso de convertir un objeto en una secuencia de bytes para almacenarlo o transmitirlo a la memoria
    //o a una base de datos, o a un JSON o XML, tambien cuando trabajamos con sesiones HTTP, se requiere
    //serializar
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(name = "NOMBRE_CLIENTE", ) //Esto es si el atributo se llama "nombre", pero en la base de datos se llama "nombre_cliente" por ejemplo
    @NotEmpty(message = "El nombre debe tener entre 4 y 11 caracteres")
    @Size(min = 4, max = 11, message = "")
    private String nombre;
    @NotEmpty(message = "El apellido no puede estar vacio")
    private String apellido;
    @NotEmpty(message = "El email no es valido o no puede estar vacio")
    //@Email
    private String email;

//    @NotNull
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @NotNull(message = "La fecha no puede ser nula")
    //para validar el formato de la fecha y darle un mensaje, en el messages.properties podemos poner typeMismatch.cliente.fechaNuestra = Formato de la fecha no es valido
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaNuestra;

    private String foto;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    //Toda las operaciones ejemplo delete, o persist se hacen en cascada. Por ejemplo cuando al cliente se le asignan varias facturas
    private List<Factura> facturas;

    @PrePersist
    public void prePersist(){
        createAt = new Date();
    }

    public Cliente() {
        facturas = new ArrayList<Factura>();
    }

    public Cliente(Long id, String nombre, String apellido, String email, Date createAt, Date fechaNuestra) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.createAt = createAt;
        this.fechaNuestra = fechaNuestra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getFechaNuestra() {
        return fechaNuestra;
    }

    public void setFechaNuestra(Date fechaNuestra) {
        this.fechaNuestra = fechaNuestra;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public void addFactura(Factura factura){
        facturas.add(factura);
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}