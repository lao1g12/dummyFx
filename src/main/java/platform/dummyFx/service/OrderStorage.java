package platform.dummyFx.service;

import org.springframework.stereotype.Service;
import platform.dummyFx.domain.Order;

import java.util.TreeMap;

@Service
public class OrderStorage {
	private TreeMap<Integer, Order> completedOrders = new TreeMap<Integer, Order>();
	private TreeMap<Integer,Order> bidOrders = new TreeMap<Integer, Order>();
	private TreeMap<Integer,Order> askOrders = new TreeMap<Integer, Order>();

	public TreeMap getCompletedOrders() {
		return completedOrders;
	}

	public TreeMap getUnMatchedBids(){
		return bidOrders;
	}

	public TreeMap getUnMatchedAsks(){
		return askOrders;
	}

	public String removeOrder(int userId){
		if(completedOrders.containsKey(userId)){
			return "This order has already been completed";
		}
		if(bidOrders.containsKey(userId)){
			bidOrders.remove(userId);
			return "Removed";
		}
		if(askOrders.containsKey(userId)){
			askOrders.remove(userId);
			return "Removed";
		}
		return "This is not a valid order";
	}
}
