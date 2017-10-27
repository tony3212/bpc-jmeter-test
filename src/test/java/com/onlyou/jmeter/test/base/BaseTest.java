package com.onlyou.jmeter.test.base;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * SERVICE端 单元测试基类
 * Created by guoqiang on 2015/10/26.
 * <p/>
 * 必须注意单元测试默认是进行事务回滚,以保持数据环境不受影响
 * 需要作保存动作的单元测试可参考DictGroupServiceTest
 * <p/>
 * 如果想让单元测试方法物理的保存到数据库可以在方法前加上：@AfterTransaction
 * 但这样会影响数据环境导致单元测试无法重复执行
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:dubbo-client.xml"})
public abstract class BaseTest extends AbstractJUnit4SpringContextTests {
    protected Logger logger = LoggerFactory.getLogger(getClass());
}
