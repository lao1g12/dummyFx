package platform.dummyFx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.dummyFx.domain.Order;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@Service
public class FxService {

	private OrderStorage orderStorage;

	@Autowired
	public FxService(OrderStorage orderStorage) {
		this.orderStorage = orderStorage;
	}

	public void placeOrder(Order order){
		TreeMap bidOrders = orderStorage.getUnMatchedBids();
		TreeMap askOrders = orderStorage.getUnMatchedAsks();
		if(order.getOrderType().equals("BID")){
			processOrder(bidOrders, askOrders, order);
		}
		if (order.getOrderType().equals("ASK")){
			processOrder(askOrders, bidOrders, order);
		}
	}

	private void processOrder(TreeMap<Integer, Order> currentOrderMap, TreeMap<Integer, Order> oppositeOrderMap, Order order) {
		if(oppositeOrderMap.size() > 0){
			findMatchingOrder(order, oppositeOrderMap, currentOrderMap);
		}else{
			currentOrderMap.put(order.getUserId(), order);
		}
	}

	private void findMatchingOrder(Order order, TreeMap<Integer, Order> oppositeOrderMap, TreeMap<Integer, Order> currentOrderMap) {
		double orderValue = order.getAmount();
		TreeMap completeOrders = orderStorage.getCompletedOrders();
		ArrayList<Integer> tempCompletedOrders = new ArrayList<>();
		for (Map.Entry<Integer, Order> entry : oppositeOrderMap.entrySet()) {
			Integer userId = entry.getKey();
			Order currentOrder = entry.getValue();
			double currentOrderAmount = currentOrder.getAmount();
			if (orderValue == 0) {
				break;
			}
			orderValue = matchOrder(orderValue, tempCompletedOrders, userId, currentOrder, currentOrderAmount);
		}
		if (orderValue > 0){
			Order newOrder = new Order(order.getUserId(), order.getOrderType(),
					order.getCurrency(), order.getRate(), orderValue);
			currentOrderMap.put(newOrder.getUserId(), newOrder);
			Order newCompleteOrder = new Order(order.getUserId(), order.getOrderType(),
					order.getCurrency(), order.getRate(), order.getAmount()-orderValue);
			completeOrders.put(newCompleteOrder.getUserId(), newCompleteOrder);

		}else {
			completeOrders.put(order.getUserId(), order);
		}
		for (Integer i: tempCompletedOrders) {
			oppositeOrderMap.remove(i);
		}
	}

	private double matchOrder(double orderValue, ArrayList<Integer> tempCompletedOrders, Integer userId, Order currentOrder, double currentOrderAmount) {
		if (currentOrderAmount <= orderValue) {
			orderValue = orderValue - currentOrderAmount;
			addToCompleted(userId, currentOrderAmount, currentOrder);
			tempCompletedOrders.add(userId);
		} else {
			Order newOrder = new Order(currentOrder.getUserId(), currentOrder.getOrderType(),
					currentOrder.getCurrency(), currentOrder.getRate(), orderValue);
			double newValue = (currentOrderAmount - orderValue);
			orderValue = 0;
			addToCompleted(userId, currentOrderAmount, newOrder);
			currentOrder.setAmount(newValue);
		}
		return orderValue;
	}

	private void addToCompleted(Integer userId, double currentOrderAmount, Order newOrder) {
		TreeMap completedOrders = orderStorage.getCompletedOrders();
		if (completedOrders.containsKey(userId)) {
			Order tempOrder = (Order) completedOrders.get(userId);
			tempOrder.setAmount(tempOrder.getAmount() + currentOrderAmount);
		} else {
			completedOrders.put(userId, newOrder);
		}
	}
}
