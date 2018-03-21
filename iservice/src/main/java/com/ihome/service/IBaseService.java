package com.ihome.service;

import com.iteaj.core.orm.Entity;
import com.iteaj.core.orm.IBaseDao;

/**
 * create time: 2018/3/4
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public interface IBaseService<D extends IBaseDao, E extends Entity> extends IBaseDao<E> {

    D entityDao();
}
