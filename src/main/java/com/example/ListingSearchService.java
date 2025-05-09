package com.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;   

public class ListingSearchService {

    public ObservableList<Listing> search(String keyword) throws IOException, CsvValidationException {
        ObservableList<Listing> data = FXCollections.observableArrayList();
        File csvFile = new File("src/main/resources/csv/listing.csv");
    
        if (!csvFile.exists()) {
            System.out.println("Listing CSV not found.");
            return data;
        }
    
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] row;
            reader.readNext(); //  Izlaižam pirmo rindu, kas ir virsraksts
    
            while ((row = reader.readNext()) != null) {
                if (row.length >= 7) {

                    boolean matches = false;
                    

                    for (int i = 0; i < row.length; i++) {
                        if (i != 0 && i != 3 && i != 4 && i != 5 && row[i].toLowerCase().contains(keyword.toLowerCase())) {
                            matches = true;
                            break;  // Ja tiek atrasta atbilstība, nav nepieciešams pārbaudīt tālākos laukus
                        }
                    }
    
                    if (matches) {

                        // Pievienojam sarakstam, ja tiek atrasta atbilstība
                        data.add(new Listing(
                                row[0].trim(),  // image
                                row[1].trim(),  // title
                                row[2].trim(),  // description
                                row[3].trim(),  // price
                                row[4].trim(),  // category
                                row[5].trim(),  // date
                                row[6].trim()   // location
                        ));
                    }
                }
            }
        }
    
        return data;
    }


    public ObservableList<Listing> searchByCatagories(String keyword) throws IOException, CsvValidationException {
        ObservableList<Listing> data = FXCollections.observableArrayList();
        File csvFile = new File("src/main/resources/csv/listing.csv");
    
        if (!csvFile.exists()) {
            System.out.println("Listing CSV not found.");
            return data;
        }
    
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] row;
            reader.readNext(); // Skip header line
    
            while ((row = reader.readNext()) != null) {
                if (row.length >= 7) {
                    // Manually check fields, skipping the price (index 3) and date (index 5)
                    boolean matches = false;
                    
                    // Loop over all fields except price and date (index 3 and 5)
                    for (int i = 0; i < row.length; i++) {
                        if (i == 4  && row[i].toLowerCase().contains(keyword.toLowerCase())) {
                            matches = true;
                            break;  // If a match is found, no need to check further fields
                        }
                    } 
    
                    if (matches) {
                        // Add listing if a match is found
                        data.add(new Listing(
                                row[0].trim(),  // image
                                row[1].trim(),  // title
                                row[2].trim(),  // description
                                row[3].trim(),  // price
                                row[4].trim(),  // category
                                row[5].trim(),  // date
                                row[6].trim()   // location
                        ));
                    }
                }
            }
        }
    
        return data;
    }

    public ObservableList<Listing> searchByBoth(String keyword, String keyword2) throws IOException, CsvValidationException {
        ObservableList<Listing> data = FXCollections.observableArrayList();
        File csvFile = new File("src/main/resources/csv/listing.csv");
    
        if (!csvFile.exists()) {
            System.out.println("Listing CSV not found.");
            return data;
        }
    
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] row;
            reader.readNext(); // Skip header line
    
            while ((row = reader.readNext()) != null) {
                if (row.length >= 7) {
                    boolean matchesCategory = row[4].toLowerCase().contains(keyword2.toLowerCase());
                    boolean matchesKeyword = row[1].toLowerCase().contains(keyword.toLowerCase()) || 
                                             row[2].toLowerCase().contains(keyword.toLowerCase()) || 
                                             row[6].toLowerCase().contains(keyword.toLowerCase());
    
                    if (matchesCategory && matchesKeyword) {
                        data.add(new Listing(
                                row[0].trim(),  // image
                                row[1].trim(),  // title
                                row[2].trim(),  // description
                                row[3].trim(),  // price
                                row[4].trim(),  // category
                                row[5].trim(),  // date
                                row[6].trim()   // location
                        ));
                    }
                }
            }
        }
    
        return data;
    }
}
