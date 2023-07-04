package rebbit.com.example.rebbit.service;

import rebbit.com.example.rebbit.model.Reaction;
import rebbit.com.example.rebbit.service.impl.ReactionServiceImpl;

public interface ReactionService {


    Reaction findByUserId(Long postId, Long useId);

    Reaction save(Reaction reac);
}
