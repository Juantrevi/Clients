package com.example.datajp.Controllers;

import com.example.datajp.Entities.Cliente;
import com.example.datajp.Entities.Factura;
import com.example.datajp.Entities.ItemFactura;
import com.example.datajp.Entities.Producto;
import com.example.datajp.Services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
@Secured("ROLE_ADMIN")
public class FacturaController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Model model, RedirectAttributes flash) {

        Cliente cliente = clienteService.findOne(clienteId);

        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe");
            return "redirect:/listar";
        }

        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Crear Factura");

        return "factura/form";
    }

    @GetMapping("/ver/{id}")
    public String verFactura(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Factura factura = clienteService.fetchByIdWithClienteWithItemFacturaWithProducto(id);//clienteService.findFacturaById(id);

        if (factura == null) {
            flash.addFlashAttribute("error", "La factura no existe en la base de datos");
            return "redirect:/listar";
        }
        model.addAttribute("titulo", "Factura: " + factura.getDescripcion());
        model.addAttribute("factura", factura);
        return "factura/ver";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    //Mismo mapping que le dimos en /js/autocomplete-producto dentro de ajax,
    // con Jquery. Despues lo que hace
    //Es generar y productir una respuesta JSON.
    //El @ResponseBody lo que hace es suprimir el cargar una
    //vista de Thymeleaf, y en vez de eso toma el resultado y lo convierte en JSON
    public @ResponseBody
    List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findByNombreLikeIgnoreCase(term);
    }

    @PostMapping("/form")
    public String guardar(@Valid Factura factura, BindingResult result, Model model,
                          @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                          @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                          RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Crear Factura");
            return "factura/form";
        }
        if (itemId == null || itemId.length == 0) {
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", "Error: La factura NO puede no tener lineas!");
            return "factura/form";
        }


        for (int i = 0; i < itemId.length; i++) {
            Producto producto = clienteService.findProductoById(itemId[i]);

            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);
        }

        clienteService.saveFactura(factura);

        status.setComplete();
        flash.addFlashAttribute("succes", "Factra creada con exito");

        return "redirect:/ver/" + factura.getCliente().getId().toString();
    }

    @GetMapping("eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {

        Factura factura = clienteService.findFacturaById(id);
        if (factura != null) {
            clienteService.deleteFactura(id);
            flash.addFlashAttribute("succes", "Factura eliminada con exito");
            return "redirect:/ver/" + factura.getCliente().getId();
        }
        flash.addFlashAttribute("error", "La factura no existe en la BD");
        return "redirect:/listar";
    }


}
