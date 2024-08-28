package proyecto.Hoteles.Utilidades;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.Hoteles.Entidades.Reserva;
import proyecto.Hoteles.Entidades.Cliente;
import proyecto.Hoteles.Servicios.Interfaces.IReservaServices;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
public class FacturaExportPDF {

    private final IReservaServices reservaServices;

    @Autowired
    public FacturaExportPDF(IReservaServices reservaServices) {
        this.reservaServices = reservaServices;
    }

    public void exportarFactura(HttpServletResponse response, Integer reservaId) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaActual = dateFormatter.format(java.time.LocalDateTime.now());

        String cabecera = "Content-Disposition";
        String valor = "inline; filename=Factura_" + fechaActual + ".pdf";
        response.setHeader(cabecera, valor);

        // Obtener la reserva por el ID de la reserva
        Reserva reserva = reservaServices.BuscarPorId(reservaId)
                .orElseThrow(() -> new RuntimeException("No se encontró la reserva seleccionada."));

        // Crear el documento PDF en tamaño A5
        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Información del cliente
        Cliente cliente = reserva.getCliente();
        Font fontTitle = new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLACK);
        Font fontHeader = new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK);
        Font fontNormal = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);

        // Título de la factura
        Paragraph title = new Paragraph("Factura", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        // Información básica de la factura
        document.add(new Paragraph("Cliente: " + cliente.getNombre() + " " + cliente.getApellido(), fontNormal));
        document.add(new Paragraph("Email: " + cliente.getCorreoElectronico(), fontNormal));
        document.add(new Paragraph("Teléfono: " + cliente.getTelefono(), fontNormal));
        document.add(new Paragraph("Fecha de emisión: " + fechaActual, fontNormal));
        document.add(new Paragraph("\n"));

        // Crear la tabla con los detalles de la reserva
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{3f, 2f, 2f, 2f}); // Ajustar el tamaño de las columnas

        // Encabezados de la tabla
        String[] headers = {"Habitación", "Fecha de Entrada", "Fecha de Salida", "Monto Total"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, fontHeader));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBackgroundColor(Color.LIGHT_GRAY);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        // Agregar los datos de la reserva
        table.addCell(new Phrase(reserva.getHabitacion().getTipoHabitacion() + " - $" + decimalFormat.format(reserva.getHabitacion().getPrecioPorHora()) + "/hora", fontNormal));
        table.addCell(new Phrase(reserva.getFechaEntrada().format(dateFormatter), fontNormal));
        table.addCell(new Phrase(reserva.getFechaSalida().format(dateFormatter), fontNormal));
        table.addCell(new Phrase("$" + decimalFormat.format(reserva.getMontoTotal()), fontNormal));

        document.add(table);
        document.add(new Paragraph("\n"));


        Paragraph gracias = new Paragraph("Gracias por su preferencia.", fontTitle);
        gracias.setAlignment(Element.ALIGN_CENTER);
        document.add(gracias);

        document.close();
    }
}
