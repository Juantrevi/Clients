package com.example.datajp.view.xlsx;

import com.example.datajp.Entities.Factura;
import com.example.datajp.Entities.ItemFactura;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//Pagina para ejemplos, poi.apache.org/spreadsheet/examples.html

@Component("factura/ver.xlsx")
public class FacturaXlxsView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Factura factura = (Factura) model.get("factura");
        response.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");
        Sheet sheet = workbook.createSheet("Factura Spring");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Datos del cliente");
        //Una forma es crear el objeto row y el objeto celda o la otra es como esta en la linea 36
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getEmail());

        //Una forma de hacerlo mas rapido en una sola linea(Podemos hacer lo mismo para los datos del cliente) encadenando metodos
        sheet.createRow(4).createCell(0).setCellValue("Datos de la factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripcion: " + factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: " + factura.getCreateAt());

        CellStyle tHeaderStyle = workbook.createCellStyle();
        tHeaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        tHeaderStyle.setBorderTop(BorderStyle.MEDIUM);
        tHeaderStyle.setBorderRight(BorderStyle.MEDIUM);
        tHeaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        tHeaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
        tHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle tBodyStyle = workbook.createCellStyle();
        tBodyStyle.setBorderBottom(BorderStyle.THIN);
        tBodyStyle.setBorderTop(BorderStyle.THIN);
        tBodyStyle.setBorderRight(BorderStyle.THIN);
        tBodyStyle.setBorderLeft(BorderStyle.THIN);


        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");
        header.getCell(0).setCellStyle(tHeaderStyle);
        header.getCell(1).setCellStyle(tHeaderStyle);
        header.getCell(2).setCellStyle(tHeaderStyle);
        header.getCell(3).setCellStyle(tHeaderStyle);

        int i = 10; //Porque ya tenemos ocupadas las celdas anteriores, por eso partimos desde la 10
        for (ItemFactura item: factura.getItems()){
            Row fila = sheet.createRow(i++);
            cell = fila.createCell(0);
            cell.setCellValue(item.getProducto().getNombre());
            cell.setCellStyle(tBodyStyle);

            cell = fila.createCell(1);
            cell.setCellValue(item.getProducto().getPrecio());
            cell.setCellStyle(tBodyStyle);

            cell = fila.createCell(2);
            cell.setCellValue(item.getCantidad());
            cell.setCellStyle(tBodyStyle);

            cell = fila.createCell(3);
            cell.setCellValue(item.calcularImporte());
            cell.setCellStyle(tBodyStyle);


        }

        Row filatotal = sheet.createRow(i);
        cell = filatotal.createCell(2);
        cell.setCellValue("Gran Total: ");
        cell.setCellStyle(tBodyStyle);

        cell = filatotal.createCell(3);
        cell.setCellValue(factura.getTotal());
        cell.setCellStyle(tBodyStyle);
    }
}
