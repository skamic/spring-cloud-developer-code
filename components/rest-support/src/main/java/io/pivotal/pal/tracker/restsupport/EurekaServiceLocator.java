package io.pivotal.pal.tracker.restsupport;

import com.netflix.discovery.EurekaClient;

public class EurekaServiceLocator implements ServiceLocator {

	private EurekaClient eurekaClient;

	public EurekaServiceLocator(EurekaClient eurekaClient) {
		this.eurekaClient = eurekaClient;
	}

	@Override
	public String lookUpServiceUrl(String serviceName) {
		return eurekaClient.getNextServerFromEureka(serviceName, false).getHomePageUrl();
	}
}
