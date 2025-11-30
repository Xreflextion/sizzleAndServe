package usecase.insights_day_calculation;

public class DayInsightsOutputData {

        private final int dayNumber;
        private final double dayRating;
        private final double dayRevenue;
        private final double dayExpenses;
        private final double dayProfit;


        public DayInsightsOutputData(int dayNumber, double dayRating, double dayRevenue, double dayExpenses, double dayProfit) {
            this.dayNumber = dayNumber;
            this.dayRating = dayRating;
            this.dayRevenue = dayRevenue;
            this.dayExpenses = dayExpenses;
            this.dayProfit = dayProfit;
        }

        public int getDayNumber() {
            return dayNumber;
        }

        public double getDayRating() {
            return dayRating;
        }

        public double getDayRevenue() {
            return dayRevenue;
        }

        public double getDayExpenses() {
            return dayExpenses;
        }

        public double getDayProfit() {
            return dayProfit;
        }


}
