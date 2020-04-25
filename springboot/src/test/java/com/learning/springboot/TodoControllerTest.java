package com.learning.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpHeaders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TodoControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRepository mockRepository;

    @Before
    public void init() {
        Todo todo = new Todo(1L, "Learning java", true);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(todo));
    }

    @Test
    public void find_todoId_OK() throws Exception {
        mockMvc.perform(get("/Todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Learning java")))
                .andExpect(jsonPath("isDone", is(true)));
        verify(mockRepository, times(1)).findById(1L);
    }

    @Test
    public void find_allTodo_OK() throws Exception {
        List<Todo> todos = Arrays.asList(
                new Todo(1L, "Learning java", true),
                new Todo(2L, "Learning python", false));
        when(mockRepository.findAll()).thenReturn(todos);
        mockMvc.perform(get("/Todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Learning java")))
                .andExpect(jsonPath("$[0].isDone", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("Learning python")))
                .andExpect(jsonPath("$[1].isDone", is(false)));
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_todoIdNotFound_404() throws Exception {
        mockMvc.perform(get("/Todos/5")).andExpect(status().isNotFound());
    }

    @Test
    public void save_todo_OK() throws Exception {
        Todo newTodo = new Todo(1L, "Learning java", true);
        when(mockRepository.save(any(Todo.class))).thenReturn(newTodo);
        mockMvc.perform(post("/Todos")
                .content(om.writeValueAsString(newTodo))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Learning java")))
                .andExpect(jsonPath("$.isDone", is(true)));
        verify(mockRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void update_todo_OK() throws Exception {
        Todo updateTodo = new Todo(1L, "Learning java", true);
        when(mockRepository.save(any(Todo.class))).thenReturn(updateTodo);
        mockMvc.perform(put("/Todos/1")
                .content(om.writeValueAsString(updateTodo))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Learning java")))
                .andExpect(jsonPath("$.isDone", is(true)));
    }

    @Test
    public void patch_todoIsDone_OK() throws Exception {
        when(mockRepository.save(any(Todo.class))).thenReturn(new Todo());
        String patchInJson = "{\"isDone\":\"False\"}";
        mockMvc.perform(patch("/Todos/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Todo.class));

    }

    @Test
    public void patch_todoIsDone_405() throws Exception {
        String patchInJson = "{\"description\":\"Learning Angular\"}";
        mockMvc.perform(patch("/Todos/1")
                .content(patchInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void delete_todo_OK() throws Exception {
        doNothing().when(mockRepository).deleteById(1L);
        mockMvc.perform(delete("/Todos/1"))
                .andExpect(status().isOk());
        verify(mockRepository, times(1)).deleteById(1L);
    }

}
