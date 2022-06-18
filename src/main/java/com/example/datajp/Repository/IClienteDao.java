package com.example.datajp.Repository;

import com.example.datajp.Entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

    //Podriamos tener otros metodos hechos por nosotros con query
//
//    public List<Cliente> findAll();
//
//    public void save(Cliente cliente);
//
//    public Cliente findOne(Long id);
//
//    public void delete(Long id);
}
