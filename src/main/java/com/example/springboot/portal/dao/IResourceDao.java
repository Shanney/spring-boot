package com.example.springboot.portal.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IResourceDao {

    public Map<String, String> getAllResourceRoles();

    public List getResourcesByRoleId(final Collection<String> roleIds);
}
