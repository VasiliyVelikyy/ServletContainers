package CRUD.ru.netology.service;

import CRUD.ru.netology.exception.NotFoundException;
import CRUD.ru.netology.model.Post;
import CRUD.ru.netology.repository.PostRepository;

import java.util.Collection;

public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Collection<Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        return repository.getById(id).orElseThrow(NotFoundException::new);
    }

    public Post save(Post post) {
        return repository.save(post);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}

