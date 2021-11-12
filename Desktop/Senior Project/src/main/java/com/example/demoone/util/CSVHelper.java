package com.example.demoone.util;

import com.example.demoone.model.Flaw;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    private static final CSVFormat csvFormat = CSVFormat.Builder.create()
            .setHeader(Flaw.HEADERS)
            .setAllowMissingColumnNames(true)
            .build();

    public static List<Flaw> processResults(List<String> results) throws IOException {
        try {
            return csvFormat.parse(new StringReader(String.join("\n", results)))
                    .stream()
                    .filter(record -> !record.get(0).equals(Flaw.FILE))
                    .map(
                            record ->
                            new Flaw(record.get(Flaw.FILE), Integer.parseInt(record.get(Flaw.LEVEL)),
                                    record.get(Flaw.NAME), Integer.parseInt(record.get(Flaw.LINE)))
                    )
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
