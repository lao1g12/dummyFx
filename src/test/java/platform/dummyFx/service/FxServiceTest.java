package platform.dummyFx.service;

import org.junit.Before;
import org.junit.Test;
import platform.dummyFx.domain.Order;

import java.util.TreeMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class FxServiceTest {

	private OrderStorage orderStorage;
	private FxService fxService;
	private Order exampleBidOrder1;
	private Order exampleBidOrder2;
	private Order exampleBidOrder3;
	private Order exampleAskOrder1;
	private Order exampleAskOrder2;

	@Before
	public void setup(){
		initMocks(this);
		orderStorage = new OrderStorage();
		fxService = new FxService(orderStorage);
		exampleBidOrder1 = new Order(1, "BID", "USD", 1.21, 10);
		exampleBidOrder2 = new Order(2, "BID", "USD", 1.21, 17);
		exampleBidOrder3 = new Order(3, "BID", "USD", 1.21, 7);
		exampleAskOrder1 = new Order(4, "ASK", "USD", 1.21, 17);
		exampleAskOrder2 = new Order(5, "ASK", "USD", 1.21, 6);
	}

	@Test
	public void placeAskOrder_noBidOrders(){
		fxService.placeOrder(exampleAskOrder1);
		TreeMap<Integer, Order> unMatchedAsks = orderStorage.getUnMatchedAsks();
		assertEquals(unMatchedAsks.get(4).getAmount(), 17, 0);
	}

	@Test
	public void placeBidOrder_noAskOrders(){
		fxService.placeOrder(exampleBidOrder1);
		TreeMap<Integer, Order> unMatchedBids = orderStorage.getUnMatchedBids();
		assertEquals(unMatchedBids.get(1).getAmount(), 10, 0);
	}

	@Test
	public void placeAskOrder_equalBidOrder(){
		fxService.placeOrder(exampleAskOrder1);
		fxService.placeOrder(exampleBidOrder2);
		TreeMap<Integer, Order> completedOrders = orderStorage.getCompletedOrders();
		assertEquals(completedOrders.get(4).getAmount(), 17, 0);
		assertEquals(completedOrders.get(2).getAmount(), 17, 0);
	}

	@Test
	public void placeAskOrder_unEqualBidOrder_askOrderGreater(){
		fxService.placeOrder(exampleAskOrder1);
		fxService.placeOrder(exampleBidOrder1);
		TreeMap<Integer, Order> completedOrders = orderStorage.getCompletedOrders();
		TreeMap<Integer, Order> unMatchedAskOrders = orderStorage.getUnMatchedAsks();
		assertEquals(completedOrders.get(4).getAmount(), 10, 0);
		assertEquals(completedOrders.get(1).getAmount(), 10, 0);
		assertEquals(unMatchedAskOrders.get(4).getAmount(), 7, 0);
	}

	@Test
	public void placeAskOrder_unEqualBidOrder_askOrderLower(){
		fxService.placeOrder(exampleAskOrder2);
		fxService.placeOrder(exampleBidOrder1);
		TreeMap<Integer, Order> completedOrders = orderStorage.getCompletedOrders();
		TreeMap<Integer, Order> unMatchedBidOrders = orderStorage.getUnMatchedBids();
		assertEquals(completedOrders.get(5).getAmount(), 6, 0);
		assertEquals(completedOrders.get(1).getAmount(), 6, 0);
		assertEquals(unMatchedBidOrders.get(1).getAmount(), 4, 0);
	}

	@Test
	public void placeAskOrder_unEqualBidOrder_remainingAskOrderMatched(){
		fxService.placeOrder(exampleAskOrder1);
		fxService.placeOrder(exampleAskOrder2);
		fxService.placeOrder(exampleBidOrder1);
		TreeMap<Integer, Order> completedOrders = orderStorage.getCompletedOrders();
		TreeMap<Integer, Order> unMatchedAskOrders = orderStorage.getUnMatchedAsks();
		assertEquals(completedOrders.get(4).getAmount(), 10, 0);
		assertEquals(completedOrders.get(1).getAmount(), 10, 0);
		assertEquals(unMatchedAskOrders.get(4).getAmount(), 7, 0);
		assertEquals(unMatchedAskOrders.get(5).getAmount(), 6, 0);
		fxService.placeOrder(exampleBidOrder3);
		assertEquals(completedOrders.get(4).getAmount(), 17, 0);
		assertEquals(completedOrders.get(1).getAmount(), 10, 0);
		assertEquals(completedOrders.get(3).getAmount(), 7, 0);
	}
}
