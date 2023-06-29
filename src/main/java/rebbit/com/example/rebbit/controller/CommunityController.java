package rebbit.com.example.rebbit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rebbit.com.example.rebbit.dto.CommunityDTO;
import rebbit.com.example.rebbit.model.Community;
import rebbit.com.example.rebbit.model.Moderator;
import rebbit.com.example.rebbit.model.User;
import rebbit.com.example.rebbit.service.CommunityService;
import rebbit.com.example.rebbit.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public ResponseEntity<Long>createCommunity(@RequestBody CommunityDTO communityDTO, Authentication auth){
        Community community = new Community();
        LocalDate now = LocalDate.now();
        community.setCreationDate(now);
        community.setName(communityDTO.getName());
        community.setDescription(communityDTO.getDescription());
        User moderator = userService.findByUsername(auth.getName());
        community.setModerators((Set<Moderator>) moderator);
        Community communityCreated = commService.save(community);
        return ResponseEntity.ok(communityCreated.getId());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommunityDTO>getCommunity(@PathVariable Long id){
        Community community = commService.findOneById(id);
        if (community == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CommunityDTO communityDTO = new CommunityDTO(community.getName(), community.getDescription());
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



}
