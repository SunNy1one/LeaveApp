package sg.nus.iss.com.Leaveapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.com.Leaveapp.model.Leave;
import sg.nus.iss.com.Leaveapp.model.LeaveEntitlement;
import sg.nus.iss.com.Leaveapp.model.LeaveStatus;
import sg.nus.iss.com.Leaveapp.model.LeaveType;

import java.util.List;
import java.util.Optional;


public interface LeaveApproveListRepository extends JpaRepository<Leave, Long> {

    Optional<Leave> findById(Long id);


    List<Leave> findLeavesByStatusOrderByStartDesc(int status);


    List<Leave> findLeavesByEmployee_IdOrderByIdDesc(Long id);
    
    List<Leave> findAllByOrderByIdDesc();
    
    List<Leave> findByEmployeeIdAndEntitlementAndStatusOrderByIdDesc(Long employeeId, LeaveEntitlement entitlement, int status);
    
    
}
