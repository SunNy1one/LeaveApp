package sg.nus.iss.com.Leaveapp.model;

import lombok.*;
import lombok.experimental.Accessors;
import sg.nus.iss.com.Leaveapp.enums.LeaveStatusEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LeaveVo extends Leave {
    private LeaveStatusEnum leaveStatus;

	public LeaveStatusEnum getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(LeaveStatusEnum leaveStatus) {
		this.leaveStatus = leaveStatus;
	}
    
    
}
