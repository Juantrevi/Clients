package com.example.datajp.Repository;

import com.example.datajp.Entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFacturaDao extends JpaRepository<Factura, Long> {


}
