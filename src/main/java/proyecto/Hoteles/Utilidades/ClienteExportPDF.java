package proyecto.Hoteles.Utilidades;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import proyecto.Hoteles.Entidades.Cliente;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Date;

public class ClienteExportPDF {

    private List<Cliente> listaClientes;

    public ClienteExportPDF(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    private void setCabeceraTabla(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell();


        celda.setBackgroundColor(new Color(0, 121, 191));
        celda.setPadding(10);
        celda.setBorderWidth(1);
        celda.setBorderColor(new Color(0, 0, 0, 0.1f));

        Font fuenteCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        fuenteCabecera.setColor(Color.WHITE);

        celda.setPhrase(new Phrase("ID", fuenteCabecera));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Nombre", fuenteCabecera));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Apellido", fuenteCabecera));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Correo Electrónico", fuenteCabecera));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Teléfono", fuenteCabecera));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Fecha de Registro", fuenteCabecera));
        tabla.addCell(celda);
    }

    private void setDatosTabla(PdfPTable tabla) {
        Font fuenteCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fuenteCuerpo.setColor(Color.DARK_GRAY);

        for (Cliente cliente : listaClientes) {
            PdfPCell celda = new PdfPCell(new Phrase(String.valueOf(cliente.getId()), fuenteCuerpo));
            celda.setPadding(8);
            celda.setBorderColor(new Color(200, 200, 200));
            celda.setBackgroundColor(new Color(245, 245, 245));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(cliente.getNombre(), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(cliente.getApellido(), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(cliente.getCorreoElectronico(), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(cliente.getTelefono(), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(cliente.getFechaRegistro().toString(), fuenteCuerpo));
            tabla.addCell(celda);
        }
    }

    public void Exportar(HttpServletResponse response) throws IOException {
        Document documento = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(documento, response.getOutputStream());
        documento.open();

        // Cargar el logo y añadirlo al documento
        try {
            InputStream inputStream = getClass().getResourceAsStream("/static/dist/assets/images/faces/Logo.png");
            if (inputStream != null) {
                Image logo = Image.getInstance(ImageIO.read(inputStream), null);
                logo.scaleToFit(100, 50);
                logo.setAlignment(Image.ALIGN_LEFT);
                documento.add(logo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Título del documento
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.BLACK);
        Paragraph titulo = new Paragraph("Lista de Clientes", fuenteTitulo);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        titulo.setSpacingBefore(20);
        titulo.setSpacingAfter(30);
        documento.add(titulo);

        // Tabla con datos de clientes
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(20);
        tabla.setWidths(new float[]{1f, 2f, 2f, 3f, 2f, 2f});
        setCabeceraTabla(tabla);
        setDatosTabla(tabla);
        documento.add(tabla);

        // Pie de página estilizado
        Font fuentePie = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, new Color(100, 100, 100));
        Paragraph pie = new Paragraph("Documento generado el: " + new Date().toString(), fuentePie);
        pie.setAlignment(Paragraph.ALIGN_RIGHT);
        pie.setSpacingBefore(30);
        documento.add(pie);

        // Dibujar una línea sutil sobre el pie de página
        PdfContentByte canvas = writer.getDirectContent();
        canvas.setColorStroke(new Color(200, 200, 200));
        canvas.moveTo(36, 50);
        canvas.lineTo(documento.getPageSize().getWidth() - 36, 50);
        canvas.stroke();

        documento.close();
    }
}
