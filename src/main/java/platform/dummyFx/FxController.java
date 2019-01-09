package platform.dummyFx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import platform.dummyFx.domain.Order;
import platform.dummyFx.domain.RequestOrder;
import platform.dummyFx.service.FxService;
import platform.dummyFx.service.OrderStorage;

import java.util.TreeMap;

@RestController
public class FxController {

	private int userNumberCounter;

	private FxService fxService;
	private OrderStorage orderStorage;
	private ExchangeRatesConfig ratesConfig;

	@Autowired
	public FxController(FxService fxService, OrderStorage orderStorage, ExchangeRatesConfig ratesConfig) {
		this.fxService = fxService;
		this.orderStorage = orderStorage;
		this.ratesConfig = ratesConfig;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/order", produces = "application/json")
	public Order placeOrder(@RequestBody RequestOrder requestOrder) {
		userNumberCounter += 1;
		Order order = new Order(userNumberCounter, requestOrder.getOrderType(),requestOrder.getCurrency(), ratesConfig.getGbpUsd(), requestOrder.getAmount());
		fxService.placeOrder(order);
		return order;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/order/complete", produces = "application/json")
	public TreeMap getCompleted() {
		return orderStorage.getCompletedOrders();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/order/bid", produces = "application/json")
	public TreeMap getUnMatchedBid() {
		return orderStorage.getUnMatchedBids();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/order/ask", produces = "application/json")
	public TreeMap getUnMatchedAsk() {
		return orderStorage.getUnMatchedAsks();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/order/{userId}", produces = "application/json")
	public String removerOrder(@PathVariable int userId) {
		return orderStorage.removeOrder(userId);
	}

}
