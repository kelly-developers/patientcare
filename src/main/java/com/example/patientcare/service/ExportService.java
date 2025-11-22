package com.example.patientcare.service;

import com.example.patientcare.entity.Patient;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    public byte[] exportPatientsToExcel(List<Patient> patients) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Patients");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Patient ID", "First Name", "Last Name", "Date of Birth", "Gender",
                    "Phone", "Email", "Address", "Emergency Contact", "Emergency Phone",
                    "Medical History", "Allergies", "Current Medications",
                    "Research Consent", "Consent Date"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            int rowNum = 1;
            for (Patient patient : patients) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(patient.getPatientId());
                row.createCell(1).setCellValue(patient.getFirstName());
                row.createCell(2).setCellValue(patient.getLastName());
                row.createCell(3).setCellValue(patient.getDateOfBirth().format(dateFormatter));
                row.createCell(4).setCellValue(patient.getGender() != null ? patient.getGender().name() : "");
                row.createCell(5).setCellValue(patient.getPhone() != null ? patient.getPhone() : "");
                row.createCell(6).setCellValue(patient.getEmail() != null ? patient.getEmail() : "");
                row.createCell(7).setCellValue(patient.getAddress() != null ? patient.getAddress() : "");
                row.createCell(8).setCellValue(patient.getEmergencyContactName() != null ? patient.getEmergencyContactName() : "");
                row.createCell(9).setCellValue(patient.getEmergencyContactPhone() != null ? patient.getEmergencyContactPhone() : "");
                row.createCell(10).setCellValue(patient.getMedicalHistory() != null ? patient.getMedicalHistory() : "");
                row.createCell(11).setCellValue(patient.getAllergies() != null ? patient.getAllergies() : "");
                row.createCell(12).setCellValue(patient.getCurrentMedications() != null ? patient.getCurrentMedications() : "");
                row.createCell(13).setCellValue(patient.getResearchConsent() != null ? patient.getResearchConsent() : false);
                row.createCell(14).setCellValue(
                        patient.getResearchConsentDate() != null ?
                                patient.getResearchConsentDate().format(dateTimeFormatter) : ""
                );
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}