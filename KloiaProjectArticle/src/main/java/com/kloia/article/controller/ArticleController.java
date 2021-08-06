package com.kloia.article.controller;


import com.kloia.article.entity.Article;
import com.kloia.article.repository.ArticleRepository;
import com.kloia.article.service.ArticleService;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {


    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PostMapping
    public ResponseEntity saveArticle(@Valid @RequestBody Article article){
        log.info("Saving Article to DB");

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(articleService.saveArticle(article));
        }catch (Exception e){
            log.error("Saving Article to DB Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Saving Article Failed");
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity findArticleById(@PathVariable("id") Long articleId){

        log.info("Getting Article by id");
        Article article=null;
        try {
            article=articleService.findArticleById(articleId);
            if(article!=null)
                return ResponseEntity.status(HttpStatus.FOUND).body(article);
            else{
                log.error("Article not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
            }
        }catch (Exception e){
            log.error("Getting Article by id Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Getting Article Failed");
        }

    }



    @GetMapping
    public ResponseEntity getAllArticlesPredicate(@QuerydslPredicate(root = Article.class)  Predicate predicate) {
        log.info("Getting Article by filter");
        try{
            return ResponseEntity.status(HttpStatus.FOUND).body(articleService.findAll(predicate));
        }catch (Exception e){
            log.error("Getting Article by filter Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Getting Article Failed");
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteArticleById(@PathVariable("id") Long articleId){//Delete reviews attached to article
        log.info("Deleting Article by id");
        try{
            articleService.deleteArticleById(articleId);

            return ResponseEntity.status(HttpStatus.OK).body("Article delete successful");
        }catch (Exception e){
            log.error("Deleting Article by id Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deleting Article Failed");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity updateArticleById(@PathVariable("id") Long articleId,@RequestBody Article article){
        log.info("Updating Article by id");
        Article oldArticle = articleService.findArticleById(articleId);
        if(oldArticle==null){
            log.error("Article not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
        }


        if(article.getArticleContent()!=null)
            oldArticle.setArticleContent(article.getArticleContent());
        if(article.getAuthor()!=null)
            oldArticle.setAuthor(article.getAuthor());
        if(article.getPublishDate()!=null)
            oldArticle.setPublishDate(article.getPublishDate());
        if(article.getStarCount()>=0 || article.getStarCount()<=5)
            oldArticle.setStarCount(article.getStarCount());
        if(article.getTitle()!=null)
            oldArticle.setTitle(article.getTitle());

        try {
            return ResponseEntity.status(HttpStatus.OK).body(articleService.saveArticle(oldArticle));
        }catch (Exception e){
            log.error("Updating Article to DB Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Updating Article Failed");
        }

    }



}
