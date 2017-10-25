package cn.wj.demo1;

/**
 * Created by WJ on 2017/10/26
 */
public interface AccountDao {
    // 扣钱
    void outMoney(String out , double money);
    // 加钱
    void inMoney(String in , double money);
}
