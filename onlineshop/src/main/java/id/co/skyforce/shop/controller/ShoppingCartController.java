package id.co.skyforce.shop.controller;

import id.co.skyforce.shop.model.Customer;
import id.co.skyforce.shop.model.Order;
import id.co.skyforce.shop.model.OrderDetail;
import id.co.skyforce.shop.model.Product;
import id.co.skyforce.shop.service.ProductListService;
import id.co.skyforce.shop.service.ProductService;
import id.co.skyforce.shop.service.ShoppingCartService;
import id.co.skyforce.shop.util.HibernateUtil;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 
 * @author Irwansyah Hazniel
 *
 */

@ManagedBean
@SessionScoped
public class ShoppingCartController implements Serializable {
	
	private Integer totalItem = 0;
	private Long productId;
	private BigDecimal totalAmount;
	
	// pass dari loginController
	private Customer customer;
	
	@ManagedProperty(value="#{loginController}")
    private LoginController loginController;
	
	private Product product;
	private Map<Product, Long> productsAndQuantity = new HashMap<>();
	
	private boolean editable = true;
	private Long quantity;
	
	public void incrementTotalItem() {
		totalItem += 1;
		calculateTotalAmount();
	}
	
	
	
	public Set<Entry<Product, Long>> getItems(){
		return productsAndQuantity.entrySet();
	}
	
	public void addProduct(Product product) {
		
		// check apakah product sudah ada di map
		long incrementQuantity = productsAndQuantity.containsKey(product) ? productsAndQuantity.get(product) : 0;
		
		// add product to map product
		productsAndQuantity.put(product, incrementQuantity + 1);
		
	}
	
	public void decrementTotalItem(){
		totalItem -=1;
		calculateTotalAmount();
	}
	
	public void delete(Product product) {

/*		String passProductId = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("productDelete");*/
		
		productsAndQuantity.remove(product);

		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			externalContext.redirect("cart.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void removeProduct(){
		delete(product);
		decrementTotalItem();
		editable = true;
	}
	
	public String editQuantity(){
		editable = false;
		return null;
		
	}
	
	
	public void saveQuantity(Product product, Long quantity){
		editable = true;
		
		productsAndQuantity.put(product, quantity);
		
	}
	
	public void saveProdQua(){
		saveQuantity(product, quantity);
	}
	
	

	
	public void calculateTotalAmount() {
		
		ShoppingCartService cartService = new ShoppingCartService();
		totalAmount = cartService.totalAmountService(productsAndQuantity);
		
	}
	
	public String checkout() {
		
		customer = loginController.cust;
		if (customer.getEmail() == null) {
			return "/user/login.xhtml";
		}
		return "checkout";
		
	}
	
	public String prosesBeli() {
		
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		
		Order order = new Order();
		
		order.setDate(new Date());
		order.setCustomer(customer);
		order.setTotalAmount(totalAmount);
		
		for(Entry<Product, Long> e : productsAndQuantity.entrySet()) {
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setProduct(e.getKey());
			orderDetail.setQuantity(e.getValue().intValue());
			orderDetail.setPrice(e.getKey().getPrice());
			order.getOrderDetails().add(orderDetail);
			session.save(orderDetail);
			
		}
		
		session.save(order);

		transaction.commit();
		session.close();
		
		// clear object's contents
		totalItem = 0;
		totalAmount = new BigDecimal(0);
		productsAndQuantity = new HashMap<>();
		
		return "successtransaction";
		
	}
	

	
	public String cancel() {
		return "index";
	}
	
	public Integer getTotalItem() {
		return totalItem;
	}
	
	public void setTotalItem(Integer totalItem) {
		this.totalItem = totalItem;
	}

	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Map<Product, Long> getProductsAndQuantity() {
		return productsAndQuantity;
	}

	public void setProductsAndQuantity(Map<Product, Long> productsAndQuantity) {
		this.productsAndQuantity = productsAndQuantity;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}



	public boolean isEditable() {
		return editable;
	}



	public void setEditable(boolean editable) {
		this.editable = editable;
	}



	public Long getQuantity() {
		return quantity;
	}



	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	
	
}
