package com.tradingsimulator.dao;

import com.tradingsimulator.model.User;
import com.tradingsimulator.model.UserProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class JdbcUserProfileDaoTests extends BaseDaoTests{
    private static final UserProfile USER_PROFILE_1 = new UserProfile(1, 1, "user profile 1", "1231", "Example Address 1", "city", "ST", "00000");
    private static final UserProfile USER_PROFILE_2 = new UserProfile(2, 2, "user profile 2", "1232", "Example Address 2", "city", "ST", "00000");
    private static final UserProfile USER_PROFILE_3 = new UserProfile(3, 3, "user profile 3", "1233", "Example Address 3", "city", "ST", "00000");


    private static final User USER_1 = new User(1, "user1", "password", "ROLE_ADMIN");
    private JdbcUserProfileDao dao;

    @Before
    public void setup(){
        dao = new JdbcUserProfileDao(dataSource);
    }

    @Test
    public void getUserProfile_return_list_of_userProfile(){
        List<UserProfile> userProfiles = dao.getUserProfiles();

        Assert.assertNotNull("getUserProfiles return null list", userProfiles);
        Assert.assertEquals("getUserProfiles return correct number of profiles", 3, userProfiles.size());
        assertUserProfile("getUserProfiles return incorrect UserProfile data", USER_PROFILE_1, userProfiles.get(0));
        assertUserProfile("getUserProfiles return incorrect UserProfile data", USER_PROFILE_2, userProfiles.get(1));
        assertUserProfile("getUserProfiles return incorrect UserProfile data", USER_PROFILE_3, userProfiles.get(2));

    }
    @Test
    public void getUserProfileByUsername_with_valid_username_returns_correct_UserProfile(){
        UserProfile userProfile = dao.getUserProfileByUsername(USER_1.getUsername());

        Assert.assertNotNull("getUserProfileByUsername return null profile", userProfile);
        assertUserProfile("getUserProfuleByUsername return incorrect profile", USER_PROFILE_1, userProfile);
    }

    @Test
    public void getUserProfileByUsername_with_invalid_username_returns_null(){
        UserProfile userProfile = dao.getUserProfileByUsername("INVALID USERNAME");

        Assert.assertNull("getUserProfileByUsername with invalid username return existing profile, expected null", userProfile);
    }

    @Test
    public void getUserProfileById_with_valid_id_returns_correct_UserProfile(){
        UserProfile userProfile = dao.getUserProfileById(3);

        Assert.assertNotNull("getUserProfileById return null profile", userProfile);
        assertUserProfile("getUserProfuleById return incorrect profile", USER_PROFILE_3, userProfile);
    }

    @Test
    public void getUserProfileById_with_invalid_id_returns_null(){
        UserProfile userProfile = dao.getUserProfileById(5);

        Assert.assertNull("getUserProfileById with invalid id return existing profile, expected null", userProfile);
    }
    @Test
    public void createUserProfile_creates_a_UserProfile(){
        UserProfile newUserProfile = new UserProfile(4, "Name", "1234", "address", "city", "st", "00000");

        UserProfile userProfile = dao.createUserProfile(newUserProfile);
        Assert.assertNotNull("createUserProfile return null", userProfile);

        UserProfile actualProfile = dao.getUserProfileById(userProfile.getId());
        Assert.assertNotNull("getUserProfileById return null profile", actualProfile);

        newUserProfile.setId(actualProfile.getId());
        assertUserProfile("UserProfiles do not match", newUserProfile, actualProfile);
    }

    @Test
    public void updateUserProfile_updates_UserProfile(){
        UserProfile userProfileToUpdate = dao.getUserProfileById(USER_PROFILE_1.getId());
        userProfileToUpdate.setZipCode("99999");
        userProfileToUpdate.setState("NV");
        userProfileToUpdate.setPhoneNumber("575757575");
        userProfileToUpdate.setName("update name");

        UserProfile updateUserProfile = dao.updateUserProfile(userProfileToUpdate);
        UserProfile retrieveUserProfile = dao.getUserProfileById(USER_PROFILE_1.getId());

        assertUserProfile("updateUserProfile returned incorrect UserProfile data", updateUserProfile, retrieveUserProfile);
    }

    @Test
    public void delete_deletes_UserProfile(){
        int rowsAffected = dao.delete(3);

        Assert.assertEquals("Account was not deleted", 1, rowsAffected);
        UserProfile retrieveUserProfile = dao.getUserProfileById(3);
        Assert.assertNull("UserProfile can still be retrieved", retrieveUserProfile);
    }


    private void assertUserProfile(String message, UserProfile expected, UserProfile actual){
        Assert.assertEquals(message, expected.getId(), actual.getId());
        Assert.assertEquals(message, expected.getUserId(), actual.getUserId());
        Assert.assertEquals(message, expected.getName(), actual.getName());
        Assert.assertEquals(message, expected.getPhoneNumber(), actual.getPhoneNumber());
        Assert.assertEquals(message, expected.getAddress(), actual.getAddress());
        Assert.assertEquals(message, expected.getCity(), actual.getCity());
        Assert.assertEquals(message, expected.getState(), actual.getState());
        Assert.assertEquals(message, expected.getZipCode(), actual.getZipCode());
    }
}
