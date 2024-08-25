package proyecto.Hoteles.Utilidades;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import proyecto.Hoteles.Entidades.Empleado;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class EmpleadoExportPDF {
    private List<Empleado> listaEmpleados;

    public EmpleadoExportPDF(List<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    private void setCabeceraTabla(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell();

        // Configuración del estilo de la celda de cabecera
        celda.setBackgroundColor(new Color(63, 81, 181)); // Azul moderno
        celda.setPadding(10);
        celda.setBorderWidth(1.5f);
        celda.setBorderColor(new Color(230, 230, 230)); // Gris claro

        // Fuente elegante para la cabecera
        Font fuenteCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);

        // Configurar las columnas de la cabecera
        String[] titulos = {"Id", "Nombre", "Apellido", "Email", "Teléfono", "Salario", "Horario"};
        for (String titulo : titulos) {
            celda.setPhrase(new Phrase(titulo, fuenteCabecera));
            tabla.addCell(celda);
        }
    }

    private void setDatosTabla(PdfPTable tabla) {
        Font fuenteCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);

        for (Empleado empleado : listaEmpleados) {
            // Estilo para las celdas de datos
            PdfPCell celda = new PdfPCell();
            celda.setPadding(8);
            celda.setBorderColor(new Color(200, 200, 200)); // Gris claro
            celda.setBackgroundColor(new Color(245, 245, 245)); // Fondo sutil

            // Añadir los datos del empleado
            celda.setPhrase(new Phrase(String.valueOf(empleado.getId()), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(empleado.getNombre(), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(empleado.getApellido(), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(empleado.getEmail(), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(empleado.getTelefono(), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(String.format("$%.2f", empleado.getSalario()), fuenteCuerpo));
            tabla.addCell(celda);

            celda.setPhrase(new Phrase(empleado.getHorario(), fuenteCuerpo));
            tabla.addCell(celda);
        }
    }

    public void Exportar(HttpServletResponse response) throws IOException {
        Document documento = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(documento, response.getOutputStream());
        documento.open();

        // Agregar logo
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

        // Título estilizado
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLACK);
        Paragraph titulo = new Paragraph("Lista de Empleados", fuenteTitulo);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        titulo.setSpacingBefore(20);
        titulo.setSpacingAfter(30);
        documento.add(titulo);

        // Tabla con datos
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(20);
        tabla.setWidths(new float[]{1f, 2f, 2f, 3f, 2f, 2f, 2f});
        setCabeceraTabla(tabla);
        setDatosTabla(tabla);
        documento.add(tabla);

        // Pie de página elegante
        Font fuentePie = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY);
        Paragraph pie = new Paragraph("Documento generado el: " + new Date().toString(), fuentePie);
        pie.setAlignment(Paragraph.ALIGN_RIGHT);
        pie.setSpacingBefore(30);
        documento.add(pie);

        // Línea de diseño
        PdfContentByte canvas = writer.getDirectContent();
        canvas.setColorStroke(new Color(180, 180, 180));
        canvas.moveTo(36, 50);
        canvas.lineTo(documento.getPageSize().getWidth() - 36, 50);
        canvas.stroke();

        documento.close();
    }
}
