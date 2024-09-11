package org.roleonce.projektarbete_web_services.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String overview;
    private String releaseDate;
    private Double voteAverage;

    public Movie() {}

    public Movie(String title, String overview, String releaseDate, double voteAverage) {
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getOverview() {return overview;}
    public void setOverview(String overview) {this.overview = overview;}

    public String getReleaseDate() {return releaseDate;}
    public void setReleaseDate(String releaseDate) {this.releaseDate = releaseDate;}

    public Double getVoteAverage() {return voteAverage;}
    public void setVoteAverage(Double voteAverage) {this.voteAverage = voteAverage;}

}
