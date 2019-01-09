package platform.dummyFx.domain;

public class Order {

	private int userId;
	private String orderType;
	private String currency;
	private double rate;
	private double amount;

	public Order(int userId, String orderType, String currency, double rate, double amount) {
		this.userId = userId;
		this.orderType = orderType;
		this.currency = currency;
		this.rate = rate;
		this.amount = amount;
	}

	public Order() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
