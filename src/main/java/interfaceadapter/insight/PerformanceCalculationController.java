package interfaceadapter.insight;

import usecase.insights_performance_calculation.PerformanceCalculationInputBoundary;

public class PerformanceCalculationController {

    private final PerformanceCalculationInputBoundary interactor;

    public PerformanceCalculationController(PerformanceCalculationInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void displayInsights(){
        interactor.calculateInsights();
    }

}
