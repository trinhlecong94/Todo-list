package com.learning.springboot;

import com.learning.springboot.error.TodoNotFoundException;
import com.learning.springboot.error.TodoBadRequestException;
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

    @GetMapping("/tasks")
    List<Todo> findAll() {
        return (List<Todo>) repository.findAll();
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    Todo newTodo(@RequestBody Todo newTodo) {
        return repository.save(newTodo);
    }

    @GetMapping("/tasks/{id}")
    Todo findOne(@PathVariable Long id) throws Exception {
        return repository.findById(id)
                .orElseGet(() -> {
                    throw new TodoNotFoundException(id);
                });
    }

    @PutMapping("/tasks/{id}")
    Todo update(@RequestBody Todo todo, @PathVariable Long id) {
        return repository.findById(id)
                .map(x -> {
                    if ((!todo.getDescription().matches("^ +$"))
                            && (!StringUtils.isEmpty(todo.getDescription()))) {
                        x.setDescription(todo.getDescription());
                        x.setIsDone(todo.isIsDone());
                        return repository.save(x);
                    } else {
                        throw new TodoBadRequestException("description");
                    }
                })
                .orElseGet(() -> {
                    throw new TodoNotFoundException(todo.getId());
                });
    }

    @PatchMapping("/tasks/{id}")
    Todo patch(@RequestBody Map<String, Boolean> update, @PathVariable Long id) {
        return repository.findById(id)
                .map(x -> {
                    if (update.containsKey("isDone")) {
                        x.setIsDone(update.get("isDone"));
                        return repository.save(x);
                    } else {
                        throw new TodoUnSupportedFieldPatchException(update.keySet());
                    }
                })
                .orElseGet(() -> {
                    throw new TodoNotFoundException(id);
                });
    }

    @DeleteMapping("/tasks/{id}")
    void deleteTodo(@PathVariable Long id) {
        repository.findById(id).orElseGet(() -> {
            throw new TodoNotFoundException(id);
        });
        repository.deleteById(id);
    }

}
