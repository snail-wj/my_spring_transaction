package cn.wj.demo2;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Created by WJ on 2017/10/26
 */
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {



    /**
     * 扣钱
     * @param out
     * @param money
     */
    public void outMoney(String out, double money) {
        this.getJdbcTemplate().update("update t_account set money = money - ? where name = ?",money,out);
    }

    /**
     * 存钱
     * @param in
     * @param money
     */
    public void inMoney(String in, double money) {
        this.getJdbcTemplate().update("update t_account set money = money + ? where name = ?",money,in);
    }
}
