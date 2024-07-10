package sg.nus.iss.com.Leaveapp.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name="claims")
public class Claim {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Employee employee;
	
	@Positive(message="Days to claim cannot be negative.")
	private double claimDays;

	private double approvedDays;

	
	@NotBlank(message="Reason cannot be blank.")
	private String reasonSupporting;
	
	private LocalDate dateOfSubmission;
	
	private LocalDate dateApproved;
	
	private int status;
	
	private String comments;
	
	

	public Claim(Employee employee, double claimDays, String reasonSupporting, int status) {
		super();
		this.employee = employee;
		this.claimDays = claimDays;
		this.reasonSupporting = reasonSupporting;
		this.status = status;
		this.comments = "";
		this.dateOfSubmission = LocalDate.now();
		this.approvedDays = 0;
	}
	
	public Claim() {
		this.status = LeaveStatus.Applied;
		this.comments = "";
		this.dateOfSubmission = LocalDate.now();
		this.approvedDays = 0;
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

	public double getClaimDays() {
		return claimDays;
	}

	public void setClaimDays(double claimDays) {
		this.claimDays = claimDays;
	}

	public String getReasonSupporting() {
		return reasonSupporting;
	}

	public void setReasonSupporting(String reasonSupporting) {
		this.reasonSupporting = reasonSupporting;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LocalDate getDateOfSubmission() {
		return dateOfSubmission;
	}
	
	public String displayDateOfSubmission() {
		return dateOfSubmission.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
	}

	public void setDateOfSubmission(LocalDate dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}

	public LocalDate getDateApproved() {
		return dateApproved;
	}
	
	public String displayDateApproved() {
		return dateApproved.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
	}

	public void setDateApproved(LocalDate dateApproved) {
		this.dateApproved = dateApproved;
	}
	
	public String displayStatus() {
		return LeaveStatus.asString(status);
	}
	
	public String showDateApproved() {
		if(dateApproved != null) {
			return dateApproved.toString();
		}
		return "";
	}
	
	@Override
	public String toString() {
		return "Claim made on " + dateOfSubmission + " by employee (id:" + employee.getId() + ") for " + claimDays + " days";
	}

	public double getApprovedDays() {
		return approvedDays;
	}

	public void setApprovedDays(double approvedDays) {
		this.approvedDays = approvedDays;
	}
	
	
}
