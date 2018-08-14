package io.pivotal.pal.tracker.timesheets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import io.pivotal.pal.tracker.timesheets.data.TimeEntryDataGateway;

@Component
public class ServerHealthCheck implements HealthIndicator {

	private static final int DOWN_STATUS_START = 5;
	private static final int DOWN_STATUS_END = 10;

	private final Logger logger = LoggerFactory.getLogger(ServerHealthCheck.class);

	private final TimeEntryDataGateway gateway;

	public ServerHealthCheck(TimeEntryDataGateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public Health health() {

		int numberOfTimeEntries = gateway.findAllByUserId(1).size();
		logger.info("Number of Time entries = " + numberOfTimeEntries);

		if (numberOfTimeEntries > DOWN_STATUS_START && numberOfTimeEntries < DOWN_STATUS_END) {
			return Health.down().withDetail("numberOfTimeEntries", numberOfTimeEntries).build();
		} else {
			return Health.up().build();
		}
	}
}