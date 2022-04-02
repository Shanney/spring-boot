package com.example.springboot.portal.dao.impl;

import com.example.springboot.portal.dao.IResourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class ResourceDaoImpl implements IResourceDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Map<String, String> getAllResourceRoles() {
        return null;
    }

    /**
     * 返回
     * @param roleIds
     * @return List<Resource>
     */
    @Override
    public List getResourcesByRoleId(Collection<String> roleIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select r from ");
        sql.append(" Resource r, ResourceRoleMapping m ");
        sql.append(" where r.resourceNo = m.resourceNo ");
        sql.append(" and m.roleId in (:roleIds) ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("roleIds", roleIds);
        return query.getResultList();
    }
}
