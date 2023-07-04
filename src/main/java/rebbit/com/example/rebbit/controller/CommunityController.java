package rebbit.com.example.rebbit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import rebbit.com.example.rebbit.dto.*;
import rebbit.com.example.rebbit.model.Community;
import rebbit.com.example.rebbit.model.IndexCommunity;
import rebbit.com.example.rebbit.service.CommunityService;
import rebbit.com.example.rebbit.service.PostService;
import rebbit.com.example.rebbit.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/community")
public class CommunityController {
    private final CommunityService commService;
    private final PostService postService;
    private final UserService userService;

    public CommunityController(CommunityService commService, PostService postService, UserService userService) {
        this.commService = commService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Secured({"USER_ROLE", "ADMIN", "MODERATOR"})
    public ResponseEntity<Long> createCommunity(@ModelAttribute CommunityDTO communityDTO) {
        Community community = new Community();
        LocalDate now = LocalDate.now();
        community.setCreationDate(now);
        community.setName(communityDTO.getName());
        community.setDescription(communityDTO.getDescription());
        Community communityCreated;
        if (communityDTO.getPdf() == null) {
            communityCreated = commService.save(community, null);
        } else {
            communityCreated = commService.save(community, communityDTO.getPdf());
        }
        return ResponseEntity.ok(communityCreated.getId());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GetCommunityDTO> getCommunity(@PathVariable Long id) {
        Optional<Community> community = commService.findOneById(id);
        if (community == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GetCommunityDTO communityDTO = new GetCommunityDTO(community.get().getName(), community.get().getDescription(), community.get().getCreationDate());
        return new ResponseEntity<>(communityDTO, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<ResponseCommunity>> getAll() {
        List<Community> communities = commService.getAll();
        List<ResponseCommunity> communityDTOS = new ArrayList<>();
        for (Community c : communities) {
           Long count = postService.findCount(c.getId());
           Long karma = postService.findKarma(c.getId());

            communityDTOS.add(new ResponseCommunity(c.getId(), c.getName(),count, karma,c.getDescription()));
        }
        return new ResponseEntity<>(communityDTOS, HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id){
        Optional<Community> community = commService.findOneById(id);
        if (community == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        commService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/search")
    public List<IndexCommunityDTO> searchCommunities(@RequestParam(value = "pdfContent", required = false) String pdfContent,
                                                     @RequestParam(value = "name", required = false) String name,
                                                     @RequestParam(value = "description", required = false) String description) {
        Iterable<IndexCommunity> indexCommunities = commService.searchCommunities(pdfContent, name, description);
        List<IndexCommunity> indextCommDTOS = new ArrayList<>();
        for (IndexCommunity indexCommunity : indexCommunities) {
            indextCommDTOS.add(indexCommunity);
        }
        System.out.println("OVDE CE ISPRINTATI");
        System.out.println(indextCommDTOS);
        return indextCommDTOS
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private IndexCommunityDTO toDto(IndexCommunity comm) {
        Long count = postService.findCount(comm.getId());
        Long karma = postService.findKarma(comm.getId());

        return new IndexCommunityDTO(comm.getId(), comm.getName(), comm.getDescription(), comm.getFileName(),count,karma);
    }

}
