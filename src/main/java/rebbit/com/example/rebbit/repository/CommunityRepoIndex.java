package rebbit.com.example.rebbit.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import rebbit.com.example.rebbit.model.IndexCommunity;

public interface CommunityRepoIndex extends ElasticsearchRepository<IndexCommunity, Long> {
}
