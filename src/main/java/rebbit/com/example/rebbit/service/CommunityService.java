package rebbit.com.example.rebbit.service;

import rebbit.com.example.rebbit.model.Community;

import java.util.List;
import java.util.Optional;

public interface CommunityService {
    Community save(Community community);

    Optional<Community> findOneById(Long id);

    List<Community> getAll();
}
