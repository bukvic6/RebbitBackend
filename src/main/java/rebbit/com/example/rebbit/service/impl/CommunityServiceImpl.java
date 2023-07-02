package rebbit.com.example.rebbit.service.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.multipart.MultipartFile;
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
    public static Optional<String> parsePdf(MultipartFile file) {
        try (var pdfInputStream = file.getInputStream(); var pddDocument = PDDocument.load(pdfInputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return Optional.of(pdfStripper.getText(pddDocument));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Community save(Community community, MultipartFile pdf) {
        Community saveComm = communityRepo.save(community);
        IndexCommunity indexCommunity;
        if(pdf == null){
            indexCommunity = new IndexCommunity(community);
        } else{
            Optional<String> pdfCont = parsePdf(pdf);
            indexCommunity = new IndexCommunity(saveComm, pdfCont.get(), pdf.getOriginalFilename());
        }
        communityRepoIndex.save(indexCommunity);
        return saveComm;
    }

    @Override
    public Optional<Community> findOneById(Long id) {
        return communityRepo.findById(id);
    }

    @Override
    public List<Community> getAll() {
        return communityRepo.findAll();
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
