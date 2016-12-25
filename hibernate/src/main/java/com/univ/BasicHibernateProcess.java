package com.univ;

/**
 * Univ
 * 16/12/25 16:25
 */

import com.univ.single.Single;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * 演示Hiberante的完整的简单使用流程
 */
public class BasicHibernateProcess {

    public static void main(String[] args) {
        //1. 加载Hibernate默认的核心配置文件,理解成Configuration就是核心配置文件的表示
        Configuration config = new Configuration();
        config.configure();

        //2. 从Configuration对象获得SessionFactory
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().
                applySettings(config.getProperties()).
                build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        //旧版方法：sessionFactory = config.buildSessionFactory();//此方法已废弃

        //3. 从SessionFactory对象获得Session
        Session session = sessionFactory.openSession();

        //4. 从Session对象开启事务
        Transaction transaction = session.beginTransaction();

        //5. 用session操作数据库
        Single single = (Single) session.get(Single.class, (long)1);//立即执行select语句
        System.out.println(single.getId());

        //6. 提交事务
        transaction.commit();

        //7. 关闭Session
        session.close();

        /*
         * 8. 关闭SessionFactory。
         * 当然SessionFactory是一个重对象，在实际使用中不会用完就关闭，而是供后续的session继续使用。
         * 一个应用一个SessionFactory.
         */
        sessionFactory.close();
    }

}
