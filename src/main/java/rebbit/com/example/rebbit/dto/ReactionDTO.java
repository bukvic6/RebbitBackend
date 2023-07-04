package rebbit.com.example.rebbit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import rebbit.com.example.rebbit.model.Reaction;
import rebbit.com.example.rebbit.model.ReactionType;


@Data
@NoArgsConstructor
public class ReactionDTO {
    private ReactionType type;
    private Long postId;
    public ReactionDTO(Reaction reaction){
        type = reaction.getType();
    }
}
