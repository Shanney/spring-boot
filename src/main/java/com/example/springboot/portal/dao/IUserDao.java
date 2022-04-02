package com.example.springboot.portal.dao;

import com.example.springboot.portal.entity.User;

import java.util.List;

public interface IUserDao {

    public List getUserRoleIds(final String userId);

    public User getUserByPortalName(final String username);
}
