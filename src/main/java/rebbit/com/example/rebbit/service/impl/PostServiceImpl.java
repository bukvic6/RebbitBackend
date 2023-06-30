package rebbit.com.example.rebbit.service.impl;

import org.springframework.stereotype.Service;
import rebbit.com.example.rebbit.model.Post;
import rebbit.com.example.rebbit.repository.PostRepo;
import rebbit.com.example.rebbit.service.PostService;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepo postRepo;


    public PostServiceImpl(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    @Override
    public Post createPost(Post post) {
        return postRepo.save(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepo.findAll();
    }

    @Override
    public List<Post> findAllByCommunity(Long id) {
        return postRepo.findAllByCommunity(id);
    }

    @Override
    public Post findOneById(Long id) {
        return postRepo.findOneById(id);
    }
}
