package com.jamii.webapi.jamiidb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "user_data" , schema =  "jamiidb")
public class UserDataTBL {
    public UserDataTBL( ) { }

    public static final String      TABLE_NAME      = "user_data";

    public static final String      ID              = "ID";
    public static final String      USER_LOGIN_ID   = "USER_LOGIN_ID";
    public static final String      FIRST_NAME      = "FIRST_NAME";
    public static final String      LAST_NAME       = "LAST_NAME";
    public static final String      MIDDLE_NAME     = "MIDDLE_NAME";
    public static final String      ADDRESS_1       = "ADDRESS_1";
    public static final String      ADDRESS_2       = "ADDRESS_2";
    public static final String      CITY            = "CITY";
    public static final String      STATE           = "STATE";
    public static final String      PROVINCE        = "PROVINCE";
    public static final String      COUNTRY         = "COUNTRY";
    public static final String      ZIPCODE         = "ZIPCODE";
    public static final String      LAST_UPDATED    = "LAST_UPDATED";
    public static final String      CURRENT         = "CURRENT";
    public static final String      AUX_DATA        = "AUX_DATA";


    /**
     * Below are the Current Statuses. "Current" reffers to the most recent record of a particular user
     */
    public static final Boolean     CURRENT_STATUS_OFF = false;
    public static final Boolean     CURRENT_STATUS_ON  = true ;

    @Id
    @Column( name = ID)
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Integer id;

    @Column( name = FIRST_NAME , nullable = true, length = 100 )
    private String firstname;

    @Column( name = LAST_NAME, nullable = true, length = 100 )
    private String lastname;

    @Column( name = MIDDLE_NAME, nullable = true, length = 100 )
    private String middlename;

    @Column( name = ADDRESS_1, nullable = true, length = 200 )
    private String address1;

    @Column( name = ADDRESS_2, nullable = true, length = 200 )
    private String address2;

    @Column( name = CITY, nullable = true, length = 50)
    private String city;

    @Column( name = STATE, nullable = true, length = 50 )
    private String state;

    @Column( name = PROVINCE, nullable = true, length = 50 )
    private String province;

    @Column( name = COUNTRY, nullable = true, length = 20 )
    private String country;

    @Column( name = ZIPCODE, nullable = true, length = 30 )
    private String zipcode;

    @Column( name = LAST_UPDATED, nullable = false )
    private LocalDateTime lastupdated;

    @Column( name = CURRENT, nullable = false, length = 30 )
    private Boolean current;

    @Column( name = AUX_DATA, nullable = true, length = 5000 )
    private String auxdata;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn( name = USER_LOGIN_ID, nullable = false )
    private UserLoginTBL FK_USER_LOGIN_DATA;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public LocalDateTime getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(LocalDateTime lastupdated) {
        this.lastupdated = lastupdated;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public String getAuxdata() {
        return auxdata;
    }

    public void setAuxdata(String auxdata) {
        this.auxdata = auxdata;
    }
}
