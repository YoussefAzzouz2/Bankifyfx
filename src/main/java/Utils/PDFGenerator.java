package Utils;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.paint.Color;

public class PDFGenerator {

    public void generatePDF(ActionEvent event, TableView<?> tableView, String title) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            // Draw table title "CompteClient Table" in blue
            contentStream.setNonStrokingColor((float) Color.BLUE.getRed(), (float) Color.BLUE.getGreen(), (float) Color.BLUE.getBlue());
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(200, 750);
            contentStream.showText(title);  // Use the provided title
            contentStream.endText();

            // Draw table headers in black
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(25, 700);

            // Add headers
            for (TableColumn<?, ?> column : tableView.getColumns()) {
                contentStream.showText(column.getText());
                contentStream.newLineAtOffset(100, 0);  // Fixed offset for headers
            }

            contentStream.newLineAtOffset(-100 * tableView.getColumns().size(), -20); // Adjust Y offset

            // Set font size for table data (even smaller)
            contentStream.setFont(PDType1Font.HELVETICA, 6); // Set the font size to 6

            // Add table data
            for (Object item : tableView.getItems()) {
                for (TableColumn<?, ?> column : tableView.getColumns()) {
                    TableColumn<Object, ?> col = (TableColumn<Object, ?>) column;
                    String cellData = col.getCellData(item) != null ? col.getCellData(item).toString() : "";
                    contentStream.showText(cellData);
                    contentStream.newLineAtOffset(100, 0); // Fixed offset for data cells
                }
                contentStream.newLineAtOffset(-100 * tableView.getColumns().size(), -20); // Adjust Y offset
            }

            contentStream.endText();

            // Add date and time below the table
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.newLineAtOffset(200, 50);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            contentStream.showText("Date and Time: " + formattedDateTime);
            contentStream.endText();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Show alert after successful download
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("PDF Downloaded");
        alert.setHeaderText(null);
        alert.setContentText("PDF downloaded successfully!");
        alert.showAndWait();

        try {
            document.save(title + ".pdf");  // Use the provided title for filename
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
