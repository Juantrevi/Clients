package com.example.datajp.view.csv;

import com.example.datajp.Entities.Cliente;
import com.example.datajp.Repository.IClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;


//Para mas ejemplos, porque puede leer y escribir archivos planos, esta la pagina super-csv.github.io/super-csv/index.html
@Component("listar.csv")
public class ClienteCsvView extends AbstractView {
    @Autowired
    private IClienteDao clienteDao;

    public ClienteCsvView() {
        setContentType("text/csv");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    public void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv\"");
        response.setContentType(getContentType());

        //Page<Cliente> clientes = (Page<Cliente>) model.get("clientes"); En el caso de querer imprimir solo la lista de esa pagina

        //Para obtener la lista de todos los clientes.
        List<Cliente> clientes = (List<Cliente>) clienteDao.findAll();
        model.get("clientes");

        ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"id", "nombre", "apellido", "email", "createAt"};
        beanWriter.writeHeader(header);

        for (Cliente cliente: clientes){
            beanWriter.write(cliente,  header);
        }
        beanWriter.close();

    }
}
