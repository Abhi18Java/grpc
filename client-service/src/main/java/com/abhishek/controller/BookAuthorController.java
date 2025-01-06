package com.abhishek.controller;

import com.abhishek.service.BookAuthorService;
import com.google.protobuf.Descriptors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class BookAuthorController {

    @Autowired
    private BookAuthorService bookAuthorService;

    @GetMapping("/author/{id}")
    public Map<Descriptors.FieldDescriptor, Object> getAuthor(@PathVariable String authorId) {
        return bookAuthorService.getAuthor(Integer.parseInt(authorId));
    }
}
