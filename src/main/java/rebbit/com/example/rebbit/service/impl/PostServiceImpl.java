package rebbit.com.example.rebbit.service.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rebbit.com.example.rebbit.model.IndexPost;
import rebbit.com.example.rebbit.model.Post;
import rebbit.com.example.rebbit.repository.PostRepo;
import rebbit.com.example.rebbit.repository.PostRepoIndex;
import rebbit.com.example.rebbit.repository.ReactionRepo;
import rebbit.com.example.rebbit.service.PostService;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepo postRepo;
    private final PostRepoIndex postRepoIndex;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final ReactionRepo reactionRepo;


    public PostServiceImpl(PostRepo postRepo, PostRepoIndex postRepoIndex, ElasticsearchRestTemplate elasticsearchRestTemplate, ReactionRepo reactionRepo) {
        this.postRepo = postRepo;
        this.postRepoIndex = postRepoIndex;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
        this.reactionRepo = reactionRepo;
    }

    public static Optional<String> parsePdfPost(MultipartFile file) {
        try (var pdfInputStream = file.getInputStream(); var pddDocument = PDDocument.load(pdfInputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return Optional.of(pdfStripper.getText(pddDocument));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Post createPost(Post post, MultipartFile pdf) {
        Post savePost = postRepo.save(post);
        IndexPost indexPost;
        if (pdf == null) {
            indexPost = new IndexPost(post);
        } else {
            Optional<String> pdfCont = parsePdfPost(pdf);
            indexPost = new IndexPost(savePost, pdfCont.get(), pdf.getOriginalFilename());
        }
        postRepoIndex.save(indexPost);
        return savePost;

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

    @Override
    public Iterable<IndexPost> searchPosts(String pdfContent, String title, String text) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (pdfContent != null) {
            queryBuilder.must(QueryBuilders.matchQuery("pdfContent", pdfContent));
        }
        if (title != null) {
            queryBuilder.must(QueryBuilders.matchQuery("title", title));
        }
        if (text != null) {
            queryBuilder.must(QueryBuilders.matchQuery("text", text));
        }
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        SearchHits<IndexPost> indexCommunities = elasticsearchRestTemplate.search(searchQuery, IndexPost.class, IndexCoordinates.of("reddit_posts"));
        var ids = indexCommunities.map(p -> p.getContent().getId());
        return postRepoIndex.findAllById(ids);
    }

    @Override
    public void save(Post post) {
        postRepo.save(post);

    }

    @Override
    public Long findCount(Long id) {
        return postRepo.findCount(id);
    }

    @Override
    public Long findKarma(Long id) {
        return postRepo.findKarma(id);
    }

    @Override
    public void remove(Long id) {
        postRepoIndex.deleteById(id);
        reactionRepo.deleteByPostId(id);
        postRepo.deleteById(id);

    }


}
