package CRUD.ru.netology.servlet;

import CRUD.ru.netology.controller.PostController;
import CRUD.ru.netology.exception.NotFoundException;
import CRUD.ru.netology.repository.PostRepository;
import CRUD.ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class MainServlet extends HttpServlet {
    private PostController controller;
    public static final String API_POSTS = "/api/posts";
    public static final String API_POSTS_D = "/api/posts/\\d+";
    public static final String STR = "/";

    @Override
    public void init() {
        final PostRepository repository = new PostRepository();
        final PostService service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        try {
            final String path = req.getRequestURI();
            final String method = req.getMethod();

            if (method.equals("GET") && path.equals(API_POSTS)) {
                controller.all(resp);
                return;
            }
            if (method.equals("GET") && path.matches(API_POSTS_D)) {
                Optional<Long> idOpt = parseId(path);
                if (idOpt.isPresent()) {
                    controller.getById(idOpt.get(), resp);
                }
                return;
            }
            if (method.equals("POST") && path.equals(API_POSTS)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches(API_POSTS_D)) {
                Optional<Long> idOpt = parseId(path);

                if (idOpt.isPresent()) {
                    controller.removeById(idOpt.get(), resp);
                }
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (NotFoundException e) {
            log(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            log(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<Long> parseId(String path) {
        try {
            int idx = path.lastIndexOf(STR);
            if (idx == -1) return Optional.empty();
            return Optional.of(Long.parseLong(path.substring(idx + 1)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}

