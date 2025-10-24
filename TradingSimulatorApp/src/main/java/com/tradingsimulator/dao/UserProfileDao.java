package com.tradingsimulator.dao;

import com.tradingsimulator.model.UserProfile;

import java.util.List;

public interface UserProfileDao {
    List<UserProfile> getUserProfiles ();
    UserProfile getUserProfileByUsername(String username);
    UserProfile getUserProfileById(int id);

    UserProfile updateUserProfile(UserProfile userProfile);

    UserProfile createUserProfile(UserProfile newUserProfile);

    int delete(int id);

}
