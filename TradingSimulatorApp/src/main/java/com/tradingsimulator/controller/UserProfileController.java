package com.tradingsimulator.controller;

import com.tradingsimulator.exception.DaoException;
import com.tradingsimulator.model.UserProfile;
import com.tradingsimulator.services.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/profiles")
@RestController
public class UserProfileController {

    private UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public List<UserProfile> list(){
        List<UserProfile> userProfiles = new ArrayList<>();

        try{
            userProfiles = userProfileService.getUserProfiles();
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return userProfiles;

    }

    @GetMapping("")
    public UserProfile getCurrentProfile(Principal principal){
        UserProfile userProfile = null;

        try{
            userProfile = userProfileService.getCurrentUserProfile(principal);
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return userProfile;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public UserProfile get(@PathVariable int id, Principal principal){
        UserProfile userProfile = null;

        try{
            userProfile = userProfileService.getUserProfileById(id, principal);
            if (userProfile == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"); //Status Code: 404 = The given URL doesn't point to a valid resource
            }
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return userProfile;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public UserProfile add(@Valid @RequestBody UserProfile newUserProfile, Principal principal) {
        UserProfile userProfile = null;
        try {
            userProfile = userProfileService.create(newUserProfile, principal);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
        return userProfile;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/{id}")
    public void delete(@PathVariable int id, Principal principal){
        try{
            int deletedRows = userProfileService.delete(id, principal);
            if (deletedRows == 0){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"); //Status Code: 404 = The given URL doesn't point to a valid resource
            }
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); //Status Code: 500 = API itself has a problem and can't fulfill the request at this time
        }
    }


    @PutMapping("/{id}")
    public UserProfile update (@PathVariable int id, @Valid @RequestBody UserProfile updateUserProfile, Principal principal){
        updateUserProfile.setId(id);

        try{
            UserProfile updatedUserProfile = userProfileService.update(updateUserProfile, principal);
            return updatedUserProfile;
        }catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"); //Status Code: 404 = The given URL doesn't point to a valid resource
        }
    }


}
