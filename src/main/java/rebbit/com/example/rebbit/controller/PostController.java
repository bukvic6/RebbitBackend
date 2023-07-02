package rebbit.com.example.rebbit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rebbit.com.example.rebbit.dto.IndexPostDTO;
import rebbit.com.example.rebbit.dto.PostDTO;
import rebbit.com.example.rebbit.dto.PostResponse;
import rebbit.com.example.rebbit.model.Community;
import rebbit.com.example.rebbit.model.IndexPost;
import rebbit.com.example.rebbit.model.Post;
import rebbit.com.example.rebbit.model.User;
import rebbit.com.example.rebbit.service.CommunityService;
import rebbit.com.example.rebbit.service.PostService;
import rebbit.com.example.rebbit.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
public class PostController {

    private final PostService postService;
    private final CommunityService communityService;
    private final UserService userService;

    public PostController(PostService postService, CommunityService communityService, UserService userService) {
        this.postService = postService;
        this.communityService = communityService;
        this.userService = userService;
    }

    @Secured({"USER_ROLE","ADMIN","MODERATOR"})
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Long>createPost(@RequestBody PostDTO postDTO, Authentication auth){
        if(postDTO.getCommunityDTO() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Community> community = communityService.findOneById(postDTO.getCommunityDTO());
        if(community == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Post post = new Post();
        post.setCommunity(community.get());
        post.setText(postDTO.getText());
        post.setTitle(postDTO.getTitle());
        LocalDate now = LocalDate.now();
        post.setCreationDate(now);

        User user = userService.findByUsername(auth.getName());
        post.setUser(user);
        Post created = postService.createPost(post);
        System.out.println("----------------------------------------------");
        System.out.println(user.getEmail());
        return ResponseEntity.ok(created.getId());
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        List<Post> posts = postService.findAll();
        List<PostResponse> postDTOS = new ArrayList<>();
        for(Post p : posts){
            postDTOS.add(new PostResponse(p.getId(),p.getTitle(), p.getText(),p.getCreationDate(),p.getUser().getUsername(),
                    p.getKarma(),p.getCommunity().getName()));
        }
        return new ResponseEntity<>(postDTOS,HttpStatus.OK);
    }
    @GetMapping(value = "/communityPosts/{id}")
    public ResponseEntity<List<PostResponse>> getCommunityPosts(@PathVariable Long id){
        List<Post> posts = postService.findAllByCommunity(id);

        List<PostResponse> postDTOS = new ArrayList<>();
        for (Post p : posts){
            postDTOS.add(new PostResponse(p.getId(),p.getTitle(), p.getText(),p.getCreationDate(),p.getUser().getUsername(),
                    p.getKarma(),p.getCommunity().getName()));
        }
        return new ResponseEntity<>(postDTOS,HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostResponse>getPost(@PathVariable Long id){
        Post post = postService.findOneById(id);
        if (post == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new PostResponse(post.getId(),post.getTitle(), post.getText(), post.getCreationDate(),
                post.getUser().getUsername(),post.getKarma(),post.getCommunity().getName()), HttpStatus.OK);
    }


    @GetMapping("/search")
    public List<IndexPostDTO> searchPosts(@RequestParam(value = "pdfContent", required = false) String pdfContent,
                                          @RequestParam(value = "title", required = false) String title,
                                          @RequestParam(value = "text", required = false) String text) {
        Iterable<IndexPost> indexPosts = postService.searchPosts(pdfContent, title, text);
        List<IndexPost> indexPostList = new ArrayList<>();
        for (IndexPost indexPost : indexPosts) {
            indexPostList.add(indexPost);
        }
        return indexPostList
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private IndexPostDTO toDto(IndexPost indexPost) {
        return new IndexPostDTO(indexPost.getId(), indexPost.getFileName(), indexPost.getTitle(), indexPost.getText());
    }
}
