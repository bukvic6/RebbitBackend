package rebbit.com.example.rebbit.service;

import org.springframework.web.multipart.MultipartFile;
import rebbit.com.example.rebbit.dto.CoundAndKarma;
import rebbit.com.example.rebbit.model.IndexPost;
import rebbit.com.example.rebbit.model.Post;

import java.util.List;

public interface PostService {
    Post createPost(Post post, MultipartFile pdf);

    List<Post> findAll();

    List<Post> findAllByCommunity(Long id);

    Post findOneById(Long id);

    Iterable<IndexPost> searchPosts(String pdfContent, String title, String text);

    void save(Post post);

    Long findCount(Long id);

    Long findKarma(Long id);

    void remove(Long id);
}
