package rebbit.com.example.rebbit.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import rebbit.com.example.rebbit.model.IndexPost;

public interface PostRepoIndex extends ElasticsearchRepository<IndexPost, Long> {
}
