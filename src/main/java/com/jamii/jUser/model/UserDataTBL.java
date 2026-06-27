package com.jamii.jUser.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Entity representing user profile data in the database.
 * 
 * <p>This class maps to the {@code user_data} table and stores user profile information
 * including name, address, and contact details. First and last name getters return "N/A"
 * if the value is null or empty.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userloginid} - User this profile belongs to</li>
 *     <li>{@code firstname} - User's first name</li>
 *     <li>{@code lastname} - User's last name</li>
 *     <li>{@code middlename} - User's middle name</li>
 *     <li>Address fields (address1, address2, city, state, province, country, zipcode)</li>
 * </ul>
 */
@Entity
@Table(name = "user_data")
public class UserDataTBL
{
    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "user_data";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for first name.
     */
    public static final String FIRST_NAME = "FIRST_NAME";
    
    /**
     * Column name constant for last name.
     */
    public static final String LAST_NAME = "LAST_NAME";
    
    /**
     * Column name constant for middle name.
     */
    public static final String MIDDLE_NAME = "MIDDLE_NAME";
    
    /**
     * Column name constant for address line 1.
     */
    public static final String ADDRESS_1 = "ADDRESS_1";
    
    /**
     * Column name constant for address line 2.
     */
    public static final String ADDRESS_2 = "ADDRESS_2";
    
    /**
     * Column name constant for city.
     */
    public static final String CITY = "CITY";
    
    /**
     * Column name constant for state.
     */
    public static final String STATE = "STATE";
    
    /**
     * Column name constant for province.
     */
    public static final String PROVINCE = "PROVINCE";
    
    /**
     * Column name constant for country.
     */
    public static final String COUNTRY = "COUNTRY";
    
    /**
     * Column name constant for zip code.
     */
    public static final String ZIPCODE = "ZIPCODE";
    
    /**
     * Column name constant for last updated timestamp.
     */
    public static final String LAST_UPDATED = "LAST_UPDATED";
    
    /**
     * Column name constant for auxiliary data.
     */
    public static final String AUX_DATA = "AUX_DATA";

    /**
     * Primary key identifier.
     */
    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * User's first name.
     */
    @Column(name = FIRST_NAME, nullable = true, length = 100) private String firstname;
    
    /**
     * User's last name.
     */
    @Column(name = LAST_NAME, nullable = true, length = 100) private String lastname;
    
    /**
     * User's middle name.
     */
    @Column(name = MIDDLE_NAME, nullable = true, length = 100) private String middlename;
    
    /**
     * Address line 1.
     */
    @Column(name = ADDRESS_1, nullable = true, length = 200) private String address1;
    
    /**
     * Address line 2.
     */
    @Column(name = ADDRESS_2, nullable = true, length = 200) private String address2;
    
    /**
     * City.
     */
    @Column(name = CITY, nullable = true, length = 50) private String city;
    
    /**
     * State.
     */
    @Column(name = STATE, nullable = true, length = 50) private String state;
    
    /**
     * Province.
     */
    @Column(name = PROVINCE, nullable = true, length = 50) private String province;
    
    /**
     * Country.
     */
    @Column(name = COUNTRY, nullable = true, length = 20) private String country;
    
    /**
     * Zip code.
     */
    @Column(name = ZIPCODE, nullable = true, length = 30) private String zipcode;
    
    /**
     * Timestamp when the profile was last updated.
     */
    @Column(name = LAST_UPDATED, nullable = false) private LocalDateTime lastupdated;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * One-to-one relationship to the user login record.
     */
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;
    
    /**
     * One-to-many relationship to user data history records.
     */
    @OneToMany(mappedBy = "userdataid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @OnDelete(action = OnDeleteAction.CASCADE) private List<UserDataHistoryTBL> userdatahistorylist;

    /**
     * Default constructor.
     */
    public UserDataTBL() {}

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
     * Gets the first name.
     * @return the first name, or "N/A" if null or empty
     */
    public String getFirstname()
    {
        if (firstname == null || firstname.isEmpty()) {
            return "N/A";
        }
        else {
            return firstname;
        }
    }

    /**
     * Sets the first name.
     * @param firstname the first name to set
     */
    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    /**
     * Gets the last name.
     * @return the last name, or "N/A" if null or empty
     */
    public String getLastname()
    {
        if (lastname == null || lastname.isEmpty()) {
            return "N/A";
        }
        else {
            return lastname;
        }
    }

    /**
     * Sets the last name.
     * @param lastname the last name to set
     */
    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    /**
     * Gets the middle name.
     * @return the middle name
     */
    public String getMiddlename()
    {
        return middlename;
    }

    /**
     * Sets the middle name.
     * @param middlename the middle name to set
     */
    public void setMiddlename(String middlename)
    {
        this.middlename = middlename;
    }

    /**
     * Gets address line 1.
     * @return address line 1
     */
    public String getAddress1()
    {
        return address1;
    }

    /**
     * Sets address line 1.
     * @param address1 address line 1 to set
     */
    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    /**
     * Gets address line 2.
     * @return address line 2
     */
    public String getAddress2()
    {
        return address2;
    }

    /**
     * Sets address line 2.
     * @param address2 address line 2 to set
     */
    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    /**
     * Gets the city.
     * @return the city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * Sets the city.
     * @param city the city to set
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * Gets the state.
     * @return the state
     */
    public String getState()
    {
        return state;
    }

    /**
     * Sets the state.
     * @param state the state to set
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * Gets the province.
     * @return the province
     */
    public String getProvince()
    {
        return province;
    }

    /**
     * Sets the province.
     * @param province the province to set
     */
    public void setProvince(String province)
    {
        this.province = province;
    }

    /**
     * Gets the country.
     * @return the country
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * Sets the country.
     * @param country the country to set
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * Gets the zip code.
     * @return the zip code
     */
    public String getZipcode()
    {
        return zipcode;
    }

    /**
     * Sets the zip code.
     * @param zipcode the zip code to set
     */
    public void setZipcode(String zipcode)
    {
        this.zipcode = zipcode;
    }

    /**
     * Gets the last updated timestamp.
     * @return the last updated timestamp
     */
    public LocalDateTime getLastupdated()
    {
        return lastupdated;
    }

    /**
     * Sets the last updated timestamp.
     * @param lastupdated the last updated timestamp to set
     */
    public void setLastupdated(LocalDateTime lastupdated)
    {
        this.lastupdated = lastupdated;
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
     * Gets the user this profile belongs to.
     * @return the user login entity
     */
    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user this profile belongs to.
     * @param userloginid the user login entity to set
     */
    public void setUserloginid(UserLoginTBL userloginid)
    {
        this.userloginid = userloginid;
    }

    /**
     * Gets the list of user data history records.
     * @return the list of user data history
     */
    public List<UserDataHistoryTBL> getUserdatahistorylist()
    {
        return userdatahistorylist;
    }

    /**
     * Sets the list of user data history records.
     * @param userdatahistorylist the list of user data history to set
     */
    public void setUserdatahistorylist(List<UserDataHistoryTBL> userdatahistorylist)
    {
        this.userdatahistorylist = userdatahistorylist;
    }
}
