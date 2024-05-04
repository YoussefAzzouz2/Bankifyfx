package utils;

import javafx.collections.ObservableList;
import models.CategorieAssurance;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelGenerator {

    public static void generateExcel(ObservableList<CategorieAssurance> data, File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Categorie Data");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nom Categorie");
        headerRow.createCell(2).setCellValue("Description");
        headerRow.createCell(3).setCellValue("Type Couverture");
        headerRow.createCell(4).setCellValue("Agence Responsable");

        // Populate data rows
        for (int i = 0; i < data.size(); i++) {
            CategorieAssurance categorie = data.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(categorie.getIdCategorie());
            row.createCell(1).setCellValue(categorie.getNomCategorie());
            row.createCell(2).setCellValue(categorie.getDescription());
            row.createCell(3).setCellValue(categorie.getTypeCouverture());
            row.createCell(4).setCellValue(categorie.getAgenceResponsable());
        }

        // Write the workbook content to the file
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        workbook.close();
    }
}
