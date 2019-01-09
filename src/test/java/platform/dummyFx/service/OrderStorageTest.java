package platform.dummyFx.service;

import org.junit.Before;
import org.junit.Test;
import platform.dummyFx.domain.Order;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrderStorageTest {

	private OrderStorage orderStorage;
	private Order exampleBidOrder1;
	private Order exampleBidOrder2;
	private Order exampleBidOrder3;
	private Order exampleAskOrder1;
	private Order exampleAskOrder2;

	@Before
	public void setup(){
		initMocks(this);
		orderStorage = new OrderStorage();
		exampleBidOrder1 = new Order(1, "BID", "USD", 1.21, 10);
		exampleBidOrder2 = new Order(2, "BID", "USD", 1.21, 17);
		exampleBidOrder3 = new Order(3, "BID", "USD", 1.21, 7);
		exampleAskOrder1 = new Order(4, "ASK", "USD", 1.21, 17);
		exampleAskOrder2 = new Order(5, "ASK", "USD", 1.21, 6);
	}

	@Test
	public void removeBidOrder(){
		orderStorage.getUnMatchedBids().put(1, exampleBidOrder1);
		assertEquals(orderStorage.getUnMatchedBids().size(), 1);
		orderStorage.removeOrder(1);
		assertEquals(orderStorage.getUnMatchedBids().size(), 0);
	}

	@Test
	public void removeAskOrder(){
		orderStorage.getUnMatchedAsks().put(4, exampleAskOrder1);
		assertEquals(orderStorage.getUnMatchedAsks().size(), 1);
		orderStorage.removeOrder(4);
		assertEquals(orderStorage.getUnMatchedAsks().size(), 0);
	}

	@Test
	public void removeOrder_orderNotFound(){
		orderStorage.getUnMatchedBids().put(1, exampleBidOrder1);
		assertEquals(orderStorage.getUnMatchedBids().size(), 1);
		assertEquals(orderStorage.removeOrder(2), "This is not a valid order");
		assertEquals(orderStorage.getUnMatchedBids().size(), 1);
	}

	@Test
	public void removeOrder_orderAlreadyCompleted(){
		orderStorage.getCompletedOrders().put(1, exampleBidOrder1);
		assertEquals(orderStorage.getCompletedOrders().size(), 1);
		assertEquals(orderStorage.removeOrder(1), "This order has already been completed");
		assertEquals(orderStorage.getCompletedOrders().size(), 1);
	}
}
