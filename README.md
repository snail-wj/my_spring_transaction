# my_spring_transaction
关于事务的操作

**技术分析之搭建事务管理转账案例的环境（强调：简化开发，以后DAO可以继承JdbcDaoSupport类）**

1. 步骤一：创建WEB工程，引入需要的jar包
		* IOC的6个包
		* AOP的4个包
		* C3P0的1个包
		* MySQL的驱动包
		* JDBC目标2个包
		* 整合JUnit测试包
