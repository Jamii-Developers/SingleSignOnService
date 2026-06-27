package com.jamii.jSocial.model;

import com.jamii.jUser.model.UserLoginTBL;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * JPA Entity representing user roles in the database.
 * 
 * <p>This class maps to the {@code user_roles} table and stores role assignments
 * for jUser within groups. Roles define permissions and access levels.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userloginid} - User assigned to this role</li>
 *     <li>{@code usergroupsid} - Group this role belongs to</li>
 * </ul>
 */
@Entity
@Table(name = "user_roles")
public class UserRolesTBL
{
    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "user_roles";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for user groups ID.
     */
    public static final String USER_GROUPS_ID = "USER_GROUPS_ID";
    
    /**
     * Column name constant for auxiliary data.
     */
    public static final String AUX_DATA = "AUX_DATA";

    /**
     * Primary key identifier.
     */
    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * Auxiliary data stored as text.
     */
    @Column(name = AUX_DATA, columnDefinition = "text") private String auxdata;

    /**
     * Foreign key reference to the user assigned to this role.
     */
    @ManyToOne @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;
    
    /**
     * Foreign key reference to the group this role belongs to.
     */
    @ManyToOne @JoinColumn(name = USER_GROUPS_ID, nullable = false) private UserGroupsTBL usergroupsid;

    /**
     * Default constructor.
     */
    public UserRolesTBL()
    {
    }

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
     * Gets the user assigned to this role.
     * @return the user login entity
     */
    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user assigned to this role.
     * @param userloginid the user login entity to set
     */
    public void setUserloginid(UserLoginTBL userloginid)
    {
        this.userloginid = userloginid;
    }

    /**
     * Gets the group this role belongs to.
     * @return the user group entity
     */
    public UserGroupsTBL getUsergroupsid()
    {
        return usergroupsid;
    }

    /**
     * Sets the group this role belongs to.
     * @param usergroupsid the user group entity to set
     */
    public void setUsergroupsid(UserGroupsTBL usergroupsid)
    {
        this.usergroupsid = usergroupsid;
    }
}
