package rebbit.com.example.rebbit.service;

import org.springframework.web.multipart.MultipartFile;
import rebbit.com.example.rebbit.model.Community;
import rebbit.com.example.rebbit.model.IndexCommunity;

import java.util.List;
import java.util.Optional;

public interface CommunityService {
    Community save(Community community, MultipartFile pdf);

    Optional<Community> findOneById(Long id);

    List<Community> getAll();

    Iterable<IndexCommunity> searchCommunities(String pdfContent, String name, String description);
}
