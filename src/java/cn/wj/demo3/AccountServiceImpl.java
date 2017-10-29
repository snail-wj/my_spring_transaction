package cn.wj.demo3;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by WJ on 2017/10/26
 * Transactional类上添加了注解,类中的所有的方法都有了事务，隔离级别，传播行为全部都是默认值
 */

@Transactional
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    /**
     * 转账的方法
     *
     * @param out
     * @param in
     * @param money
     */
    public void pay(String out, String in, double money) {
        //先扣钱
        accountDao.outMoney(out, money);
        //模拟异常
        int a = 10 / 0;
        //后加钱
        accountDao.inMoney(in, money);
    }
}
