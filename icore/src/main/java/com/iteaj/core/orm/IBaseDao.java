package com.iteaj.core.orm;

import java.io.Serializable;
import java.util.Collection;

/**
 * create time: 2017/10/28
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public interface IBaseDao<E extends Entity>{

    /**
     * 增加一条实体记录到表
     * @param entity
     */
    void add(E entity);

    /**
     * 根据id删除一条记录
     * @param id
     * @return
     */
    void delete(Serializable id);

    /**
     * 更新一条实体记录到表
     * @param entity
     * @return
     */
    void update(E entity);

    /**
     * 根据id获取一条实体对象
     * @param id
     * @return
     */
    E get(Serializable id);

    /**
     * 查询记录列表通过指定的条件
     * @param condition
     * @return
     */
    Collection<E> find(E condition);
}
