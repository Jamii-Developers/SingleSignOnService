package com.jamii.social.model;

import com.jamii.users.model.UserLoginTBL;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

/**
 * JPA Entity representing user groups in the database.
 * 
 * <p>This class maps to the {@code user_groups} table and stores group information
 * for organizing users. Groups can have multiple roles assigned to them.</p>
 * 
 * <p>Key fields:</p>
 * <ul>
 *     <li>{@code id} - Primary key</li>
 *     <li>{@code userloginid} - User who belongs to this group</li>
 *     <li>{@code userRolesTBL} - List of roles assigned to this group</li>
 * </ul>
 */
@Entity
@Table(name = "user_groups")
public class UserGroupsTBL
{

    /**
     * Database table name constant.
     */
    public static final String TABLE_NAME = "user_groups";
    
    /**
     * Column name constant for ID.
     */
    public static final String ID = "ID";
    
    /**
     * Column name constant for user login ID.
     */
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    
    /**
     * Column name constant for auxiliary data.
     */
    public static final String AUX_DATA = "AUX_DATA";

    /**
     * Primary key identifier.
     */
    @Id @Column(name = ID) @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    
    /**
     * One-to-many relationship to user roles assigned to this group.
     */
    @OneToMany(mappedBy = "usergroupsid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) private List<UserRolesTBL> userRolesTBL;
    
    /**
     * Foreign key reference to the user who belongs to this group.
     */
    @ManyToOne @JoinColumn(name = USER_LOGIN_ID, nullable = false) private UserLoginTBL userloginid;

    /**
     * Default constructor.
     */
    public UserGroupsTBL() {}

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
     * Gets the list of roles assigned to this group.
     * @return the list of user roles
     */
    public List<UserRolesTBL> getUserRolesTBL()
    {
        return userRolesTBL;
    }

    /**
     * Sets the list of roles assigned to this group.
     * @param userRolesTBL the list of user roles to set
     */
    public void setUserRolesTBL(List<UserRolesTBL> userRolesTBL)
    {
        this.userRolesTBL = userRolesTBL;
    }

    /**
     * Gets the user who belongs to this group.
     * @return the user login entity
     */
    public UserLoginTBL getUserloginid()
    {
        return userloginid;
    }

    /**
     * Sets the user who belongs to this group.
     * @param userloginid the user login entity to set
     */
    public void setUserloginid(UserLoginTBL userloginid)
    {
        this.userloginid = userloginid;
    }
}
