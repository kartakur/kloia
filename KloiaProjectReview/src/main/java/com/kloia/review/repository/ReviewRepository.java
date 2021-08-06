package com.kloia.review.repository;


import com.kloia.review.entity.QReview;
import com.kloia.review.entity.Review;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, QuerydslPredicateExecutor<Review>, QuerydslBinderCustomizer<QReview> {
    @Override
    default public void customize(QuerydslBindings bindings, QReview qReview) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }


    void deleteReviewById(Long reviewId);
    Review findReviewById(Long reviewId);
}