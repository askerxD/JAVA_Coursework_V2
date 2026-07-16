package com.example.javacw.utils;

import com.example.javacw.objects.Part;
import java.util.ArrayList;

public class LowStockUtil {

     public static ArrayList<Part> getLowStockItems(ArrayList<Part> parts, int threshold) {
          ArrayList<Part> lowStockItems = new ArrayList<>();
          for (Part part : parts) {
               if (part.getQuantity() < threshold) {
                    lowStockItems.add(part);
               }
          }
          return lowStockItems;
     }
}
