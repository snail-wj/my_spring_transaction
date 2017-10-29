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

2. 步骤二：引入配置文件
		* 引入配置文件
			* 引入log4j.properties
			
			* 引入applicationContext.xml
				<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
			    	<property name="driverClass" value="com.mysql.jdbc.Driver"/>
			    	<property name="jdbcUrl" value="jdbc:mysql:///spring_day03"/>
			    	<property name="user" value="root"/>
			    	<property name="password" value="root"/>
			    </bean>
			    
3. 步骤三：创建对应的包结构和类
		* com.itheima.demo1
			* AccountService
			* AccountServlceImpl
			* AccountDao
			* AccountDaoImpl
	
4. 步骤四:引入Spring的配置文件,将类配置到Spring中
    <bean id="accountService" class="com.itheima.demo1.AccountServiceImpl">
    </bean>
    
    <bean id="accountDao" class="com.itheima.demo1.AccountDaoImpl">
    </bean>
    
	
5. 步骤五：在业务层注入DAO ,在DAO中注入JDBC模板（强调：简化开发，以后DAO可以继承JdbcDaoSupport类）
    <bean id="accountService" class="com.itheima.demo1.AccountServiceImpl">
        <property name="accountDao" ref="accountDao"/>
    </bean>
    
    <bean id="accountDao" class="com.itheima.demo1.AccountDaoImpl">
        <property name="dataSource" ref="dataSource"/>
    </bean>
   
6. 步骤六：编写DAO和Service中的方法
		public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {
			public void outMoney(String out, double money) {
				this.getJdbcTemplate().update("update t_account set money = money = ? where name = ?", money,out);
			}
			public void inMoney(String in, double money) {
				this.getJdbcTemplate().update("update t_account set money = money + ? where name = ?", money,in);
			}
		}
7. 步骤七：编写测试程序.
		@RunWith(SpringJUnit4ClassRunner.class)
		@ContextConfiguration("classpath:applicationContext.xml")
		public class Demo1 {
			
			@Resource(name="accountService")
			private AccountService accountService;
			
			@Test
			public void run1(){
				accountService.pay("冠希", "美美", 1000);
			}
		}

**技术分析之Spring框架的事务管理的分类**
    1. Spring的事务管理的分类
		1. Spring的编程式事务管理（不推荐使用）
			* 通过手动编写代码的方式完成事务的管理（不推荐）
		
		2. Spring的声明式事务管理（底层采用AOP的技术）
			* 通过一段配置的方式完成事务的管理（重点掌握注解的方式）
			
**技术分析之Spring框架的事务管理之编程式的事务管理（了解）**

    1. 说明：Spring为了简化事务管理的代码:提供了模板类 TransactionTemplate，所以手动编程的方式来管理事务，只需要使用该模板类即可！！
	
	2. 手动编程方式的具体步骤如下：
		1. 步骤一:配置一个事务管理器，Spring使用PlatformTransactionManager接口来管理事务，所以咱们需要使用到他的实现类！！
			<!-- 配置事务管理器 -->
			<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
				<property name="dataSource" ref="dataSource"/>
			</bean>
		
		2. 步骤二:配置事务管理的模板
			<!-- 配置事务管理的模板 -->
			<bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
				<property name="transactionManager" ref="transactionManager"/>
			</bean>
		
		3. 步骤三:在需要进行事务管理的类中,注入事务管理的模板.
			<bean id="accountService" class="com.itheima.demo1.AccountServiceImpl">
				<property name="accountDao" ref="accountDao"/>
				<property name="transactionTemplate" ref="transactionTemplate"/>
			</bean>
		
		4. 步骤四:在业务层使用模板管理事务:
			// 注入事务模板对象
			private TransactionTemplate transactionTemplate;
			public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
				this.transactionTemplate = transactionTemplate;
			}
			
			public void pay(final String out, final String in, final double money) {
				transactionTemplate.execute(new TransactionCallbackWithoutResult() {
					
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						// 扣钱
						accountDao.outMoney(out, money);
						int a = 10/0;
						// 加钱
						accountDao.inMoney(in, money);
					}
				});
			}


**Spring框架的事务管理之声明式事务管理，即通过配置文件来完成事务管理(AOP思想)**

    1. 声明式事务管理又分成两种方式
        * 基于AspectJ的XML方式（重点掌握）
        * 基于AspectJ的注解方式（重点掌握）
        
**Spring框架的事务管理之基于AspectJ的XML方式（重点掌握）**

	1. 步骤一:恢复转账开发环境
	
    2. 步骤二:引入AOP的开发包
    
    3. 步骤三:配置事务管理器
        <!-- 配置事务管理器 -->
        <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
            <property name="dataSource" ref="dataSource"/>
        </bean>
        
    4. 步骤四:配置事务增强
        <!-- 配置事务增强 -->
        <tx:advice id="txAdvice" transaction-manager="transactionManager">
            <tx:attributes>
                <!--
                    name		：绑定事务的方法名，可以使用通配符，可以配置多个。
                    propagation	：传播行为
                    isolation	：隔离级别
                    read-only	：是否只读
                    timeout		：超时信息
                    rollback-for：发生哪些异常回滚.
                    no-rollback-for：发生哪些异常不回滚.
                 -->
                <!-- 哪些方法加事务 -->
                <tx:method name="pay" propagation="REQUIRED"/>
            </tx:attributes>
        </tx:advice>
        
    5. 步骤五:配置AOP的切面
        <!-- 配置AOP切面产生代理 -->
        <aop:config>
            <aop:advisor advice-ref="myAdvice" pointcut="execution(* com.itheima.demo2.AccountServiceImpl.pay(..))"/>
        </aop:config>
        
        * 注意：如果是自己编写的切面，使用<aop:aspect>标签，如果是系统制作的，使用<aop:advisor>标签。
        
    6. 步骤六:编写测试类
        @RunWith(SpringJUnit4ClassRunner.class)
        @ContextConfiguration("classpath:applicationContext2.xml")
        public class Demo2 {
            
            @Resource(name="accountService")
            private AccountService accountService;
            
            @Test
            public void run1(){
                accountService.pay("冠希", "美美", 1000);
            }
        }

**Spring框架的事务管理之基于AspectJ的注解方式（重点掌握，最简单的方式）**

1. 步骤一:恢复转账的开发环境

2. 步骤二:配置事务管理器
    <!-- 配置事务管理器  -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    
3. 步骤三:开启注解事务
    <!-- 开启注解事务 -->
    <tx:annotation-driven transaction-manager="transactionManager"/>
    
4. 步骤四:在业务层上添加一个注解:@Transactional

5. 编写测试类
    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration("classpath:applicationContext3.xml")
    public class Demo3 {
        
        @Resource(name="accountService")
        private AccountService accountService;
        
        @Test
        public void run1(){
            accountService.pay("冠希", "美美", 1000);
        }
    }
    数据库的回滚，则是说如果在add,update,delete的操作数据库的时候，那么我们如果中间出现了错误，就可以进行回滚
    
    一般我们做事务的时候，都是采用声明式的事务，但是具体来说是注解还是xml的配置，还要看他们如何配置
    

总结：
    1.JDBC的模板技术，主要就是说连接数据库一些操作
        例如如果我们要使用开源的连接池，例如c3po或是dbcp，则这个则很简单，我们就是和jdbc一样使用，我们直接就可以导入jar包以及在xml添加注解即可