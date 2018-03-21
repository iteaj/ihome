package com.ihome.service;

import com.iteaj.core.orm.Entity;
import com.iteaj.core.orm.IBaseDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;

/**
 * create time: 2018/3/4
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public abstract class AbstractBaseService<D extends IBaseDao, E extends Entity>
        implements IBaseService<D, E> {

    @Autowired
    private D entityDao;

    @Override
    public D entityDao() {
        return entityDao;
    }

    @Override
    public void add(E entity) {
        entityDao().add(entity);
    }

    @Override
    public void delete(Serializable id) {
        entityDao().delete(id);
    }

    @Override
    public void update(E entity) {
        entityDao().update(entity);
    }

    @Override
    public E get(Serializable id) {
        return (E) entityDao().get(id);
    }

    @Override
    public Collection<E> find(E condition) {
        return entityDao().find(condition);
    }
}
