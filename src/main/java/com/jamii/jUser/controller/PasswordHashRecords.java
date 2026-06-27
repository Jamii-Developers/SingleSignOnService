package com.jamii.jUser.controller;

import com.jamii.utils.JamiiStringUtils;
import com.jamii.jUser.model.PasswordHashRecordsTBL;
import com.jamii.jUser.model.UserLoginTBL;
import com.jamii.jUser.repo.PasswordHashRecordsREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service component for managing password hash records in the database.
 * 
 * <p>This class tracks password history for jUser, enforcing password reuse policies
 * by checking against the last 10 password hashes.</p>
 * 
 * <p>Key functionality:</p>
 * <ul>
 *     <li>Add new password hash records for jUser</li>
 *     <li>Check if a password has been used in the last 10 records</li>
 *     <li>Save password hash records individually or in batch</li>
 * </ul>
 */
@Component
public class PasswordHashRecords
{

    /**
     * Single password hash record instance for creating new entries.
     */
    public PasswordHashRecordsTBL data ;
    
    /**
     * List of password hash records for batch operations.
     */
    public ArrayList<PasswordHashRecordsTBL> dataList ;

    @Autowired private PasswordHashRecordsREPO passwordHashRecordsREPO;

    /**
     * Adds a new password hash record for a user.
     * 
     * <p>This method captures the current password salt from the user's login record
     * and stores it with a timestamp for password history tracking.</p>
     * 
     * @param userLoginTBL the user to add the password record for
     * @return the saved password hash record with generated ID
     */
    public PasswordHashRecordsTBL addUserNewPasswordRecord(UserLoginTBL userLoginTBL)
    {

        data.setPasswordsalt(userLoginTBL.getPasswordsalt());
        data.setUserloginid(userLoginTBL);
        data.setDateadded(LocalDateTime.now());

        return passwordHashRecordsREPO.save(data);
    }

    /**
     * Checks if the user's current password has been used in their last 10 password records.
     * 
     * <p>This method enforces password reuse policies by comparing the current password salt
     * against the last 10 password hashes stored for the user.</p>
     * 
     * @param userLoginTBL the user to check password history for
     * @return {@code true} if the password was used in the last 10 records, {@code false} otherwise
     */
    public Boolean isPasswordInLastTenRecords(UserLoginTBL userLoginTBL)
    {

        List<PasswordHashRecordsTBL> passwordHashRecords = passwordHashRecordsREPO.findLast10Passwords(userLoginTBL.getId());
        for (PasswordHashRecordsTBL passwordHashRecord : passwordHashRecords) {
            if (JamiiStringUtils.equals(passwordHashRecord.getPasswordsalt(), userLoginTBL.getPasswordsalt())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the current password hash record from the {@code data} field to the database.
     * 
     * <p>The saved entity (with generated ID) is stored back in the {@code data} field.</p>
     */
    public void save()
    {
        data = this.passwordHashRecordsREPO.save(data);
    }

    /**
     * Saves all password hash records in the {@code dataList} to the database in a batch operation.
     * 
     * <p>After saving, the list is cleared and repopulated with the saved entities
     * (including any generated IDs).</p>
     */
    public void saveAll()
    {
        Iterable<PasswordHashRecordsTBL> datalist = this.passwordHashRecordsREPO.saveAll(dataList);
        dataList.clear();
        datalist.forEach(x -> dataList.add(x));
    }
}
