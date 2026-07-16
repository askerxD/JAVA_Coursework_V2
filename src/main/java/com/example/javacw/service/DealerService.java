package com.example.javacw.service;

import com.example.javacw.objects.Dealer;
import com.example.javacw.parsers.DealerParser;
import com.example.javacw.utils.SortUtil;
import java.util.ArrayList;
import java.util.Random;

public class DealerService {
    private ArrayList<Dealer> dealers = new ArrayList<>();
    private final String filePath;
    public DealerService(String filePath) {
        this.filePath = filePath;
        loadDealers();
    }
    // Load dealer data
    public void loadDealers() {
        dealers = DealerParser.parseDealerFile(filePath);
    }
    // Get all dealers
    public ArrayList<Dealer> getAllDealers() {
        return dealers;
    }
    // Select 4 unique random dealers, then sort by location
    public ArrayList<Dealer> getRandomFourDealers() {

        ArrayList<Dealer> selected = new ArrayList<>();
        Random random = new Random();

        if (dealers.size() <= 4) {
            selected = new ArrayList<>(dealers);
            SortUtil.sortDealersByLocation(selected);
            return selected;
        }
        while (selected.size() < 4) {

            int index = random.nextInt(dealers.size());

            Dealer dealer = dealers.get(index);

            if (!containsDealer(selected, dealer.getDealerId())) {
                selected.add(dealer);
            }
        }
        SortUtil.sortDealersByLocation(selected);
        return selected;
    }
    // Prevent duplicate dealer selection
    private boolean containsDealer(ArrayList<Dealer> list, String dealerId) {
        for (Dealer dealer : list) {
            if (dealer.getDealerId().equals(dealerId)) {
                return true;
            }
        }
        return false;
    }
}
