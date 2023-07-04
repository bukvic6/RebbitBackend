package rebbit.com.example.rebbit.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rebbit.com.example.rebbit.dto.ReactionDTO;
import rebbit.com.example.rebbit.model.Post;
import rebbit.com.example.rebbit.model.Reaction;
import rebbit.com.example.rebbit.model.User;
import rebbit.com.example.rebbit.service.PostService;
import rebbit.com.example.rebbit.service.ReactionService;
import rebbit.com.example.rebbit.service.UserService;

import java.time.LocalDate;

import static rebbit.com.example.rebbit.model.ReactionType.UPVOTE;

@RestController
@RequestMapping(value = "/api/reaction")
public class ReactionController {

    private final ReactionService reactionService;
    private final UserService userService;
    private final PostService postService;

    public ReactionController(ReactionService reactionService, UserService userService, PostService postService) {
        this.reactionService = reactionService;
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ReactionDTO>reaction(@RequestBody ReactionDTO reactionDTO, Authentication auth){
        if(reactionDTO.getPostId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Post post = postService.findOneById(reactionDTO.getPostId());
        if (post == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userService.findByUsername(auth.getName());

        Reaction reac = reactionService.findByUserId(post.getId(),user.getId());
        if(reac != null){
            if (reac.getType().toString() == "UPVOTE"){
                post.setKarma(post.getKarma() - 1);
            }
            if (reac.getType().toString() == "DOWNVOTE"){
                post.setKarma(post.getKarma() + 1);
            }
            if(UPVOTE.equals(reactionDTO.getType())){
                post.setKarma(post.getKarma() + 1);
            } else {

                post.setKarma(post.getKarma() - 1);
            }
            postService.save(post);
            reac.setType(reactionDTO.getType());
            reac = reactionService.save(reac);

            return new ResponseEntity<>(new ReactionDTO(reac), HttpStatus.CREATED);
        }
        if(UPVOTE.equals(reactionDTO.getType())){
            post.setKarma(post.getKarma() + 1);
        } else {

            post.setKarma(post.getKarma() - 1);
        }
        postService.save(post);

//            User user = userService.findByUsername("mika");

        Reaction reaction = new Reaction();
        LocalDate lt = LocalDate.now();
        reaction.setTime(lt);
        reaction.setType(reactionDTO.getType());
        reaction.setUser(user);
        reaction.setPost(post);
        reaction = reactionService.save(reaction);
        return new ResponseEntity<>(new ReactionDTO(reaction), HttpStatus.CREATED);
        }
}
