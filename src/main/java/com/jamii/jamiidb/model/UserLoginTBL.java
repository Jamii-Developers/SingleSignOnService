package com.jamii.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table( name = "user_login" , schema =  "jamiidb")
public class UserLoginTBL {

    public UserLoginTBL( ) { }

    public static final String              TABLE_NAME      = "user_login";

    public static final String              ID              = "ID";
    public static final String              USERNAME        = "USERNAME";
    public static final String              EMAIL_ADDRESS   = "EMAIL_ADDRESS";
    public static final String              PASSWORD_SALT   = "PASSWORD_SALT";
    public static final String              USER_KEY        = "USER_KEY";
    public static final String              ACTIVE          = "ACTIVE";
    public static final String              DATE_CREATED    = "DATE_CREATED";
    public static final String              PRIVACY         = "PRIVACY";
    public static final String              AUX_DATA        = "AUX_DATA";

    @Id
    @Column( name = ID )
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = USERNAME , nullable = false, length = 50, unique = true)
    private String username;

    @Column( name = EMAIL_ADDRESS, nullable = false, length = 200, unique = true )
    private String emailaddress;

    @Column(   name = PASSWORD_SALT , nullable = false, length = 200 )
    private String passwordsalt;

    @Column( name = USER_KEY , nullable = false, length = 1000 )
    private String userkey;

    @Column( name = ACTIVE, nullable = false )
    private Integer active;

    @Column( name = PRIVACY, nullable = false )
    private Integer privacy;

    @Column( name = DATE_CREATED, nullable = false )
    private LocalDateTime datecreated;

    @Column( name = AUX_DATA, columnDefinition = "text" )
    private String auxdata;

    //Foreign Keys
    @OneToMany( mappedBy = "userloginid" )
    private List<DeviceInformationTBL> deviceInformationTBL;

    @OneToMany( mappedBy = "userloginid" )
    private List<UserDataTBL> userDataTBL;

    @OneToMany( mappedBy = "userloginid" )
    private List<PasswordHashRecordsTBL> passwordHashRecordsTBL;

    @OneToMany( mappedBy = "userloginid" )
    private List<UserGroupsTBL> userGroupsTBL;

    @OneToMany( mappedBy = "userloginid" )
    private List<UserRolesTBL> userRolesTBL;

    @OneToMany( mappedBy = "userloginid" )
    private List<FileTableOwnerTBL> fileTableOwnerTBL;

    @OneToMany( mappedBy = "userloginid" )
    private List<FileDirectoryTBL> fileDirectoryTBL;

    @OneToMany( mappedBy = "userloginid" )
    private List<UserCookiesTBL> userCookiesTBL;

    @OneToMany( mappedBy = "senderid" )
    private List<UserRelationshipTBL> senderIDuserRelationshipTBLS;

    @OneToMany( mappedBy = "senderid" )
    private List<UserRequestsTBL> senderIDUserRequests;

    @OneToMany( mappedBy = "receiverid" )
    private List<UserRequestsTBL> receiverIDUserRequests;

    @OneToMany( mappedBy = "userid" )
    private List<UserBlockListTBL> useridUserBlockListTBL;

    @OneToMany( mappedBy = "blockedid" )
    private List<UserBlockListTBL> blockedidUserBlockListTBL;



    public Integer getId() {
        return id;
    }

    public String getIdAsString( ){
        return String.valueOf( id );
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailAddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPasswordsalt() {
        return passwordsalt;
    }

    public void setPasswordsalt(String passwordsalt) {
        this.passwordsalt = passwordsalt;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getUserKey() {
        return userkey;
    }

    public void setUserKey(String userkey) {
        this.userkey = userkey;
    }

    public LocalDateTime getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(LocalDateTime datecreated) {
        this.datecreated = datecreated;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getAuxdata() {
        return auxdata;
    }

    public void setAuxdata(String auxdata) {
        this.auxdata = auxdata;
    }

    public List<DeviceInformationTBL> getDeviceInformationTBL() {
        return deviceInformationTBL;
    }

    public void setDeviceInformationTBL(List<DeviceInformationTBL> deviceInformationTBL) {
        this.deviceInformationTBL = deviceInformationTBL;
    }

    public List<UserDataTBL> getUserDataTBL() {
        return userDataTBL;
    }

    public void setUserDataTBL(List<UserDataTBL> userDataTBL) {
        this.userDataTBL = userDataTBL;
    }

    public List<PasswordHashRecordsTBL> getPasswordHashRecordsTBL() {
        return passwordHashRecordsTBL;
    }

    public void setPasswordHashRecordsTBL(List<PasswordHashRecordsTBL> passwordHashRecordsTBL) {
        this.passwordHashRecordsTBL = passwordHashRecordsTBL;
    }

    public List<UserGroupsTBL> getUserGroupsTBL() {
        return userGroupsTBL;
    }

    public void setUserGroupsTBL(List<UserGroupsTBL> userGroupsTBL) {
        this.userGroupsTBL = userGroupsTBL;
    }

    public List<UserRolesTBL> getUserRolesTBL() {
        return userRolesTBL;
    }

    public void setUserRolesTBL(List<UserRolesTBL> userRolesTBL) {
        this.userRolesTBL = userRolesTBL;
    }

    public List<FileTableOwnerTBL> getFileTableOwnerTBL() {
        return fileTableOwnerTBL;
    }

    public void setFileTableOwnerTBL(List<FileTableOwnerTBL> fileTableOwnerTBL) {
        this.fileTableOwnerTBL = fileTableOwnerTBL;
    }

    public List<FileDirectoryTBL> getFileDirectoryTBL() {
        return fileDirectoryTBL;
    }

    public void setFileDirectoryTBL(List<FileDirectoryTBL> fileDirectoryTBL) {
        this.fileDirectoryTBL = fileDirectoryTBL;
    }

    public List<UserCookiesTBL> getUserCookiesTBL() {
        return userCookiesTBL;
    }

    public void setUserCookiesTBL(List<UserCookiesTBL> userCookiesTBL) {
        this.userCookiesTBL = userCookiesTBL;
    }

    public List<UserRelationshipTBL> getSenderIDuserRelationshipTBLS() {
        return senderIDuserRelationshipTBLS;
    }

    public void setSenderIDuserRelationshipTBLS(List<UserRelationshipTBL> senderIDuserRelationshipTBLS) {
        this.senderIDuserRelationshipTBLS = senderIDuserRelationshipTBLS;
    }

    public List<UserRequestsTBL> getSenderIDUserRequests() {
        return senderIDUserRequests;
    }

    public void setSenderIDUserRequests(List<UserRequestsTBL> senderIDUserRequests) {
        this.senderIDUserRequests = senderIDUserRequests;
    }

    public List<UserRequestsTBL> getReceiverIDUserRequests() {
        return receiverIDUserRequests;
    }

    public void setReceiverIDUserRequests(List<UserRequestsTBL> receiverIDUserRequests) {
        this.receiverIDUserRequests = receiverIDUserRequests;
    }

    public List<UserBlockListTBL> getUseridUserBlockListTBL() {
        return useridUserBlockListTBL;
    }

    public void setUseridUserBlockListTBL(List<UserBlockListTBL> useridUserBlockListTBL) {
        this.useridUserBlockListTBL = useridUserBlockListTBL;
    }

    public List<UserBlockListTBL> getBlockedidUserBlockListTBL() {
        return blockedidUserBlockListTBL;
    }

    public void setBlockedidUserBlockListTBL(List<UserBlockListTBL> blockedidUserBlockListTBL) {
        this.blockedidUserBlockListTBL = blockedidUserBlockListTBL;
    }
}
