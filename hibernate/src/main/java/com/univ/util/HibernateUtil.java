package com.univ.util;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static Session session;
	static {
		session = new Configuration().configure().buildSessionFactory().openSession();
	}
	public static Session getSession(){
		return session;
	}
}
