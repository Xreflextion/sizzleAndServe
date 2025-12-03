package use_case.simulate;

import java.util.Map;

import entity.Pantry;

public interface SimulatePantryDataAccessInterface {

    /**
     * Return the pantry.
     *
     * @return the stored pantry
     */
    Pantry getPantry();

    /**
     * Get current stock for each recipe in pantry.
     *
     * @return mapping of dish names to their current stock
     */
    Map<String, Integer> getStock();

    /**
     * Save the given stock into the pantry.
     *
     * @param stock new stock to save
     */
    void saveStock(Map<String, Integer> stock);

    /**
     * Get current prices for each recipe in pantry.
     *
     * @return mapping of dish names to their current price
     */
    Map<String, Double> getCurrentPrices();

}
