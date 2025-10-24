package com.tradingsimulator.services;

import com.tradingsimulator.dao.UserDao;
import com.tradingsimulator.dao.UserProfileDao;
import com.tradingsimulator.model.User;
import com.tradingsimulator.model.UserProfile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class RestUserProfileService implements UserProfileService{
    private UserProfileDao userProfileDao;
    private UserDao userDao;


    public RestUserProfileService (UserProfileDao userProfileDao, UserDao userDao){
        this.userProfileDao = userProfileDao;
        this.userDao = userDao;
    }

    @Override
    public List<UserProfile> getUserProfiles() {
        return userProfileDao.getUserProfiles();
    }

    @Override
    public UserProfile getCurrentUserProfile(Principal principal) {
        User user = getUser(principal);

        UserProfile userProfile = userProfileDao.getUserProfileByUsername(user.getUsername());

        if (userProfile == null){
            return null;
        }

        if (user.getId() == userProfile.getUserId()){
            return userProfile;
        }
        else{
            throw new AccessDeniedException("Access denied");
        }
    }


    @Override
    public UserProfile getUserProfileById(int id, Principal principal) {
        UserProfile userProfile = userProfileDao.getUserProfileById(id);
        return userProfile;
    }

    @Override
    public UserProfile update(UserProfile updateUserProfile, Principal principal) {
        UserProfile userProfile = null;

        UserProfile existingProfile = userProfileDao.getUserProfileById(updateUserProfile.getId());
        if (existingProfile != null){
            User user = getUser(principal);

            if(user != null){
                if (existingProfile.getUserId() == user.getId()){
                    updateUserProfile.setUserId(existingProfile.getUserId());
                    userProfile = userProfileDao.updateUserProfile(updateUserProfile);
                }
                else{
                    throw new AccessDeniedException("Access denied");
                }
            }
        }
        return userProfile;
    }

    @Override
    public UserProfile create(UserProfile newUserProfile, Principal principal) {
        UserProfile userProfile = null;

        User user = getUser(principal);

        if (user != null) {
                newUserProfile.setUserId(user.getId());
                userProfile = userProfileDao.createUserProfile(newUserProfile);
        }
        return userProfile;
    }

    @Override
    public int delete(int id, Principal principal) {
        User user = getUser(principal);
        return userProfileDao.delete(id);
    }

    private boolean isAdminUser(User user) {
        return user.getRole().equalsIgnoreCase("ROLE_ADMIN");
    }

    private User getUser(Principal principal){
        return userDao.getUserByUsername(principal.getName());
    }
}
