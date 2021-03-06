package com.example.datajp.Repository;

import com.example.datajp.Entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioDao extends JpaRepository<Usuario, Long> {

    public Usuario findByUsername(String username);


}
