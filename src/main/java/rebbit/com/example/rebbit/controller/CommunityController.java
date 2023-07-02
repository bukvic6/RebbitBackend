package rebbit.com.example.rebbit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rebbit.com.example.rebbit.dto.CommunityDTO;
import rebbit.com.example.rebbit.dto.IndexCommunityDTO;
import rebbit.com.example.rebbit.model.Community;
import rebbit.com.example.rebbit.model.IndexCommunity;
import rebbit.com.example.rebbit.model.Moderator;
import rebbit.com.example.rebbit.model.User;
import rebbit.com.example.rebbit.service.CommunityService;
import rebbit.com.example.rebbit.service.UserService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/community")
public class CommunityController {
    private final CommunityService commService;
    private final UserService userService;

    public CommunityController(CommunityService commService, UserService userService) {
        this.commService = commService;
        this.userService = userService;
    }
    @PostMapping("/create")
    @Secured({"USER_ROLE", "ADMIN", "MODERATOR"})
    public ResponseEntity<Long>createCommunity(@RequestBody CommunityDTO communityDTO){
        Community community = new Community();
        LocalDate now = LocalDate.now();
        community.setCreationDate(now);
        community.setName(communityDTO.getName());
        community.setDescription(communityDTO.getDescription());
        Community communityCreated = commService.save(community);
        return ResponseEntity.ok(communityCreated.getId());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommunityDTO>getCommunity(@PathVariable Long id){
        Optional<Community> community = commService.findOneById(id);
        if (community == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CommunityDTO communityDTO = new CommunityDTO(community.get().getName(), community.get().getDescription());
        return new ResponseEntity<>(communityDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommunityDTO>> getAll(){
        List<Community> communities = commService.getAll();
        List<CommunityDTO> communityDTOS = new ArrayList<>();
        for (Community  c : communities){
            communityDTOS.add(new CommunityDTO(c.getName(),c.getDescription()));
        }
        return new ResponseEntity<>(communityDTOS, HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<IndexCommunityDTO> searchCommunities(@RequestParam(value = "pdfContent", required = false) String pdfContent,
                                                  @RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "description", required = false) String description){
        Iterable<IndexCommunity> indexCommunities = commService.searchCommunities(pdfContent,name, description);
        List<IndexCommunity> indextCommDTOS = new ArrayList<>();
        for (IndexCommunity indexCommunity : indexCommunities){
            indextCommDTOS.add(indexCommunity);
        }
        return indextCommDTOS
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private IndexCommunityDTO toDto(IndexCommunity comm) {
        return new IndexCommunityDTO(comm.getId(), comm.getName(), comm.getDescription(), comm.getFileName());
    }

}
