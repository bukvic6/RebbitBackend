package rebbit.com.example.rebbit.service.impl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import org.springframework.data.elasticsearch.core.SearchHits;
import rebbit.com.example.rebbit.model.Community;
import rebbit.com.example.rebbit.model.IndexCommunity;
import rebbit.com.example.rebbit.repository.CommunityRepo;
import rebbit.com.example.rebbit.repository.CommunityRepoIndex;
import rebbit.com.example.rebbit.service.CommunityService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityServiceImpl implements CommunityService {
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    private final CommunityRepo communityRepo;
    private final CommunityRepoIndex communityRepoIndex;

    public CommunityServiceImpl(ElasticsearchRestTemplate elasticsearchRestTemplate, CommunityRepo communityRepo, CommunityRepoIndex communityRepoIndex) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
        this.communityRepo = communityRepo;
        this.communityRepoIndex = communityRepoIndex;
    }

    @Override
    @Transactional
    public Community save(Community community) {
        Community saveComm = communityRepo.save(community);
        return saveComm;
    }

    @Override
    public Optional<Community> findOneById(Long id) {
        return communityRepo.findById(id);
    }

    @Override
    public List<Community> getAll() {
        return null;
    }

    @Override
    public Iterable<IndexCommunity> searchCommunities(String pdfContent, String name, String description) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (pdfContent != null) {
            queryBuilder.must(QueryBuilders.matchQuery("pdfContent", pdfContent));
        }
        if (name != null) {
            queryBuilder.must(QueryBuilders.matchQuery("name", name));
        }
        if (description != null) {
            queryBuilder.must(QueryBuilders.matchQuery("description", description));
        }
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        SearchHits<IndexCommunity> indexCommunities = elasticsearchRestTemplate.search(searchQuery, IndexCommunity.class, IndexCoordinates.of("reddit_community"));
        var ids = indexCommunities.map(c -> c.getContent().getId());
        return communityRepoIndex.findAllById(ids);
    }
}
