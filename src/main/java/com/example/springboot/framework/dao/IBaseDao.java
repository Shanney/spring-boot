package com.example.springboot.framework.dao;

import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface IBaseDao {
    public void add(Object o);

    public void updateObject(Object o);

    public void saveOrUpdateObject(Object obj);

    public void deleteObject(Object o);

    public void deleteObject(Class clazz, Serializable id);

    public void deleteAllObject(Collection entities);

    public void saveOrUpdateAllObjects(Collection objs);

    public void evict(Object obj);

    public Object getObject(Class clazz, Serializable id);

    public List getObjects(Class clazz);

    public List getObjects(final String mysql);

    public List getObjects(final String queryString, Object[] values);

    /**
     * 分页查询方法，使用Query对象，不能直接被Service调用
     *
     * @param query
     *            Query接口实例对象
     * @param startRowNum
     *            起始行数
     * @param pageSize
     *            单页行数
     * @return 对象集合
     */
    public List getObjects(Query query, final int startRowNum, final int pageSize);

    /**
     * 查询数据总量，使用Query实例对象（返回Long转为int）
     *
     * @param query
     * @return 总数
     */
    public int getObjectsCount(Query query);

    /**
     * 分页查询方法，使用HQL，不能直接被Service调用
     *
     * @param hql
     *            Hibernate类SQL查询语句
     * @param startRowNum
     *            起始行数
     * @param pageSize
     *            单页行数
     * @return 对象集合
     */
    public List getObjectsByHQL(final String hql, final int startRowNum, final int pageSize);

    /**
     * 查询数据总量，使用HQL语句（返回Long转为int）
     *
     * @param hql
     * @return 总数
     */
    public int getObjectsCountByHQL(final String hql);

    /**
     * 分页查询方法，使用原生SQL语句，不能直接被Service调用
     *
     * @param sql
     *            原生SQL语句
     * @param startRowNum
     *            起始行数
     * @param pageSize
     *            单页行数
     * @return 对象集合
     */
    public List getObjectsBySQL(final String sql, final int startRowNum, final int pageSize);

    /**
     * 查询数据总量，使用原生SQL语句（返回Long转为int）
     *
     * @param sql
     * @return 总数
     */
    public int getObjectsCountBySQL(final String sql);

    public List getObjectsByPageNoAndSize(Query query, int pageNo, int pageSize);
}
