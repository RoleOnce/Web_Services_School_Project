package org.roleonce.projektarbete_web_services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String overview;
    private String movie_review;
    private String release_date;
    @JsonProperty("origin_country")
    private List<String> originCountry;
    @JsonProperty("vote_average")
    private double voteAverage;
    private int budget;

    public Movie() {}

    public Movie(Long id, String title, String overview, String movieReview, String release_date, List<String> origin_country, double vote_average, int budget) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.movie_review = movieReview;
        this.release_date = release_date;
        this.originCountry = origin_country;
        this.voteAverage = vote_average;
        this.budget = budget;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getMovie_review() {
        return movie_review;
    }
    public void setMovie_review(String movieReview) {
        this.movie_review = movieReview;
    }

    public String getRelease_date() {
        return release_date;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }
    public void setOriginCountry(List<String> origin_country) {
        this.originCountry = origin_country;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }
    public void setVoteAverage(Double vote_average) {
        this.voteAverage = vote_average;
    }

    public int getBudget() {
        return budget;
    }
    public void setBudget(int budget) {
        this.budget = budget;
    }
}
