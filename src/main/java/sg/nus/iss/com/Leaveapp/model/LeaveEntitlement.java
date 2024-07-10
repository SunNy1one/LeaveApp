package sg.nus.iss.com.Leaveapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "leave_entitlements") // Updated table name
public class LeaveEntitlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Max(value = 20, message = "Annual leave cannot exceed 20 days")
    @Column(name = "annual_leave") // Updated column name
    @PositiveOrZero(message = "Annual leave must be zero or positive")
    private Integer annualLeave;

    @Max(value = 10, message = "Sick leave cannot exceed 10 days")
    @Column(name = "sick_leave") // Updated column name
    @PositiveOrZero(message = "Medical leave must be zero or positive")
    private Integer sickLeave;

    private String leaveType;
    
    @JsonIgnoreProperties(value = {"employees", "entitlements"})
    @ManyToOne(fetch=FetchType.EAGER)
    private Role role;

    @PositiveOrZero(message = "Number of days must be zero or positive")
    @Column(name = "number_of_days") // Updated column name
    private int numberOfDays;

    @Min(value = 2020, message = "Year must be greater than or equal to 2020")
    private int year;

    public LeaveEntitlement(Integer annual, Integer sick) {
    	this.annualLeave = annual;
    	this.sickLeave = sick;
    }

    public LeaveEntitlement(String type, int numberOfDays, Role role, int year) {
        this.numberOfDays = numberOfDays;
        this.year = year;
        this.leaveType = type;
        this.role = role;
    }
    
    public LeaveEntitlement() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public Integer getAnnualLeave() {
		return annualLeave;
	}

	public void setAnnualLeave(Integer annualLeave) {
		this.annualLeave = annualLeave;
	}

	public Integer getSickLeave() {
		return sickLeave;
	}

	public void setSickLeave(Integer sickLeave) {
		this.sickLeave = sickLeave;
	}
	
	
	
}
