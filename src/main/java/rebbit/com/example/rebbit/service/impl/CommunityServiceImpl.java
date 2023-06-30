package rebbit.com.example.rebbit.service.impl;

import org.springframework.stereotype.Service;
import rebbit.com.example.rebbit.model.Community;
import rebbit.com.example.rebbit.repository.CommunityRepo;
import rebbit.com.example.rebbit.service.CommunityService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
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
    public Optional<Community> findOneById(Long id) {
        return communityRepo.findById(id);
    }

    @Override
    public List<Community> getAll() {
        return null;
    }
}
