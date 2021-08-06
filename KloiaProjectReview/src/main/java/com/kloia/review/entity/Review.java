package com.kloia.review.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id")
    Long id;
    @NotNull(message = "Article ID is mandatory")
    Long articleId;


    public Review() {

    }

    public Review(Long id, Long articleId, String reviewer, String reviewContent, Article article) {
        this.id = id;
        this.articleId = articleId;
        this.reviewer = reviewer;
        this.reviewContent = reviewContent;
        this.article = article;
    }

    public Review(Long id, Long articleId, String reviewer, String reviewContent) {
        this.id = id;
        this.articleId = articleId;
        this.reviewer = reviewer;
        this.reviewContent = reviewContent;
    }

    @NotNull
    @NotBlank(message = "Reviewer is mandatory")
    String reviewer;
    @NotNull
    @NotBlank(message = "Review Content is mandatory")
    String reviewContent;

    @ManyToOne
    private Article article;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
