package usecase.simulate;

import entity.Pantry;

import java.util.Map;

public interface SimulatePantryDataAccessInterface {

    Pantry getPantry();

    Map<String, Integer> getStock();

    void saveStock(Map<String, Integer> stock);

    Map<String, Double> getCurrentPrices();

}
