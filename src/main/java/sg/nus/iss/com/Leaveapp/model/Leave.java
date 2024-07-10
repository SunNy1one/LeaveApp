package sg.nus.iss.com.Leaveapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "leaves")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @JsonIgnoreProperties(value = {"manager", "role", "reportees", "claims"})
    private Employee employee;

    private LocalDate start;
    
    private int startMonth;
    
    private LocalDate end;
    private String reasons;
    private String nameOfSupportingCoworker;

    private boolean overseas;

    private String overseasContact;

    private String comment;

    private Integer status;

    private boolean halfDayLeave;

    private Integer halfOfDay;
	
    @ManyToOne(fetch = FetchType.EAGER)
    private LeaveEntitlement entitlement;
    //page number
    @JsonIgnore
    @Transient
    private Integer pageNo;
    //page size
    @JsonIgnore
    @Transient
    private Integer pageSize;
    //search parameters
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Transient
    private Map<String, Object> params;

    public Leave() {
        this.start = LocalDate.now();
        this.end = LocalDate.now();
        this.comment = "";
        this.halfDayLeave = false;
        this.halfOfDay = HalfOfDay.Morning;
        this.startMonth = start.getMonthValue();
    }

	public Leave(Employee employee, LocalDate start, LocalDate end, LeaveEntitlement entitlement, String reasons, int status) {
		super();
		this.employee = employee;
		this.start = start;
		this.end = end;
		this.entitlement = entitlement;
		this.reasons = reasons;
		this.setStatus(status);
		this.comment = "";
		this.halfDayLeave = false;
		this.halfOfDay = HalfOfDay.Morning;
		this.startMonth = start.getMonthValue();
	}
    public Map<String, Object> getParams() {
        return params;
    }

    public Leave setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Leave setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Leave setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public int getStartMonth() {
		return start.getMonth().getValue();
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LeaveEntitlement getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(LeaveEntitlement type) {
		this.entitlement = type;
	}

	public String getNameOfSupportingCoworker() {
		return nameOfSupportingCoworker;
	}

	public void setNameOfSupportingCoworker(String nameOfSupportingCoworker) {
		this.nameOfSupportingCoworker = nameOfSupportingCoworker;
	}

	public boolean isOverseas() {
		return overseas;
	}

	public void setOverseas(boolean overseas) {
		this.overseas = overseas;
	}

	public String getOverseasContact() {
		return overseasContact;
	}

	public void setOverseasContact(String overseasContact) {
		this.overseasContact = overseasContact;
	}
	
	public Double getNumberOfDays() {
		if(halfDayLeave) {
			return 0.5;
		}
		Integer range = Integer.parseInt((end.toEpochDay() - start.toEpochDay() + 1) + "");
		if(range > 14) {
			return range * 1.0;
		} else {
			return range - getNumberOfWeekendDaysInLeaveRange(range) * 1.0;
		}
	}
	
	private Integer getNumberOfWeekendDaysInLeaveRange(int range) {
		Integer count = 0;
		for(Long i = 0L; i < range; i++) {
			if(isWeekendDayOfWeek(start.plusDays(i).getDayOfWeek())){
				count++;
			}
		}
		return count;
	}
	
	private Boolean isWeekendDayOfWeek(DayOfWeek dayOfWeek) {
		return dayOfWeek.compareTo(DayOfWeek.SATURDAY) == 0 || dayOfWeek.compareTo(DayOfWeek.SUNDAY) == 0;
	}
	
	public Boolean isOverlappedWith(List<Leave> consumedLeave) {
		boolean hasOverlap = false;
		for(Leave l : consumedLeave) {
			if(isOverlappedWith(l)) {
				hasOverlap = true;
			}
		}
		return hasOverlap;
	}
	

	public Boolean isOverlappedWith(Leave other) {
		if(getStart().compareTo(other.getStart()) == 0) {
			return true;
		}
		if(getStart().compareTo(other.getStart()) < 0) {
			return getEnd().compareTo(other.getStart()) >= 0;
		} else {
			return other.getEnd().compareTo(getStart()) >= 0;
		}
	}
	
	public Boolean isConsumedOrConsuming() {
		return this.status == LeaveStatus.Updated ||this.status == LeaveStatus.Applied || this.status == LeaveStatus.Approved;
	}
	
	public static Double consumedDaysOfLeave(List<Leave> consumedLeaves, String type) {
		List<Leave> consumedLeavesOfGivenType = consumedLeaves
		.stream()
		.filter(l -> l.getEntitlement().getLeaveType().compareTo(type) == 0).toList();
		
		if(consumedLeavesOfGivenType.isEmpty()) {
			return 0.0;
		}
		return consumedLeavesOfGivenType
				.stream()
				.map(l -> {
					System.out.println("Leave from: " + l.getStart() + " to " + l.getEnd() + ". Number of days: " + l.getNumberOfDays());
					return l.getNumberOfDays();
				})
				.reduce((d1, d2) -> d1 + d2)
				.get();
	}
	
	public Boolean isCancellable() {
		return this.status == LeaveStatus.Approved;
	}
	
	public Boolean isUpdateable() {
		return this.status == LeaveStatus.Updated ||this.status == LeaveStatus.Applied;
	}
	
	public Boolean isDeletable() {
		return this.isUpdateable();
	}
	
	public String getLocalStartDate() {
		return start.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
	}
	
	public String getLocalEndDate() {
		return end.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String displayStatus() {
		return LeaveStatus.asString(status);
	}

	public String displayEnd() {
		if(halfDayLeave) {
			return HalfOfDay.asString(halfOfDay);
		}
		return getLocalEndDate();
	}

	public boolean isHalfDayLeave() {
		return halfDayLeave;
	}

	public void setHalfDayLeave(boolean halfDayLeave) {
		this.halfDayLeave = halfDayLeave;
	}

	public Integer getHalfOfDay() {
		return halfOfDay;
	}

	public void setHalfOfDay(Integer halfOfDay) {
		this.halfOfDay = halfOfDay;
	}
	
	public String getStartDateISO() {
		return start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public String getEndDateISO() {
		return end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}

