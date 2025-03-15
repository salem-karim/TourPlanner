package at.technikum.tourplanner.models;

public class Logs {
    private int id;
    private String date_time;
    private String comment;
    private int difficulty;
    private int total_distance;
    private int total_time;
    private int rating;

    public Logs(int id, String date_time, String comment, int difficulty, int total_distance, int total_time, int rating) {
        this.id = id;
        this.date_time = date_time;
        this.comment = comment;
        this.difficulty = difficulty;
        this.total_distance = total_distance;
        this.total_time = total_time;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDate_time() {
        return date_time;
    }
    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public int getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    public int getTotal_distance() {
        return total_distance;
    }
    public void setTotal_distance(int total_distance) {
        this.total_distance = total_distance;
    }
    public int getTotal_time() {
        return total_time;
    }
    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
}


