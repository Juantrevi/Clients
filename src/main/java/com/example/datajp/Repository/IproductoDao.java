package com.example.datajp.Repository;

import com.example.datajp.Entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IproductoDao extends JpaRepository<Producto, Long> {

//    @Query("SELECT p from Producto p WHERE p.nombre like %?1%")
//    public List<Producto> findByNombre(String term);

//    @Query("select p from Producto p where upper(p.nombre) like upper(concat('%', ?1, '%'))") //Hay que concatenar en el service los % %
    public List<Producto> findByNombreLikeIgnoreCase(String term);

}
