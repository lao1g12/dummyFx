package platform.dummyFx;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("exchange-rate")
public class ExchangeRatesConfig {

	private double gbpUsd;

	public double getGbpUsd() {
		return gbpUsd;
	}

	public void setGbpUsd(double gbpUsd) {
		this.gbpUsd = gbpUsd;
	}
}
