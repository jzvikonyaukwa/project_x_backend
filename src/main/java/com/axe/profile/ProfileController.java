package com.axe.profile;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("")
    public List<Profile> getAllProfiles(){
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public Profile getProfileById(@PathVariable  Long id){
        return profileService.getProfileById(id);
    }
}
