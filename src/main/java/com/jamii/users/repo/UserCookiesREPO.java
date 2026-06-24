package com.jamii.users.repo;

import com.jamii.users.model.DeviceInformationTBL;
import com.jamii.users.model.UserCookiesTBL;
import com.jamii.users.model.UserLoginTBL;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link UserCookiesTBL} entities.
 * 
 * <p>This repository provides CRUD operations and custom queries for user cookie/session records.</p>
 */
public interface UserCookiesREPO
        extends CrudRepository<UserCookiesTBL, Integer>
{

    /**
     * Finds cookie records by user and active status.
     * @param userLoginTBL the user to search for
     * @param active the active status to match
     * @return list of matching cookie records
     */
    List<UserCookiesTBL> findByUserloginidAndActive(UserLoginTBL userLoginTBL, boolean active);

    /**
     * Finds cookie records by user, device, session key, and active status.
     * @param userLoginTBL the user to search for
     * @param device the device information to match
     * @param cookie the session key to match
     * @param active the active status to match
     * @return list of matching cookie records
     */
    List<UserCookiesTBL> findByUserloginidAndDeviceinformationidAndSessionkeyAndActive(UserLoginTBL userLoginTBL, DeviceInformationTBL device, String cookie, boolean active);
}
