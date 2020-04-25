package com.learning.springboot;

import com.learning.springboot.error.TodoNotFoundException;
import com.learning.springboot.error.TodoUnSupportedFieldPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository repository;

    // Find
    @GetMapping("/Todos")
    List<Todo> findAll() {
        return (List<Todo>) repository.findAll();
    }

    // Save
    @PostMapping("/Todos")
    @ResponseStatus(HttpStatus.CREATED)
    Todo newTodo(@RequestBody Todo newTodo) {
        return repository.save(newTodo);
    }

    // Find
    @GetMapping("/Todos/{id}")
    Todo findOne(@PathVariable Long id) throws Exception {
        return repository.findById(id)
                .orElseGet(() -> {
                    throw new TodoNotFoundException(id);
                });
    }

    // Save or update
    @PutMapping("/Todos/{id}")
    Todo saveOrUpdate(@RequestBody Todo todo, @PathVariable Long id) {
        return repository.findById(id)
                .map(x -> {
                    x.setId(todo.getId());
                    x.setDescription(todo.getDescription());
                    x.setIsDone(todo.isIsDone());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    todo.setId(id);
                    return repository.save(todo);
                });
    }

    // update isDone only
    @PatchMapping("/Todos/{id}")
    Todo patch(@RequestBody Map<String, String> update, @PathVariable Long id) {
        return repository.findById(id)
                .map(x -> {
                    String description = update.get("isDone");
                    if (!StringUtils.isEmpty(description)) {
                        x.setDescription(description);
                        return repository.save(x);
                    } else {
                        throw new TodoUnSupportedFieldPatchException(update.keySet());
                    }
                })
                .orElseGet(() -> {
                    throw new TodoNotFoundException(id);
                });
    }

    @DeleteMapping("/Todos/{id}")
    void deleteTodo(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
