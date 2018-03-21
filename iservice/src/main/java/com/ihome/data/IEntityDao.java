package com.ihome.data;

import com.iteaj.core.orm.Entity;
import com.iteaj.core.orm.IBaseDao;
import com.iteaj.core.orm.mybatis.MapperDaoScanner;

/**
 * create time: 2018/3/4
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public interface IEntityDao<T extends Entity> extends IBaseDao<T>, MapperDaoScanner {

}
