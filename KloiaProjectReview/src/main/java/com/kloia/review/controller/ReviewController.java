package com.kloia.review.controller;



import com.kloia.review.entity.Article;
import com.kloia.review.entity.Review;
import com.kloia.review.service.ReviewService;
import com.querydsl.core.types.Predicate;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {


    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${basicUsername}")
    String username;

    @Value("${basicPassword}")
    String password;

    @Value("${articlePath}")
    String articleUrl;


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
    public ResponseEntity saveReview(@Valid @RequestBody Review review){
        log.info("Saving Review to DB");


        // Basic authentication for rest call
        String plainCreds = username+":"+password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        try {
            ResponseEntity<Article> response= restTemplate.exchange(articleUrl+"articles/"+review.getArticleId(), HttpMethod.GET, request, Article.class);
            Article article = response.getBody();
            review.setArticle(article);
        }
        catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {//If article not found, review will not be saved
            if(HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
                log.error("Article not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid article ID - Article not found");
            }
        }
        catch (Exception e){
            log.error("Getting Article from DB Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Saving review Failed");
        }

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.saveReview(review));
        }catch (Exception e){
            log.error("Saving Review to DB Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Saving review Failed");
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity findReviewById(@PathVariable("id") Long reviewId){
        log.info("Getting Review by id");
        Review review=null;
        try {
            review=reviewService.findReviewById(reviewId);
            if(review!=null)
                return ResponseEntity.status(HttpStatus.OK).body(review);
            else{
                log.error("Review not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
            }
        }catch (Exception e){
            log.error("Getting Review by id Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Getting Review Failed");
        }

    }


    @GetMapping
    public ResponseEntity getAllReviewsPredicate(@QuerydslPredicate(root = Review.class) Predicate predicate) {//If predicate left empty it will return all reviews
        log.info("Getting Review by filter");
        try{
            return ResponseEntity.status(HttpStatus.OK).body(reviewService.findAll(predicate));
        }catch (Exception e){
            log.error("Getting Review by filter Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Getting Review Failed");
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteReviewById(@PathVariable("id") Long reviewId){
        log.info("Deleting Review by id");

        try{
            reviewService.deleteReviewById(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body("Review delete successful");
        }catch (Exception e){
            log.error("Deleting Review by id Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deleting Review Failed");
        }

    }


    @PutMapping("/{id}")
    public ResponseEntity updateReviewById(@PathVariable("id") Long reviewId,@RequestBody Review review){
        log.info("Updating Review by id");
        Review oldReview = reviewService.findReviewById(reviewId);

        if(oldReview==null){
            log.error("Review not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }

        if(review.getReviewContent()!=null)
            oldReview.setReviewContent(review.getReviewContent());
        if(review.getReviewer()!=null)
            oldReview.setReviewer(review.getReviewer());


        try {
            return ResponseEntity.status(HttpStatus.OK).body(reviewService.saveReview(oldReview));
        }catch (Exception e){
            log.error("Updating Review to DB Failed - Exception: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Updating review Failed");
        }


    }



}
