package com.shoppingcart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.shoppingcart.dao.OrderDAO;
import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.model.OrderDetailInfo;
import com.shoppingcart.model.OrderInfo;
import com.shoppingcart.model.PaginationResult;
import com.shoppingcart.model.ProductInfo;
import com.shoppingcart.validator.CustomerInfoValidator;
import com.shoppingcart.validator.ProductInfoValidator;

@Controller
public class AdminController {

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private ProductInfoValidator productInfoValidator;

	@RequestMapping("/403")
	public String accessDenied() {
		return "/403";
	}

	@RequestMapping("/")
	public String home() {
		return "index";
	}

	// GET: Hiển thị trang login
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping(value = { "/accountInfo" }, method = RequestMethod.GET)
	public String accountInfo(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("username: " + userDetails.getUsername());
		System.out.println("password: " + userDetails.getPassword());
		System.out.println("enable: " + userDetails.isEnabled());

		model.addAttribute("userDetails", userDetails);
		return "accountInfo";
	}

	@RequestMapping(value = { "/orderList" }, method = RequestMethod.GET) // orderList?page=2
	public String orderList(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;
		PaginationResult<OrderInfo> paginationOrderInfos = orderDAO.getAllOrderInfos(page, MAX_RESULT,
				MAX_NAVIGATION_PAGE);
		model.addAttribute("paginationOrderInfos", paginationOrderInfos);
		return "orderList";
	}

	@RequestMapping(value = { "/order" }, method = RequestMethod.GET)
	public String orderView(Model model, @RequestParam("orderId") String orderId) {
		OrderInfo orderInfo = null;
		if (orderId != null) {
			orderInfo = orderDAO.getOrderInfoById(orderId);
		}
		if (orderInfo == null) {
			return "redirect:/orderList";
		}

		List<OrderDetailInfo> orderDetailInfos = orderDAO.getAllOrderDetailInfos(orderId);
		orderInfo.setOrderDetailInfos(orderDetailInfos);
		model.addAttribute("orderInfo", orderInfo);
		return "order";
	}

	// GET: Hiển thị product // create -->/product  edit -->/product?code=s1
	@RequestMapping(value = { "/product" }, method = RequestMethod.GET)
	public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
		ProductInfo productInfo = null;
		if (code != null && code.length() > 0) {
			productInfo = productDAO.getProductInfoByCode(code);
		}
		if (productInfo == null) {
			productInfo = new ProductInfo();
			productInfo.setNewProduct(true);
		}

		model.addAttribute("productForm", productInfo);
		return "product";
	}

	// POST: Save product
	@RequestMapping(value = { "/product" }, method = RequestMethod.POST)
	//@Transactional(propagation = Propagation.NEVER) // Tránh ngoại lệ: UnexpectedRollbackException
	public String productSave(Model model, @ModelAttribute("productForm") @Validated ProductInfo productInfo,
			BindingResult result) {
		productInfoValidator.validate(productInfo, result);
		if (result.hasErrors()) {
			return "product";
		}

		try {
			productDAO.saveProductInfo(productInfo);
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "product";
		}
		return "redirect:/productList";
	}

}
