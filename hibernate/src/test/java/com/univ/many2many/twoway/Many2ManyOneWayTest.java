package com.univ.many2many.twoway;

import com.univ.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.Set;

/**
 * Univ
 * 2017/1/12 19:13
 */
public class Many2ManyOneWayTest {
    private Session session = HibernateUtil.getSession();
    private Transaction transaction = null;

    /**
     * 保存测试
     *
     * c1与i1,i2,i3关联；
     * c2与i3,i4关联；
     *
     * i1与c1关联；
     * i2与c1关联；
     * i3与c1，c2关联；
     * i4与c2关联；
     */
    @Test
    public void save(){
        Item i1 = new Item("item1");
        Item i2 = new Item("item2");
        Item i3 = new Item("item3");
        Item i4 = new Item("item4");
        Category c1 = new Category("category1");
        Category c2 = new Category("category2");

        //i1关联i1,i2,i3
        c1.getItemSet().add(i1);
        c1.getItemSet().add(i2);
        c1.getItemSet().add(i3);

        //i2关联i3,i4
        c2.getItemSet().add(i3);
        c2.getItemSet().add(i4);

        /**
         * 注意，不要执行下面的语句，结果是会重复插入导致中间表主键重复的异常
         */
       /* //i1关联c1
        i1.getCategorySet().add(c1);

        //i2关联c1
        i2.getCategorySet().add(c1);

        //i3关联c1,c2
        i3.getCategorySet().add(c1);
        i3.getCategorySet().add(c2);

        //i4关联c2
        i4.getCategorySet().add(c2);*/

        transaction = session.beginTransaction();
        session.save(c1);
        session.save(c2);

        transaction.commit();
        session.close();
    }

    @Test
    public void get(){
        //找出与c1关联的item
        Category c1 = (Category) session.load(Category.class, (long) 1);
        Set<Item> itemSet = c1.getItemSet();
        System.out.print("与c1关联的item有： ");
        for (Item i : itemSet) {
            System.out.print(i.getName() + " , ");
        }

        //找出与c2关联的item
        Category c2 = (Category) session.load(Category.class, (long) 2);
        Set<Item> itemSet2 = c2.getItemSet();
        System.out.print("与c2关联的item有： ");
        for (Item i : itemSet2) {
            System.out.print(i.getName() + " , ");
        }

        //找出与id为1的item有关联的category
        Item i = (Item) session.load(Item.class, (long) 1);
        Set<Category> categorySet = i.getCategorySet();
        System.out.print("与id为1的item关联的category有： ");
        for (Category c : categorySet) {
            System.out.println(c.getName());
        }

        //找出与id为2的item有关联的category
        Item i2 = (Item) session.load(Item.class, (long) 2);
        Set<Category> categorySet2 = i2.getCategorySet();
        System.out.print("与id为1的item关联的category有： ");
        for (Category c : categorySet2) {
            System.out.println(c.getName());
        }

    }
}
