package com.tradingsimulator.dao;

import com.tradingsimulator.exception.DaoException;
import com.tradingsimulator.model.UserProfile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcUserProfileDao implements UserProfileDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserProfileDao (DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<UserProfile> getUserProfiles() {
        List<UserProfile> userProfiles = new ArrayList<>();

        String sql = "SELECT id, user_id, name, phone_number, address, city, state, zip_code, avatar_url " +
                "FROM user_profile " +
                "ORDER BY user_id;";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
            while(result.next()){
                userProfiles.add(mapRowToUserProfile(result));
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userProfiles;
    }

    @Override
    public UserProfile getUserProfileByUsername(String username) {
        UserProfile userProfile = null;

        String sql = "SELECT up.id, up.user_id, up.name, up.phone_number, up.address, up.city, up.state, up.zip_code, up.avatar_url " +
                "FROM user_profile up " +
                "JOIN users u ON up.user_id = u.id " +
                "WHERE u.username = ?;";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
            if (result.next()){
                userProfile = mapRowToUserProfile(result);
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userProfile;
    }

    @Override
    public UserProfile getUserProfileById(int id) {
        UserProfile userProfile = null;

        String sql = "SELECT id, user_id, name, phone_number, address, city, state, zip_code, avatar_url " +
                "FROM user_profile " +
                "WHERE id = ?;";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
            if (result.next()){
                userProfile = mapRowToUserProfile(result);
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userProfile;
    }

    @Override
    public UserProfile updateUserProfile(UserProfile updateUserProfile) {
        UserProfile userProfile = null;

        String sql = "UPDATE user_profile " +
                "SET name = ?, phone_number = ?, address = ?, city = ?, state = ?, zip_code = ?, avatar_url = ? " +
                "WHERE id = ?;";

        try{
            int rowsAffected = jdbcTemplate.update(sql, updateUserProfile.getName(), updateUserProfile.getPhoneNumber(),
                    updateUserProfile.getAddress(), updateUserProfile.getCity(), updateUserProfile.getState(), updateUserProfile.getZipCode(), updateUserProfile.getAvatarUrl(), updateUserProfile.getId());
            if (rowsAffected == 0){
                throw new DaoException("Zero rows affected, expected at least one");
            }

            userProfile = getUserProfileById(updateUserProfile.getId());

        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return userProfile;
    }

    @Override
    public UserProfile createUserProfile(UserProfile newUserProfile) {
        UserProfile userProfile = null;
        int newId;

        String sql = "INSERT INTO user_profile (user_id, name, phone_number, address, city, state, zip_code, avatar_url) " +
                "VALUES (?,?,?,?,?,?,?,?) RETURNING id;";

        try{
            newId = jdbcTemplate.queryForObject(sql, int.class, newUserProfile.getUserId(), newUserProfile.getName(), newUserProfile.getPhoneNumber(),
                    newUserProfile.getAddress(), newUserProfile.getCity(), newUserProfile.getState(), newUserProfile.getZipCode(), newUserProfile.getAvatarUrl());
            userProfile = getUserProfileById(newId);
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return userProfile;
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM user_profile WHERE id = ?;";
        try{
            return jdbcTemplate.update(sql, id);
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private UserProfile mapRowToUserProfile(SqlRowSet rowSet){
        UserProfile userProfile = new UserProfile();
        userProfile.setId(rowSet.getInt("id"));
        userProfile.setUserId(rowSet.getInt("user_id"));
        userProfile.setName(rowSet.getString("name"));
        userProfile.setPhoneNumber(rowSet.getString("phone_number"));
        userProfile.setAddress(rowSet.getString("address"));
        userProfile.setCity(rowSet.getString("city"));
        userProfile.setState(rowSet.getString("state"));
        userProfile.setZipCode(rowSet.getString("zip_code"));
        userProfile.setAvatarUrl(rowSet.getString("avatar_url"));
        return userProfile;
    }
}
