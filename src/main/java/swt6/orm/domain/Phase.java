package swt6.orm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Phase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	
	private Set<LogbookEntry> logbooks = new HashSet<>();
	
	public Phase() {
	}
	
	public Phase(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<LogbookEntry> getLogbooks() {
		return logbooks;
	}
	public void setLogbooks(Set<LogbookEntry> logbooks) {
		this.logbooks = logbooks;
	}
	
	public void addLogbook(LogbookEntry logbook) {
		if (logbook == null) {
			throw new IllegalArgumentException("Cannot add NULL logbook");
		}
		
		logbook.attachPhase(this);
		logbooks.add(logbook);
	}
	
	public void removeLogbook(LogbookEntry logbook) {
		if (logbook == null) {
			throw new IllegalArgumentException("Cannot remove NULL logbook");
		}
		
		logbook.detachPhase();
		logbooks.remove(logbook);
	}
}
