package com.jasinskipawel.personal_budget_api.transaction;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvTransactionExporter {

    public byte[] exportToCsv(List<Transaction> transactions) {

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append('\uFEFF');
        csvBuilder.append("ID;Kwota;Typ;Kategoria;Opis;Data\n");

        for (Transaction t : transactions) {
            csvBuilder.append(t.getId()).append(";")
                    .append(t.getAmount()).append(";")
                    .append(t.getType()).append(";")
                    .append(escapeCsv(t.getCategory())).append(";")
                    .append(escapeCsv(t.getDescription())).append(";")
                    .append(t.getDate()).append("\n");
        }

        return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsv(String data) {
        if (data == null) {
            return "";
        }

        if (data.contains(";") || data.contains("\"") || data.contains("\n") || data.contains("\r")) {
            data = data.replace("\"", "\"\"");
            return "\"" + data + "\"";
        }
        return data;
    }
}
