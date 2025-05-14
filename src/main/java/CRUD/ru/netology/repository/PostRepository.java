package CRUD.ru.netology.repository;

import CRUD.ru.netology.exception.NotFoundException;
import CRUD.ru.netology.model.Post;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private final ConcurrentMap<Long, Post> allPosts;
    private final AtomicLong idCounter = new AtomicLong();

    public PostRepository() {
        this.allPosts = new ConcurrentHashMap<>();
    }

    public Collection<Post> all() {
        return allPosts.values();
    }

    public Post getById(long id) {
        return Optional.ofNullable(allPosts.get(id))
                .orElseThrow(NotFoundException::new);
    }

    public Post save(Post savePost) {
        if (savePost.getId() == 0) {
            long newId = idCounter.incrementAndGet();
            Post newPost = new Post(newId, savePost.getContent());
            allPosts.put(newId, newPost);
            return newPost;
        } else {
            return allPosts.compute(savePost.getId(), (id, existingPost) -> {
                if (existingPost == null) {
                    throw new NotFoundException("Post with id " + id + " not found");
                }
                return new Post(id, savePost.getContent()); // Неизменяемость
            });
        }
    }

    public void removeById(long id) {
        allPosts.remove(id);
    }
}
