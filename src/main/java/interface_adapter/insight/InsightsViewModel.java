package interface_adapter.insight;

import interface_adapter.ViewModel;

public class InsightsViewModel extends ViewModel<InsightsState>{

    public static final String viewName = "Insights";
    public InsightsViewModel() {
        super(viewName);
        setState(new InsightsState());
    }

    @Override
    public void setState(InsightsState state) {
        super.setState(state);
        firePropertyChange();
    }

//    public double getAverageRating() {
//        return averageRating;
//    }
//
//    public void setAverageRating(double averageRating) {
//        this.averageRating = averageRating;
//    }
//
//    public int getReviewCount() {
//        return reviewCount;
//    }
//
//    public void setReviewCount(int reviewCount) {
//        this.reviewCount = reviewCount;
//    }
//
//    public double getTotalRevenue() {
//        return totalRevenue;
//    }
//
//    public void setTotalRevenue(double totalRevenue) {
//        this.totalRevenue = totalRevenue;
//    }
//
//    public double getTotalExpenses() {
//        return totalExpenses;
//    }
//
////    public void setTotalExpenses(double totalExpenses) {
////        this.totalExpenses = totalExpenses;
////    }
//
//    public double getTotalProfit() {
//        return totalProfit;
//    }
//
////    public void setTotalProfit(double totalProfit) {
////        this.totalProfit = totalProfit;
////    }

}
