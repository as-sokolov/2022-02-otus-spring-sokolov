package ru.otus.spring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.models.Comment;
import ru.otus.spring.service.CommentService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @Test
    void shouldReturnCorrectCommentList() throws Exception {
        List<Comment> comments = List.of(new Comment(1, "Comment1", null), new Comment(2, "Comment2", null));
        given(commentService.getComments(1L)).willReturn(comments);
        List<CommentDto> dtos = comments.stream().map(CommentDto::toDto).collect(Collectors.toList());


        mvc.perform(get("/api/comment").param("bookId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtos)));
    }


    @Test
    void shouldCorrectDeleteComment() throws Exception {
        mvc.perform(delete("/api/comment/1"))
                .andExpect(status().isOk());
        verify(commentService, times(1)).deleteCommentById(1L);
    }

    @Test
    void shouldCorrectSaveNewComment() throws Exception {
        Comment comment = new Comment(1, "Comment1", null);
        given(commentService.saveComment(1, comment)).willReturn(comment);
        String expectedResult = mapper.writeValueAsString(comment);

        mvc.perform(post("/api/comment").param("bookId", "1").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void shouldThrowExceptionDeleteComment() throws Exception {
        doThrow(new DataIntegrityViolationException("Exception")).when(commentService).deleteCommentById(1L);

        mvc.perform(delete("/api/comment/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("При удалении комментария с id = 1 произошла ошибка"));
        verify(commentService, times(1)).deleteCommentById(1L);
    }
}
