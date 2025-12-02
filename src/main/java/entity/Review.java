package entity;

public class Review {

    // Following things come from entity
    // The rating for the restaurant
    private double rating;
    // The day number for the rating, for example It would say like on Day 1: 4 stars out of 5
    private int dayNum;


    /**
     Each rating should come from the simulation
     Where the simulation will randomly do the rating and the day number
     will already be tracked
     */
    public Review(double rating, int dayNum) {
        this.rating = rating;
        this.dayNum = dayNum;
    }

    // Returns the day number
    public int getDayNum() {
        return this.dayNum;
    }

    // Returns the rating
    public double getRating() {
        return this.rating;
    }

}

