package com.iteaj.core.orm.mybatis;

import java.io.Serializable;

/**
 * mybatis会为实现此接口的实体类进行自动类型别名的注册
 * @see org.mybatis.spring.SqlSessionFactoryBean#typeAliasesSuperType
 * @see org.mybatis.spring.SqlSessionFactoryBean#typeAliasesPackage
 *
 * @see Serializable 实体类可序列化
 */
public interface EntityAliasSuperType extends Serializable{

}
