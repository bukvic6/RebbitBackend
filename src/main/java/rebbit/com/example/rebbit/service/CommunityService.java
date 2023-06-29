package rebbit.com.example.rebbit.service;

import rebbit.com.example.rebbit.model.Community;

import java.util.List;

public interface CommunityService {
    Community save(Community community);

    Community findOneById(Long id);

    List<Community> getAll();
}
