package com.shoppingcart.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.entity.Product;
import com.shoppingcart.model.PaginationResult;
import com.shoppingcart.model.ProductInfo;


@Repository
@Transactional
public class ProductDAOImpl implements ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public PaginationResult<ProductInfo> getAllProductInfos(int page, int maxResult, int maxNavigationPage,
			String likeName) {
		Session session = sessionFactory.getCurrentSession();
		//SELECT PRO.code, PRO.name, PRO.price FROM Product PRO -->cách thường sử dụng
		//muốn sử dụng  NEW " + ProductInfo.class.getName() thì trong class ProductInfo phải khai báo Constructor
		//có 3 tham số là code(String), name(String), price(double)
		String hql = "SELECT NEW " + ProductInfo.class.getName() + " (PRO.code, PRO.name, PRO.price) FROM Product PRO";
		if (likeName != null && likeName.length() > 0) {
			hql += " WHERE LOWER(PRO.name) LIKE :LIKENAME ";
		}
		hql += " ORDER BY PRO.createDate DESC ";
		
		Query<ProductInfo> query = session.createQuery(hql);
		List<ProductInfo> productInfos = query.list();
		if (likeName != null && likeName.length() > 0) {
			query.setParameter("LIKENAME", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
	}
	
	@Override
	public Product getProductByCode(String code) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "SELECT PRO FROM Product PRO WHERE PRO.code = :CODE";
		Query<Product> query = session.createQuery(hql);
		query.setParameter("CODE", code);
		Product product = (Product) query.uniqueResult();
		return product;
	}

}
