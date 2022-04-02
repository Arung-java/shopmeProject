package com.amazon.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.amazon.common.entity.Customer;
import com.amazon.common.entity.order.Order;
import com.amazon.common.entity.order.OrderDetail;
import com.amazon.common.entity.order.OrderStatus;
import com.amazon.common.entity.order.OrderTrack;
import com.amazon.common.entity.order.PaymentMethod;
import com.amazon.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTests {

	@Autowired
	private OrderRepositroy repo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewOrderWithSingleProduct() {
		Customer customer = entityManager.find(Customer.class, 1);
		Product product = entityManager.find(Product.class, 1);

		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.copyAddressFromCustomer();

		mainOrder.setShippingCost(10);
		mainOrder.setProductCost(product.getCost());
		mainOrder.setTax(0);
		mainOrder.setSubtotal(product.getPrice());
		mainOrder.setTotal(product.getPrice() + 10);

		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.NEW);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(1);

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(mainOrder);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10);
		orderDetail.setQuantity(1);
		orderDetail.setSubtotal(product.getPrice());
		orderDetail.setUnitPrice(product.getPrice());

		mainOrder.getOrderDetails().add(orderDetail);
		Order savedOrder = repo.save(mainOrder);

		assertThat(savedOrder.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateNewOrderWithMultipleProducts() {

		Customer customer = entityManager.find(Customer.class, 30);
		Product product1 = entityManager.find(Product.class, 10);
		Product product2 = entityManager.find(Product.class, 11);

		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.copyAddressFromCustomer();

		OrderDetail orderDetail1 = new OrderDetail();
		orderDetail1.setProduct(product1);
		orderDetail1.setOrder(mainOrder);
		orderDetail1.setProductCost(product1.getCost());
		orderDetail1.setShippingCost(10);
		orderDetail1.setQuantity(1);
		orderDetail1.setSubtotal(product1.getPrice());
		orderDetail1.setUnitPrice(product1.getPrice());

		OrderDetail orderDetail2 = new OrderDetail();
		orderDetail2.setProduct(product2);
		orderDetail2.setOrder(mainOrder);
		orderDetail2.setProductCost(product2.getCost());
		orderDetail2.setShippingCost(10);
		orderDetail2.setQuantity(2);
		orderDetail2.setSubtotal(product2.getPrice() * 2);
		orderDetail2.setUnitPrice(product2.getPrice());

		mainOrder.getOrderDetails().add(orderDetail1);
		mainOrder.getOrderDetails().add(orderDetail2);

		mainOrder.setShippingCost(30);
		mainOrder.setProductCost(product1.getCost() + product2.getCost());
		mainOrder.setTax(0);
		float subtotal = product1.getPrice() + product2.getPrice() * 2;
		mainOrder.setSubtotal(subtotal);
		mainOrder.setTotal(subtotal + 30);

		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.SHIPPING);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(3);

		Order savedOrder = repo.save(mainOrder);
		assertThat(savedOrder.getId()).isGreaterThan(0);
	}

	@Test
	public void testListOrders() {
		Iterable<Order> orders = repo.findAll();

		assertThat(orders).hasSizeGreaterThan(0);

		orders.forEach(System.out::println);
	}

	@Test
	public void testUpdateOrder() {
		Integer orderId = 3;
		Order order = repo.findById(orderId).get();
		order.setStatus(OrderStatus.SHIPPING);
		order.setPaymentMethod(PaymentMethod.COD);
		order.setOrderTime(new Date());
		order.setDeliverDays(2);

		Order updatedOrder = repo.save(order);
		assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPING);
	}

	@Test
	public void testGetOrder() {
		Integer orderId = 3;
		Order order = repo.findById(orderId).get();
		assertThat(order).isNotNull();
		System.out.println(order);
	}

	@Test
	public void testDeleteOrder() {
		Integer orderId = 3;
		repo.deleteById(orderId);
		Optional<Order> order = repo.findById(orderId);
		assertThat(order).isNotPresent();
	}
	@Test
	public void testUpdateOrderTracks() {
		Integer orderId=4;
		Order order = repo.findById(orderId).get();
		OrderTrack newTrack=new OrderTrack();
		newTrack.setOrder(order);
		newTrack.setUpdatedTime(new Date());
		newTrack.setStatus(OrderStatus.PICKED);
		newTrack.setNotes(OrderStatus.PICKED.defaultDescription());
		
		OrderTrack processingTrack=new OrderTrack();
		processingTrack.setOrder(order);
		processingTrack.setUpdatedTime(new Date());
		processingTrack.setStatus(OrderStatus.PACKAGED);
		processingTrack.setNotes(OrderStatus.PACKAGED.defaultDescription());
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		orderTracks.add(processingTrack);
		orderTracks.add(newTrack);
		
		Order updatedOrder = repo.save(order);
		assertThat(updatedOrder.getOrderTracks()).hasSizeGreaterThan(1);
		
		
		
		
	}
}
