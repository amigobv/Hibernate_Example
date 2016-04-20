package swt6.orm.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

public class LogbookEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String activity;
	private Date startTime;
	private Date endTime;
	private Employee employee;
	private Phase phaseId;
	private Module moduleId;

	public LogbookEntry() {
	}

	public LogbookEntry(String activity, Date start, Date end) {
		this.activity = activity;
		this.startTime = start;
		this.endTime = end;
	}
	
	public LogbookEntry(String activity, Date start, Date end, Phase phase, Module module) {
		this(activity, start, end);
		attachPhase(phase);
		attachModule(module);
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Employee getEmployee() {
		return employee;
	}

	// setEmployee is also invoked when logbook entries are being
	// loaded lazily. This causes an exception.
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void attachEmployee(Employee employee) {
		if (employee == null)
			throw new IllegalArgumentException("Employee must not be null!");

		if (this.employee != null) {
			this.employee.getLogbookEntries().remove(this);
		}

		this.employee = employee;
		this.employee.getLogbookEntries().add(this);
	}

	public void detachEmployee() {
		if (employee != null)
			employee.getLogbookEntries().remove(this);

		employee = null;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date end) {
		this.endTime = end;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date start) {
		this.startTime = start;
	}

	public Phase getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Phase phaseId) {
		this.phaseId = phaseId;
	}
	
	public void attachPhase(Phase phase) {
		if (phase == null)
			throw new IllegalArgumentException("Cannot attach NULL phase!");
		
		if (this.phaseId != null) {
			this.phaseId.getLogbooks().remove(this);
		}
		
		phase.getLogbooks().add(this);
		this.phaseId = phase;
	}
	
	public void detachPhase() {
		if (this.phaseId != null) {
			this.phaseId.getLogbooks().remove(this);
		}
		
		phaseId = null;
	}

	public Module getModuleId() {
		return moduleId;
	}

	public void setModuleId(Module moduleId) {
		this.moduleId = moduleId;
	}
	
	public void attachModule(Module module) {
		if (module == null)
			throw new IllegalArgumentException("Cannot attach NULL module!");
		
		if (this.moduleId != null) {
			this.moduleId.getLogbooks().remove(this);
		}
		
		module.getLogbooks().add(this);
		this.moduleId = module;
	}
	
	public void detachModule() {
		if (this.moduleId != null) {
			this.moduleId.getLogbooks().remove(this);
		}
		
		this.moduleId = null;
	}

	@Override
	public String toString() {
		DateFormat fmt = DateFormat.getDateTimeInstance();
		return activity + ": " + fmt.format(startTime) + " - " + fmt.format(endTime) + " ("
				+ getEmployee().getLastName() + ")";

	}
}
