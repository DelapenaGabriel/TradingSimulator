package com.tradingsimulator.services;

import com.tradingsimulator.model.UserProfile;

import java.security.Principal;
import java.util.List;

public interface UserProfileService {
    List<UserProfile> getUserProfiles();
    UserProfile getCurrentUserProfile(Principal principal);
    UserProfile getUserProfileById(int id, Principal principal);

    UserProfile update(UserProfile userProfile, Principal principal);

    UserProfile create(UserProfile newUserProfile, Principal principal);

    int delete(int id, Principal principal);
}
