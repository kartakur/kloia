package com.kloia.article.service;


import com.kloia.article.entity.Article;
import com.kloia.article.repository.ArticleRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    
    
    @Autowired
    private ArticleRepository articleRepository;

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article findArticleById(Long articleId) {

        return articleRepository.findArticleById(articleId);
    }

    public Iterable<Article> findAll(Predicate predicate) {
        return articleRepository.findAll(predicate);
    }

    public void deleteArticleById(Long articleId) {
        articleRepository.deleteArticleById(articleId);
    }
}
