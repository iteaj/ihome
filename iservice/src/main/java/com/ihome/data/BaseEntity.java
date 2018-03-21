package com.ihome.data;

import com.iteaj.core.orm.Entity;

import java.util.Date;

public abstract class BaseEntity implements Entity{

    private long id; //实体对应表的主键
    private Date createTime; //实体记录的创建时间

    /**
     * 实体对应表的表主键
     * @return
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * 创建时间
     * @return
     */
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
