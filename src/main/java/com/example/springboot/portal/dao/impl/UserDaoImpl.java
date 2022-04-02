package com.example.springboot.portal.dao.impl;

import com.example.springboot.portal.Constants;
import com.example.springboot.portal.dao.IUserDao;
import com.example.springboot.portal.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class UserDaoImpl implements IUserDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List getUserRoleIds(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select roleId from UserRoleMapping where userId=:userId ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public User getUserByPortalName(String username) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from User ").append(" where portalName=:username and useState=:useState ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("username", username);
        query.setParameter("useState", Constants.COMMON_FLAG_INUSE_YES);
        List<User> result = query.getResultList();
        if(result.size()>0){
            return result.get(0);
        }else{
            return null;
        }
    }

}
