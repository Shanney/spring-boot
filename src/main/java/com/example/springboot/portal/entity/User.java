package com.example.springboot.portal.entity;

import org.hibernate.annotations.Tables;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "PORTAL", name = "EMPLOYEE")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "portal_name")
    private String portalName;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "sex")
    private String sex;

    @Column(name = "office_phone")
    private String officePhone;

    @Column(name = "fax_phone")
    private String mobilePhone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "version")
    private String version;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "pinyin")
    private String pinyin;

    @Column(name = "use_state")
    private String useState;

    /**
     * 用于给前端展示的，用户的所有角色
     */
    @Transient
    private String userRoleString;

    public void intiUserRoleString(List<String> roles) {
        StringBuilder sb = new StringBuilder();
        for (String urm : roles) {
            sb.append(urm).append(";");
        }
        this.userRoleString = sb.toString().substring(0,
                sb.toString().length() - 1);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getUseState() {
        return useState;
    }

    public void setUseState(String useState) {
        this.useState = useState;
    }
}
