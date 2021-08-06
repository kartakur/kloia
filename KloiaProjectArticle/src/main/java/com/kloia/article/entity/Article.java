package com.kloia.article.entity;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Article {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "article_id")
    Long id;

    @NotNull(message ="Title is mandatory")
    String title;
    @NotNull(message ="Author is mandatory")
    String author;
    @NotNull(message ="Article Content is mandatory")
    String articleContent;
    @NotNull(message ="Date is mandatory")
    Date publishDate;
    @Min(0)
    @Max(5)
    int starCount;


    public Article( String title, String author, String articleContent, Date publishDate, int starCount) {
        this.title = title;
        this.author = author;
        this.articleContent = articleContent;
        this.publishDate = publishDate;
        this.starCount = starCount;
    }

    public Article() {
    }

    @OneToMany(cascade = { CascadeType.REMOVE },mappedBy="article")
    Set<Review> reviews = new HashSet<>();

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }
}
