package com.example.datajp.Controllers;


import com.example.datajp.Entities.Cliente;
import com.example.datajp.Services.IClienteService;
import com.example.datajp.Services.IUploadFileService;
import com.example.datajp.util.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;



@Controller
@SessionAttributes("cliente") //Con esto, se indica que se guarda en los atributos de la sesion el objeto cliente cada vez que se invoca el crear o editar.
                                //Haciendo esto se evita poner como hidden el id en el html, pero hay que eliminar la sesion en el controlador guardar con SessionStatus
public class ClienteController {

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private IUploadFileService uploadFileService;



    //Pageable se usa para mostrar una cantidad de registros determinados (Modificar Interfaz y service) y en otro controlador (util.paginator)

    @GetMapping("/listar")
    public String listar(@RequestParam(value = "page", defaultValue = "0") int page, Model model){

        //Asi es como traemos una cantidad de elementos por pagina
        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
        //


        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes); //Para traer solo 4
//        model.addAttribute("clientes", clienteService.findAll()); --> SI QUISIERAMOS TRAER TODA LA LISTA
        model.addAttribute("page", pageRender);
        return "listar";
    }



    @GetMapping("/form")
    public String crear(Model model){

        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Formulario de cliente");
        model.addAttribute("opcion", "Crear Cliente");

        return "form";
    }



    //FUNDAMENTAL PARA LAS VALIDACIONES anotar con @Valid, pero si falla hay que preguntar si contiene errores
    //Hay que cargar la vista del formulario y volver a la plantilla con los mensajes de error, para eso hay que
    //agregar otro objeto como argumento, el BindingResult, siempre junto al objeto (En este caso el cliente)
    @PostMapping("/form")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status){

        if (result.hasErrors()){
            //En este caso, al cliente lo pasa en forma automatica siempre y cuando el tipo de la clase se llame igual al nombre con el cual lo pasamos a la vista
            model.addAttribute("titulo", "Error cliente");
            model.addAttribute("opcion", "Crear Cliente");
            return "form";
        }

        if (!foto.isEmpty()){

            if (cliente.getId() != null &&
                    cliente.getId() >=0 &&
                    cliente.getFoto() != null &&
                    cliente.getFoto().length() > 0){

                uploadFileService.delete(cliente.getFoto());
                }

            String uniqueFileName = null;
            try {
                uniqueFileName = uploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }


            flash.addFlashAttribute("info", "Ha subido la foto correctamente");
            cliente.setFoto(uniqueFileName);
        }

        String mensajeFlash = (cliente.getId() != null)?"Cliente editado con exito" : "Cliente creado con exito";
        clienteService.save(cliente);
        flash.addFlashAttribute("succes", mensajeFlash);
        status.setComplete();
        return "redirect:listar";
    }



    @GetMapping("/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash){

        Cliente cliente = null;
        if (id > 0){
            cliente = clienteService.findOne(id);
            if (cliente == null){
                flash.addFlashAttribute("error", "El id del cliente no existe en la base de datos");
                return "redirect:/listar";
            }
        }else {
            flash.addFlashAttribute("error", "El id del cliente no puede ser 0");
            return "redirect:/listar";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar cliente: " + cliente.getNombre());
        model.addAttribute("opcion", "Editar");

        return "form";
    }



    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash){

        if (id > 0){
            Cliente cliente = clienteService.findOne(id);
            clienteService.delete(id);
            flash.addFlashAttribute("succes", "Cliente eliminado con exito");


                if (uploadFileService.delete(cliente.getFoto())){
                    flash.addFlashAttribute("info", "Foto eliminada con exito");
                }
            }

        return "redirect:/listar";
    }



    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash){

        Cliente cliente = clienteService.findOne(id);
        if (cliente == null){
            flash.addFlashAttribute("error", "El cliente no existe");
            return "redirect:/listar";
        }
        model.addAttribute("titulo", "Detalle de cliente " + cliente.getNombre());
        model.addAttribute("cliente", cliente);

        return "ver";

    }




    //De esta forma se puede cargar la imagen de forma mas programatica (Si vemos la url esta, es la que nos manda desde el HTML listar)
    //de la otra manera lo carga como un recurso, pero hay que configurarlo en el MvcConfig (Ahora comentado)
    //Es decir, de esta forma lo busca directamente a traves de la respuesta resource, se cargan los recursos y se pasan a la respuesta, anexandolo al body
    //Ambas formas estan bien, esta solo la carga a traves de una metodo handler
    @GetMapping(value = "/uploads/{filename:.+}") //el .+ al final es para que no pase el .PNG por ejemplo
    public ResponseEntity<Resource> verFoto(@PathVariable String filename){

        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"" ).body(recurso);
    }

}
