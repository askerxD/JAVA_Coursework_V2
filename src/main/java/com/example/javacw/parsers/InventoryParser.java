package com.example.javacw.parsers;

import com.example.javacw.objects.Part;
import com.example.javacw.utils.ValidationUtil;
import java.io.*;
import java.util.ArrayList;

public class InventoryParser {
    public static ArrayList<Part> parseInventoryFile(String filePath) {
        ArrayList<Part> parts = new ArrayList<>();
        try (BufferedReader file = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = file.readLine()) != null) {
                // skip the empty lines
                if (line.trim().isEmpty()){
                    continue;
                }

                // change delimeters to commas
                line = line.replace("|", ",")
                           .replace(";", ",");
                String[] words = line.split(",");
                // safe extraction with fallback values
                String partCode = get(words, 0);
                String name = get(words, 1);
                String brand = get(words, 2);
                String priceStr = get(words, 3);
                String qtyStr = get(words, 4);
                String category = get(words, 5);
                String date = get(words, 6);
                String image = get(words, 7);

                // validation
                if (!ValidationUtil.isValidPartCode(partCode)) {
                    continue; // skip invalid records
                }
                double price = ValidationUtil.parsePrice(priceStr);
                int qty = ValidationUtil.parseQuantity(qtyStr);

                category = ValidationUtil.normalizeCategory(category);

                Part part = new Part(
                        partCode,
                        ValidationUtil.safeString(name),
                        ValidationUtil.safeString(brand),
                        price,
                        qty,
                        category,
                        ValidationUtil.safeString(date),
                        ValidationUtil.safeString(image)
                );
                parts.add(part);
            }
        } catch (IOException e) {
            System.out.println("Error reading inventory file: " + e.getMessage());
        }
        return parts;
    }
    private static String get(String[] arr, int index) {
        if (index >= arr.length){
            return "";
        }else{
        return arr[index].trim();
        }
    }
}