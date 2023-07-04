package rebbit.com.example.rebbit.service.impl;


import org.springframework.stereotype.Service;
import rebbit.com.example.rebbit.model.Reaction;
import rebbit.com.example.rebbit.repository.ReactionRepo;
import rebbit.com.example.rebbit.service.ReactionService;

@Service
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepo reactionRepo;

    public ReactionServiceImpl(ReactionRepo reactionRepo) {
        this.reactionRepo = reactionRepo;
    }

    @Override
    public Reaction findByUserId(Long postId, Long useId) {
        return reactionRepo.findByUserId(postId, useId);
    }

    @Override
    public Reaction save(Reaction reac) {
        try {
            return reactionRepo.save(reac);
        }catch (IllegalArgumentException e){
            return null;
        }
    }
}
