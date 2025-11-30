package interface_adapter.insight;

import use_case.insights.performance_calculation.PerformanceCalculationInputBoundary;

public class PerformanceCalculationController {

    private final PerformanceCalculationInputBoundary interactor;

    public PerformanceCalculationController(PerformanceCalculationInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void displayInsights(){
        interactor.calculateInsights();
    }

}
