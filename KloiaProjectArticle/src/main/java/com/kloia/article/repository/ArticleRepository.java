package com.kloia.article.repository;


import com.kloia.article.entity.Article;
import com.kloia.article.entity.QArticle;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article>, QuerydslBinderCustomizer<QArticle> {
    @Override
    default public void customize(QuerydslBindings bindings, QArticle article) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }



    @Transactional
    void deleteArticleById(Long articleId);

    Article findArticleById(Long articleId);
}