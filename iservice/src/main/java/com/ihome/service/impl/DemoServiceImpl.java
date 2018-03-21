package com.ihome.service.impl;

import com.ihome.data.dao.IDemoDao;
import com.ihome.data.entity.Demo;
import com.ihome.service.AbstractBaseService;
import com.ihome.service.IDemoService;
import org.springframework.stereotype.Service;

/**
 * create time: 2018/3/4
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
@Service("DemoServiceImpl")
public class DemoServiceImpl extends AbstractBaseService<IDemoDao, Demo> implements IDemoService {

}
