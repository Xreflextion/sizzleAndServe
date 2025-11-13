package use_case;

import use_case.product_prices.ProductPricesOutputBoundary;
import use_case.product_prices.ProductPricesOutputData;

public class TestOutputBoundary implements ProductPricesOutputBoundary {

    public ProductPricesOutputData lastSuccessOutput = null;
    public String lastFailMessage = null;

    @Override
    public void prepareSuccessView(ProductPricesOutputData outputData) {
        this.lastSuccessOutput = outputData;
    }

    @Override
    public void prepareFailView(String errorMessage) {
        this.lastFailMessage = errorMessage;
    }

    public boolean wasSuccessCalled() {
        return lastSuccessOutput != null;
    }

    public boolean wasFailCalled() {
        return lastFailMessage != null;
    }
}
