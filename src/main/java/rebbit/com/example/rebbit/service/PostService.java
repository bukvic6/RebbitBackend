package rebbit.com.example.rebbit.service;

import rebbit.com.example.rebbit.model.IndexPost;
import rebbit.com.example.rebbit.model.Post;

import java.util.List;

public interface PostService {
    Post createPost(Post post);

    List<Post> findAll();

    List<Post> findAllByCommunity(Long id);

    Post findOneById(Long id);

    Iterable<IndexPost> searchPosts(String pdfContent, String title, String text);
}
