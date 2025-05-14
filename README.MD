mvn clean package

copy war in apache-tomcat-10.1.28/webapps


cd \apache-tomcat-10.1.28\bin
start - startup.bat
stop  - shutdown.bat


tomcat ui -http://localhost:8080/manager/html

GET http://localhost:8080/api/posts
POST http://localhost:8080/api/posts
DELETE http://localhost:8080/api/posts/1




Нелогичная проверка условия
if (savePost.getId() == 0) { ... }
else if (savePost.getId() != 0) { ... }
Второе условие (getId() != 0) — всегда истинно , если не выполнено первое.
Это избыточно. Лучше просто использовать else.
Изменение входного объекта savePost
savePost.setId(id);
Это нарушает принцип немутабельности входных данных.
Если этот объект используется где-то ещё, изменения отразятся и там.
Это может привести к багам, особенно в многопоточной среде.
✅ Лучше создать новый объект:
Post newPost = new Post(id, savePost.getContent());
allPosts.put(id, newPost);
return newPost;
3. Нет обработки случая, когда обновляемый пост не существует
   Если ты пытаешься обновить пост с id=5, но такого нет в мапе — ты просто добавишь его как новый.

Это может быть корректной стратегией , но лучше сделать поведение явным:

либо бросать исключение: throw new NotFoundException("Post not found")
либо возвращать Optional<Post>
4. Потенциальные проблемы с потоками
   Ты используешь ConcurrentHashMap и AtomicLong, что хорошо для многопоточности, но сам метод содержит несколько операций , которые не атомарны :
   long id = idCounter.incrementAndGet();
   Post newPost = new Post(id, savePost.getContent());
   allPosts.put(id, newPost);
   Между incrementAndGet() и put() возможна гонка.

✅ Решение: используй computeIfAbsent или putIfAbsent.

✅ Улучшенная версия метода
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