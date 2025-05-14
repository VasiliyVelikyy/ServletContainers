package CRUD.ru.netology.controller;

import com.google.gson.Gson;
import CRUD.ru.netology.exception.NotFoundException;
import CRUD.ru.netology.model.Post;
import CRUD.ru.netology.service.PostService;
import com.google.gson.JsonSyntaxException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;
    private final Gson gson = new Gson();

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        Collection<Post> data = service.all();
        writeJson(response, data);
    }

    public void getById(long id, HttpServletResponse response) throws IOException, NotFoundException {
        Post post = service.getById(id);
        writeJson(response, post);
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        try {
            Post post = gson.fromJson(body, Post.class);
            Post saved = service.save(post);
            writeJson(response, saved);
        } catch (JsonSyntaxException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
        }
    }

    public void removeById(Long id, HttpServletResponse response) throws IOException {
        try {
            service.removeById(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Вспомогательные методы
    private void writeJson(HttpServletResponse response, Object data) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.getWriter().print(gson.toJson(data));
    }
}
