package cn.wj.demo2;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Created by WJ on 2017/10/26
 */
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
//        int a = 10 / 0;
        //后加钱
        accountDao.inMoney(in, money);
    }
}
