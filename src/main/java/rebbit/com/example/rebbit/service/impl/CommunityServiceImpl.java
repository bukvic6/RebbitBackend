package rebbit.com.example.rebbit.service.impl;

import rebbit.com.example.rebbit.model.Community;
import rebbit.com.example.rebbit.repository.CommunityRepo;
import rebbit.com.example.rebbit.service.CommunityService;

import javax.transaction.Transactional;
import java.util.List;

public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepo communityRepo;

    public CommunityServiceImpl(CommunityRepo communityRepo) {
        this.communityRepo = communityRepo;
    }

    @Override
    @Transactional
    public Community save(Community community) {
        Community saveComm = communityRepo.save(community);
        return saveComm;
    }

    @Override
    public Community findOneById(Long id) {
        return null;
    }

    @Override
    public List<Community> getAll() {
        return null;
    }
}
