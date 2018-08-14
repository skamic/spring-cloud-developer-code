package io.pivotal.pal.tracker.projects;

import static io.pivotal.pal.tracker.projects.ProjectInfo.projectInfoBuilder;
import static io.pivotal.pal.tracker.projects.data.ProjectFields.projectFieldsBuilder;
import static java.util.stream.Collectors.toList;

import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.pal.tracker.projects.data.ProjectDataGateway;
import io.pivotal.pal.tracker.projects.data.ProjectFields;
import io.pivotal.pal.tracker.projects.data.ProjectRecord;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	private final ProjectDataGateway gateway;
	private LocalTime startupTime;

	public ProjectController(ProjectDataGateway gateway) {
		this.gateway = gateway;
		this.startupTime = LocalTime.now();
	}

	@PostMapping
	public ResponseEntity<ProjectInfo> create(@RequestBody ProjectForm form) {
		ProjectRecord record = gateway.create(formToFields(form));
		return new ResponseEntity<>(present(record), HttpStatus.CREATED);
	}

	@GetMapping
	public List<ProjectInfo> list(@RequestParam long accountId) {
		return gateway.findAllByAccountId(accountId).stream().map(this::present).collect(toList());
	}

	@InstrumentLatency
	@GetMapping("/{projectId}")
	public ProjectInfo get(@PathVariable long projectId) throws InterruptedException {

		ProjectRecord record = gateway.find(projectId);

		if (record != null) {
			return present(record);
		}

		return null;
	}

	private ProjectFields formToFields(ProjectForm form) {
		return projectFieldsBuilder().accountId(form.accountId).name(form.name).active(form.active).build();
	}

	private ProjectInfo present(ProjectRecord record) {
		return projectInfoBuilder().id(record.id).accountId(record.accountId).name(record.name).active(record.active)
				.info("project info").build();
	}
}