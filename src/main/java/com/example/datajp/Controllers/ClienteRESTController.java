package com.example.datajp.Controllers;

import com.example.datajp.Entities.Cliente;
import com.example.datajp.Services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //Combina controller con respondebody
@RequestMapping("/api/clientes")
public class ClienteRESTController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/listar")
    public List<Cliente> listarREST() {

        return clienteService.findAll();
    }


}
