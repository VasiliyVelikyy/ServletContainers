package CRUD.ru.netology.servlet;

import CRUD.ru.netology.controller.PostController;
import CRUD.ru.netology.exception.NotFoundException;
import CRUD.ru.netology.repository.PostRepository;
import CRUD.ru.netology.service.PostService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;
    public static final String API_POSTS = "/api/posts";
    public static final String API_POSTS_D = "/api/posts/\\d+";
    public static final String STR = "/";

    @Override
    public void init()  {
        //super.init(config);
        final PostRepository repository = new PostRepository();
        final PostService service = new PostService(repository);
        controller = new PostController(service);
        log("Method init =)");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого

        try {
            final String path = req.getRequestURI();
            final String method = req.getMethod();
            System.out.println(path);
            System.out.println(path);
            // primitive routing
            if (method.equals("GET") && path.equals("/api/posts")) {
                controller.all(resp);
                return;
            }
            if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
                // easy way
                final long id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                controller.getById(id, resp);
                return;
            }
            if (method.equals("POST") && path.equals("/api/posts")) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
                // easy way
                final long id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (NotFoundException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().write("Method doGet\n");
    }

    @Override
    public void destroy() {
        log("Method destroy =)");
    }

    private long parseId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf(STR) + 1));
    }
}

