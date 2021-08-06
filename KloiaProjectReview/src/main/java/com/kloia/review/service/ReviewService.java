package com.kloia.review.service;


import com.kloia.review.entity.Review;
import com.kloia.review.repository.ReviewRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    
    
    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review findReviewById(Long reviewId) {

        return reviewRepository.findReviewById(reviewId);
    }

    public Iterable<Review> findAll(Predicate predicate) {
        return reviewRepository.findAll(predicate);
    }

    public void deleteReviewById(Long reviewId) {
        reviewRepository.deleteReviewById(reviewId);
    }
}
