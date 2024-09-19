package org.roleonce.projektarbete_web_services.response;

import org.roleonce.projektarbete_web_services.model.Movie;

public class MovieResponse  extends Movie implements WsResponse {
    public MovieResponse(Movie movie) {
        super(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getMovie_review(), movie.getRelease_date(),
                movie.getOriginCountry(),movie.getVoteAverage(), movie.getBudget());
    }

}
