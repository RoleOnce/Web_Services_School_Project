package org.roleonce.projektarbete_web_services.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int movieId;  // Filmens ID
    private double value; // Ratingens värde

    public Rating() {}

    public Rating(int movieId, double value) {
        this.movieId = movieId;
        this.value = value;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public int getMovieId() {return movieId;}
    public void setMovieId(int movieId) {this.movieId = movieId;}

    public double getValue() {return value;}
    public void setValue(double value) {this.value = value;}

}
