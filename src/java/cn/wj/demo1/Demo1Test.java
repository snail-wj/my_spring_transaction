package cn.wj.demo1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by WJ on 2017/10/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Demo1Test {

    @Resource
    private AccountService accountService;

    @Test
    public void run1(){
        //调用支付的方法
        accountService.pay("jayChen","燕姿大人",100.1);
    }
}
