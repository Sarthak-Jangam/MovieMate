package com.movieproject.movies.service;

import com.movieproject.movies.model.Movie;
import com.movieproject.movies.model.Review;
import com.movieproject.movies.repository.MovieRepository;
import com.movieproject.movies.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public Review createReview(String reviewBody, String imdbId) {
        // Find the movie by imdbId
        Optional<Movie> optionalMovie = movieRepository.findMovieByImdbId(imdbId);

        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();

            // Create a new review and associate it with the movie
            Review review = new Review();
            review.setBody(reviewBody);
            review.setCreated(LocalDateTime.now());
            review.setUpdated(LocalDateTime.now());
            review.setMovie(movie); // Associate review with movie

            // Save review
            review = reviewRepository.save(review);

            // Add the review to the movie's review list
            movie.getReviews().add(review);
            movieRepository.save(movie);

            return review;
        } else {
            throw new RuntimeException("Movie with imdbId " + imdbId + " not found.");
        }
    }
}
