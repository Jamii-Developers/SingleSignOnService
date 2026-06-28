package com.jamii.jUser.model;

import com.jamii.jDrive.model.FileDirectoryTBL;
import com.jamii.jDrive.model.FileTableOwnerTBL;
import com.jamii.jSocial.model.UserBlockListTBL;
import com.jamii.jSocial.model.UserGroupsTBL;
import com.jamii.jSocial.model.UserRelationshipTBL;
import com.jamii.jSocial.model.UserRequestsTBL;
import com.jamii.jSocial.model.UserRolesTBL;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Entity representing user login credentials and account information in the database.
 * 
 * <p>This class maps to the {@code user_login} table and is the central user entity with numerous
 * relationships to other tables including user data, devices, passwords, groups, roles, files,
 * cookies, relationships, requests, and block lists.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code username} - Unique username</li>
 *     <li>{@code emailaddress} - Unique email address</li>
 *     <li>{@code passwordsalt} - Hashed password</li>
 *     <li>{@code userkey} - User key for authentication</li>
 *     <li>{@code active} - Account active status</li>
 *     <li>{@code privacy} - Privacy settings</li>
 *     <li>{@code datecreated} - Account creation timestamp</li>
 * </ul>
 */
@Entity
@Table(name = "user_login")
public class UserLoginTBL
{

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "user_login";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for username.
     */
    public static final String USERNAME = "USERNAME";
    
    /**
     * Column name constant for email address.
     */
    public static final String EMAIL_ADDRESS = "EMAIL_ADDRESS";
    
    /**
     * Column name constant for password salt.
     */
    public static final String PASSWORD_SALT = "PASSWORD_SALT";
    
    /**
     * Column name constant for user key.
     */
    public static final String USER_KEY = "USER_KEY";
    
    /**
     * Column name constant for active status.
     */
    public static final String ACTIVE = "ACTIVE";
    
    /**
     * Column name constant for date created.
     */
    public static final String DATE_CREATED = "DATE_CREATED";
    
    /**
     * Column name constant for privacy settings.
     */
    public static final String PRIVACY = "PRIVACY";
    
    /**
     * Column name constant for auxiliary data.
     */
    public static final String AUX_DATA = "AUX_DATA";

    /**
     * Primary key identifier.
     */
    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * Unique username.
     */
    @Column(name = USERNAME, nullable = false, length = 50, unique = true) private String username;
    
    /**
     * Unique email address.
     */
    @Column(name = EMAIL_ADDRESS, nullable = false, length = 200, unique = true) private String emailaddress;
    
    /**
     * Hashed password.
     */
    @Column(name = PASSWORD_SALT, nullable = false, length = 200) private String passwordsalt;
    
    /**
     * User key for authentication.
     */
    @Column(name = USER_KEY, nullable = false, length = 1000) private String userkey;
    
    /**
     * Account active status.
     */
    @Column(name = ACTIVE, nullable = false) private Integer active;
    
    /**
     * Privacy settings.
     */
    @Column(name = PRIVACY, nullable = false) private Integer privacy;
    
    /**
     * Account creation timestamp.
     */
    @Column(name = DATE_CREATED, nullable = false) private LocalDateTime datecreated;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;
    
    /**
     * One-to-one relationship to user profile data.
     */
    @OneToOne( mappedBy = "userloginid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private UserDataTBL userData;
    
    /**
     * One-to-many relationship to device information records.
     */
    @OneToMany(mappedBy = "userloginid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE) private List<DeviceInformationTBL> deviceInformation;
    
    /**
     * One-to-many relationship to password hash records.
     */
    @OneToMany(mappedBy = "userloginid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<PasswordHashRecordsTBL> passwordHashRecord;
    
    /**
     * One-to-many relationship to user groups.
     */
    @OneToMany(mappedBy = "userloginid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<UserGroupsTBL> userGroupsTBL;
    
    /**
     * One-to-many relationship to user roles.
     */
    @OneToMany(mappedBy = "userloginid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<UserRolesTBL> userRolesTBL;
    
    /**
     * One-to-many relationship to file ownership records.
     */
    @OneToMany(mappedBy = "userloginid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<FileTableOwnerTBL> fileTableOwnerTBL;
    
    /**
     * One-to-many relationship to file directory records.
     */
    @OneToMany(mappedBy = "userloginid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<FileDirectoryTBL> fileDirectoryTBL;
    
    /**
     * One-to-many relationship to user cookie/session records.
     */
    @OneToMany(mappedBy = "userloginid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<UserCookiesTBL> userCookiesTBL;
    
    /**
     * One-to-many relationship to user relationships where this user is the sender.
     */
    @OneToMany(mappedBy = "senderid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<UserRelationshipTBL> senderIDuserRelationshipTBLS;
    
    /**
     * One-to-many relationship to user relationships where this user is the receiver.
     */
    @OneToMany(mappedBy = "receiverid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<UserRelationshipTBL> receiverIDuserRelationshipTBLS;
    
    /**
     * One-to-many relationship to user requests where this user is the sender.
     */
    @OneToMany(mappedBy = "senderid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<UserRequestsTBL> senderIDUserRequests;
    
    /**
     * One-to-many relationship to user requests where this user is the receiver.
     */
    @OneToMany(mappedBy = "receiverid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<UserRequestsTBL> receiverIDUserRequests;
    
    /**
     * One-to-many relationship to block list where this user blocked someone.
     */
    @OneToMany(mappedBy = "userid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<UserBlockListTBL> useridUserBlockListTBL;
    
    /**
     * One-to-many relationship to block list where this user was blocked.
     */
    @OneToMany(mappedBy = "blockedid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE)private List<UserBlockListTBL> blockedidUserBlockListTBL;

    /**
     * Default constructor.
     */
    public UserLoginTBL() {}

    /**
     * Gets the primary key ID.
     * @return the ID
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets the primary key ID.
     * @param id the ID to set
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * Gets the ID as a string.
     * @return the ID as a string
     */
    public String getIdAsString()
    {
        return String.valueOf(id);
    }

    /**
     * Gets the username.
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username.
     * @param username the username to set
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Gets the email address.
     * @return the email address
     */
    public String getEmailaddress()
    {
        return emailaddress;
    }

    /**
     * Sets the email address.
     * @param emailaddress the email address to set
     */
    public void setEmailaddress(String emailaddress)
    {
        this.emailaddress = emailaddress;
    }

    /**
     * Sets the email address (alternative method name).
     * @param emailaddress the email address to set
     */
    public void setEmailAddress(String emailaddress)
    {
        this.emailaddress = emailaddress;
    }

    /**
     * Gets the hashed password.
     * @return the password salt
     */
    public String getPasswordsalt()
    {
        return passwordsalt;
    }

    /**
     * Sets the hashed password.
     * @param passwordsalt the password salt to set
     */
    public void setPasswordsalt(String passwordsalt)
    {
        this.passwordsalt = passwordsalt;
    }

    /**
     * Gets the active status.
     * @return the active status
     */
    public Integer getActive()
    {
        return active;
    }

    /**
     * Sets the active status.
     * @param active the active status to set
     */
    public void setActive(Integer active)
    {
        this.active = active;
    }

    /**
     * Gets the user key.
     * @return the user key
     */
    public String getUserKey()
    {
        return userkey;
    }

    /**
     * Sets the user key.
     * @param userkey the user key to set
     */
    public void setUserKey(String userkey)
    {
        this.userkey = userkey;
    }

    /**
     * Gets the account creation timestamp.
     * @return the date created
     */
    public LocalDateTime getDatecreated()
    {
        return datecreated;
    }

    /**
     * Sets the account creation timestamp.
     * @param datecreated the date created to set
     */
    public void setDatecreated(LocalDateTime datecreated)
    {
        this.datecreated = datecreated;
    }

    /**
     * Gets the privacy settings.
     * @return the privacy settings
     */
    public Integer getPrivacy()
    {
        return privacy;
    }

    /**
     * Sets the privacy settings.
     * @param privacy the privacy settings to set
     */
    public void setPrivacy(Integer privacy)
    {
        this.privacy = privacy;
    }

    /**
     * Gets the user key (alternative method name).
     * @return the user key
     */
    public String getUserkey()
    {
        return userkey;
    }

    /**
     * Sets the user key (alternative method name).
     * @param userkey the user key to set
     */
    public void setUserkey(String userkey)
    {
        this.userkey = userkey;
    }

    /**
     * Gets the auxiliary data.
     * @return the auxiliary data
     */
    public String getAuxdata()
    {
        return auxdata;
    }

    /**
     * Sets the auxiliary data.
     * @param auxdata the auxiliary data to set
     */
    public void setAuxdata(String auxdata)
    {
        this.auxdata = auxdata;
    }

    /**
     * Gets the list of device information records.
     * @return the list of device information
     */
    public List<DeviceInformationTBL> getDeviceInformation()
    {
        return deviceInformation;
    }

    /**
     * Sets the list of device information records.
     * @param deviceInformation the list of device information to set
     */
    public void setDeviceInformation(List<DeviceInformationTBL> deviceInformation)
    {
        this.deviceInformation = deviceInformation;
    }

    /**
     * Sets the user profile data.
     * @param userData the user data entity to set
     */
    public void setUserData(UserDataTBL userData)
    {
        this.userData = userData;
    }

    /**
     * Gets the list of user relationships where this user is the sender.
     * @return the list of user relationships
     */
    public List<UserRelationshipTBL> getSenderIDuserRelationshipTBLS()
    {
        return senderIDuserRelationshipTBLS;
    }

    /**
     * Gets the list of password hash records.
     * @return the list of password hash records
     */
    public List<PasswordHashRecordsTBL> getPasswordHashRecord()
    {
        return passwordHashRecord;
    }

    /**
     * Sets the list of password hash records.
     * @param passwordHashRecord the list of password hash records to set
     */
    public void setPasswordHashRecord(List<PasswordHashRecordsTBL> passwordHashRecord)
    {
        this.passwordHashRecord = passwordHashRecord;
    }

    /**
     * Gets the list of user groups.
     * @return the list of user groups
     */
    public List<UserGroupsTBL> getUserGroupsTBL()
    {
        return userGroupsTBL;
    }

    /**
     * Sets the list of user groups.
     * @param userGroupsTBL the list of user groups to set
     */
    public void setUserGroupsTBL(List<UserGroupsTBL> userGroupsTBL)
    {
        this.userGroupsTBL = userGroupsTBL;
    }

    /**
     * Gets the list of user roles.
     * @return the list of user roles
     */
    public List<UserRolesTBL> getUserRolesTBL()
    {
        return userRolesTBL;
    }

    /**
     * Sets the list of user roles.
     * @param userRolesTBL the list of user roles to set
     */
    public void setUserRolesTBL(List<UserRolesTBL> userRolesTBL)
    {
        this.userRolesTBL = userRolesTBL;
    }

    /**
     * Gets the list of file ownership records.
     * @return the list of file ownership records
     */
    public List<FileTableOwnerTBL> getFileTableOwnerTBL()
    {
        return fileTableOwnerTBL;
    }

    /**
     * Sets the list of file ownership records.
     * @param fileTableOwnerTBL the list of file ownership records to set
     */
    public void setFileTableOwnerTBL(List<FileTableOwnerTBL> fileTableOwnerTBL)
    {
        this.fileTableOwnerTBL = fileTableOwnerTBL;
    }

    /**
     * Gets the list of file directory records.
     * @return the list of file directory records
     */
    public List<FileDirectoryTBL> getFileDirectoryTBL()
    {
        return fileDirectoryTBL;
    }

    /**
     * Sets the list of file directory records.
     * @param fileDirectoryTBL the list of file directory records to set
     */
    public void setFileDirectoryTBL(List<FileDirectoryTBL> fileDirectoryTBL)
    {
        this.fileDirectoryTBL = fileDirectoryTBL;
    }

    /**
     * Gets the list of user cookie/session records.
     * @return the list of user cookies
     */
    public List<UserCookiesTBL> getUserCookies()
    {
        return userCookiesTBL;
    }

    /**
     * Sets the list of user cookie/session records.
     * @param userCookiesTBL the list of user cookies to set
     */
    public void setUserCookies(List<UserCookiesTBL> userCookiesTBL)
    {
        this.userCookiesTBL = userCookiesTBL;
    }

    /**
     * Gets the list of user relationships where this user is the sender (alternative method name).
     * @return the list of user relationships
     */
    public List<UserRelationshipTBL> getSenderIDuserRelationship()
    {
        return senderIDuserRelationshipTBLS;
    }

    /**
     * Sets the list of user relationships where this user is the sender.
     * @param senderIDuserRelationshipTBLS the list of user relationships to set
     */
    public void setSenderIDuserRelationshipTBLS(List<UserRelationshipTBL> senderIDuserRelationshipTBLS)
    {
        this.senderIDuserRelationshipTBLS = senderIDuserRelationshipTBLS;
    }

    /**
     * Gets the list of user relationships where this user is the receiver.
     * @return the list of user relationships
     */
    public List<UserRelationshipTBL> getReceiverIDuserRelationshipTBLS()
    {
        return receiverIDuserRelationshipTBLS;
    }

    /**
     * Sets the list of user relationships where this user is the receiver.
     * @param receiverIDuserRelationshipTBLS the list of user relationships to set
     */
    public void setReceiverIDuserRelationshipTBLS(List<UserRelationshipTBL> receiverIDuserRelationshipTBLS)
    {
        this.receiverIDuserRelationshipTBLS = receiverIDuserRelationshipTBLS;
    }

    /**
     * Gets the list of user requests where this user is the sender.
     * @return the list of user requests
     */
    public List<UserRequestsTBL> getSenderIDUserRequests()
    {
        return senderIDUserRequests;
    }

    /**
     * Sets the list of user requests where this user is the sender.
     * @param senderIDUserRequests the list of user requests to set
     */
    public void setSenderIDUserRequests(List<UserRequestsTBL> senderIDUserRequests)
    {
        this.senderIDUserRequests = senderIDUserRequests;
    }

    /**
     * Gets the list of user requests where this user is the receiver.
     * @return the list of user requests
     */
    public List<UserRequestsTBL> getReceiverIDUserRequests()
    {
        return receiverIDUserRequests;
    }

    /**
     * Sets the list of user requests where this user is the receiver.
     * @param receiverIDUserRequests the list of user requests to set
     */
    public void setReceiverIDUserRequests(List<UserRequestsTBL> receiverIDUserRequests)
    {
        this.receiverIDUserRequests = receiverIDUserRequests;
    }

    /**
     * Gets the list of block list records where this user blocked someone.
     * @return the list of block list records
     */
    public List<UserBlockListTBL> getUseridUserBlockListTBL()
    {
        return useridUserBlockListTBL;
    }

    /**
     * Sets the list of block list records where this user blocked someone.
     * @param useridUserBlockListTBL the list of block list records to set
     */
    public void setUseridUserBlockListTBL(List<UserBlockListTBL> useridUserBlockListTBL)
    {
        this.useridUserBlockListTBL = useridUserBlockListTBL;
    }

    /**
     * Gets the list of block list records where this user was blocked.
     * @return the list of block list records
     */
    public List<UserBlockListTBL> getBlockedidUserBlockListTBL()
    {
        return blockedidUserBlockListTBL;
    }

    /**
     * Sets the list of block list records where this user was blocked.
     * @param blockedidUserBlockListTBL the list of block list records to set
     */
    public void setBlockedidUserBlockListTBL(List<UserBlockListTBL> blockedidUserBlockListTBL)
    {
        this.blockedidUserBlockListTBL = blockedidUserBlockListTBL;
    }

    /**
     * Gets the user profile data.
     * @return the user data entity
     */
    public UserDataTBL getUserData()
    {
        return userData;
    }
}
