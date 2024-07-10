package sg.nus.iss.com.Leaveapp.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import sg.nus.iss.com.Leaveapp.Exceptions.TypeNotFoundException;

//@Entity
//@Table(name="leave_type")
public class LeaveType {
	
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

//    @OneToMany(mappedBy="type", fetch=FetchType.LAZY)
    private List<Leave> leaves;
    
//    @OneToOne(mappedBy="leaveType")
    private LeaveEntitlement entitlement;
    
//    @ManyToMany
    private List<Role> roles;
    // Getters and Setters
    
    public Long getId() { return id; }
    
    public LeaveType(Long id, String type) {
		super();
		this.id = id;
		this.type = type;
	}
    
    public LeaveType() {}

	public void setId(Long id) { this.id = id; }
        
    public String getType() { return type; }
    
    public void setType(String type) { this.type = type; }
    
    public static final LeaveType annual = new LeaveType(1L, "annual");
    
    public static final LeaveType medical = new LeaveType(2L, "medical");
    
    public static final LeaveType compensation = new LeaveType(3L, "compensation");
    
    public static LeaveType of(String typeId) throws TypeNotFoundException {
		switch (typeId) {
			case "1":
				return annual;
			case "2":
				return medical;
			case "3":
				return compensation;
			default:
				throw new TypeNotFoundException(typeId + "does not have an associated leave type");
		}
    }
    
}
