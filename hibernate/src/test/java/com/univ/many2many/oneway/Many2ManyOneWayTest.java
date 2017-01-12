package com.univ.many2many.oneway;

import com.univ.many2many.oneway.CategoryOne;
import com.univ.many2many.oneway.ItemOne;
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
     */
    @Test
    public void save(){
        ItemOne i1 = new ItemOne("item1");
        ItemOne i2 = new ItemOne("item2");
        ItemOne i3 = new ItemOne("item3");
        ItemOne i4 = new ItemOne("item4");

        CategoryOne c1 = new CategoryOne("category1");
        CategoryOne c2 = new CategoryOne("category2");

        c1.getItemSet().add(i1);
        c1.getItemSet().add(i2);
        c1.getItemSet().add(i3);

        c2.getItemSet().add(i3);
        c2.getItemSet().add(i4);

        transaction = session.beginTransaction();
        session.save(c1);//已在单向的多方设置了级联保存
        session.save(c2);

        transaction.commit();
        session.close();
    }

    @Test
    public void getItemFromCategory(){
        //找出与c1关联的item
        CategoryOne c1 = (CategoryOne) session.load(CategoryOne.class, (long) 1);
        Set<ItemOne> itemSet = c1.getItemSet();
        for (ItemOne i :
                itemSet) {
            System.out.println(i.getName());
        }

        //找出与c2关联的item
        CategoryOne c2 = (CategoryOne) session.load(CategoryOne.class, (long) 2);
        Set<ItemOne> itemSet2 = c2.getItemSet();
        for (ItemOne i :
                itemSet2) {
            System.out.println(i.getName());
        }
    }
}
