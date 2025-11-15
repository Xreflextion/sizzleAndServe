package data_access;

import java.util.Map;

// Todo: remove this file and use files made by groupmates
public class PantryDataAccessObject {
    private Map<String, Integer> stock;
    private Map<String, Double> prices;

    public Map<String, Integer> getStock() {
        return stock;
    }

    public void setStock(Map<String, Integer> newStock) {
        stock = newStock;
    }

    public Map<String, Double> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Double> newPrices) {
        prices = newPrices;
    }


}
