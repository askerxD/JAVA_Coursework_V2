package com.example.javacw.service;

import com.example.javacw.objects.Part;
import java.util.ArrayList;

public class SearchService {
    /**
     * Multi-criteria search
     *
     * Any filter can be left empty/null to ignore it.
     */
    public ArrayList<Part> searchParts(
            ArrayList<Part> parts,
            String category,
            String keyword,
            Double minPrice,
            Double maxPrice) {
        ArrayList<Part> results = new ArrayList<>();
        for (Part part : parts) {
            boolean matches = true;
            // Category filter
            if (category != null &&
                    !category.trim().isEmpty()) {

                if (!part.getCategory()
                        .equalsIgnoreCase(category.trim())) {

                    matches = false;
                }
            }
            // Keyword filter
            if (keyword != null &&
                    !keyword.trim().isEmpty()) {
                String searchWord =
                        keyword.trim().toLowerCase();
                String name =
                        part.getName().toLowerCase();
                String brand =
                        part.getBrand().toLowerCase();
                String code =
                        part.getPartCode().toLowerCase();
                if (!name.contains(searchWord)
                        && !brand.contains(searchWord)
                        && !code.contains(searchWord)) {
                    matches = false;
                }
            }
            // Minimum price filter
            if (minPrice != null) {
                if (part.getPrice() < minPrice) {
                    matches = false;
                }
            }
            // Maximum price filter
            if (maxPrice != null) {
                if (part.getPrice() > maxPrice) {
                    matches = false;
                }
            }
            if (matches) {
                results.add(part);
            }
        }
        return results;
    }
}