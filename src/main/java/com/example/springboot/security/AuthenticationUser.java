package com.example.springboot.security;

import com.example.springboot.portal.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthenticationUser implements UserDetails {

    /**
     * 登录名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    private User user;

    /**
     * 用户ID
     */
    private String userId;

    private Set<String> roleIds;

    // private List<Resource> modules;

    // private List<Resource> allResources;

    private String allResourcesStr;

    /**
     * Indicates whether the user's account has expired. An expired account
     * cannot be authenticated.
     */
    private boolean accountNonExpired = true;

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     */
    private boolean accountNonLocked = true;

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     */
    private boolean credentialsNonExpired = true;

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot
     * be authenticated.
     */
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        final int length = roleIds.size();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>(
                length);
        for (String roleId : roleIds) {
            grantedAuthorities.add(new SimpleGrantedAuthority(roleId));
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public void setAllResourcesStr(String allResourcesStr) {
        this.allResourcesStr = allResourcesStr;
    }
}
