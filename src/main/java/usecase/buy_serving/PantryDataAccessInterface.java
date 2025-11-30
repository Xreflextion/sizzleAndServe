package usecase.buy_serving;

import entity.Pantry;

/**
 * The Data Access Interface for the Pantry.
 */
public interface PantryDataAccessInterface {

    /**
     * Retrieves the pantry containing all recipes.
     * @return the Pantry entity
     */
    Pantry getPantry();

    /**
     * Saves the updated pantry state.
     * @param pantry the Pantry entity with updated stock
     */
    void savePantry(Pantry pantry);
}
