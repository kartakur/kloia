package com.kloia.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kloia.article.entity.Article;
import com.kloia.article.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ArticleApplicationTests {

    @Value("${basicUsername}")
    String username;

    @Value("${basicPassword}")
    String password;

    @Resource
    private ArticleRepository articleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenArticle_whenSave_thenGetOk() {
        Date date = new Date();

        Article article = new Article("Kavgam","Ayşegül","Gev gev",date,3);
        articleRepository.save(article);

        Article article2 = articleRepository.findArticleById(article.getId());
        assertEquals("Kavgam", article2.getTitle());
    }

    @Test
    void test2() throws Exception {
        Article article = new Article("Dönüşüm","Kafka","Gev gev blahh",new Date(),4);

        mockMvc.perform(post("/articles")
                .with(user(username).password(password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isCreated());

    }

}
