package io.pivotal.pal.tracker.restsupport;

public interface ServiceLocator {
	String lookUpServiceUrl(String serviceName);
}
