package com.kloia.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kloia.review.entity.Review;
import com.kloia.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ReviewApplicationTests {

    @Value("${basicUsername}")
    String username;

    @Value("${basicPassword}")
    String password;

    @Resource
    private ReviewRepository reviewRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenReview_whenSave_thenGetOk() {
        Review review = new Review(1L,2L,"Osmaniye","asdasdads");
        reviewRepository.save(review);

        Review review2 = reviewRepository.findReviewById(1L);
        assertEquals("Osmaniye", review2.getReviewer());
    }

    @Test
    void test2() throws Exception {
        //Review review = new Review(1L,2L,"Ayşegül","asddcxvc213asdasd");

        mockMvc.perform(get("/reviews")
                .with(user(username).password(password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(" ")))
                .andExpect(status().isOk());

    }



}
