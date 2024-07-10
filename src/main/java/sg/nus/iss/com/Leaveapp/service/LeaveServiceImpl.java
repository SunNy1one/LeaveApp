package sg.nus.iss.com.Leaveapp.service;

import cn.hutool.core.collection.CollectionUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sg.nus.iss.com.Leaveapp.enums.LeaveStatusEnum;
import sg.nus.iss.com.Leaveapp.model.*;
import sg.nus.iss.com.Leaveapp.model.LeaveVo;
import sg.nus.iss.com.Leaveapp.repository.ClaimRepository;
import sg.nus.iss.com.Leaveapp.repository.LeaveEntitlementRepository;
import sg.nus.iss.com.Leaveapp.repository.LeaveRepository;
import sg.nus.iss.com.Leaveapp.service.LeaveService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private ClaimRepository claimRepository;
	
    @Autowired
    private LeaveRepository leaveRepository;
    
    @Autowired 
    private LeaveEntitlementRepository leaveEntitlementRepository;

    @Override
    public Employee findEmployee(Long id) {
        return leaveRepository.findEmployeeById(id);
    }

    @Override
    public LocalDate findStartDate(Long id) {
        return leaveRepository.findStartDateById(id);
    }

    @Override
    public LocalDate findEndDate(Long id) {
        return leaveRepository.findEndDateById(id);
    }

    @Override
    public List<Employee> findEmployeesBetweenStartAndEndDate(LocalDate startDate, LocalDate endDate) {
        return leaveRepository.findEmployeesBetweenStartAndEndDate(startDate, endDate);
    }

    @Override
    public String findReason(Long id) {
        return leaveRepository.findReasonById(id);
    }

    @Override
    public LeaveStatus findLeaveStatus(Long id) {
        return leaveRepository.findLeaveStatusById(id);
    }
    
    @Override
    public Leave findById(Long id) {
        return leaveRepository.findById(id);
    }

    @Override
    public LeaveEntitlement findLeaveEntitlement(Long id) {
        return leaveRepository.findLeaveEntitlementById(id);
    }

    @Override
    public Long findIdByEmpId(Long empId) {
        return leaveRepository.findLeaveAppIdByEmpId(empId);
    }

    @Override
    public String findLeaveReasonsByEmpId(Long empId) {
        return leaveRepository.findLeaveAppReasonsByEmpId(empId);
    }

    @Override
    public LocalDate findLeaveAppStartDateByEmpId(Long empId) {
        return leaveRepository.findLeaveAppStartDateByEmpId(empId);
    }

    @Override
    public LocalDate findLeaveAppEndDateByEmpId(Long empId) {
        return leaveRepository.findLeaveAppEndDateByEmpId(empId);
    }

    @Override
    public LeaveStatus findLeaveappStatusByEmpId(Long empId) {
        return leaveRepository.findLeaveappStatusByEmpId(empId);
    }

    @Override
    public String findEmpNameByLeaveId(Long leaveId) {
        return leaveRepository.findEmpNameByLeaveId(leaveId);
    }

    @Override
    public String findEmpRoleByLeaveId(Long leaveId) {
        return leaveRepository.findEmpRoleByLeaveId(leaveId);
    }
    
    @Override
    public Leave save(Leave leave)
    {
    	return leaveRepository.save(leave);
    }
    
    
    @Override
    public List<Leave> findLeavesFromEmployeeId(Long id) {
    	return leaveRepository.findLeavesFromEmployeeId(id);
    }
    
    @Override
    public Claim saveClaim(Claim claim) {
    	return claimRepository.save(claim);
    }
    
    @Override
    public List<Claim> findClaimsByEmployee(Employee employee) {
    	return claimRepository.findClaimsByEmployee(employee);
    }
    
    @Override
    public List<Claim> findApprovedClaimsByEmployee(Employee employee) {
    	return claimRepository.findApprovedClaimsByEmployee(LeaveStatus.Approved, employee.getId());
    }
    
    @Override
    public Boolean hasUnconsumeClaimedLeaves(Employee employee) {
    	List<Claim> claimedLeaves = claimRepository.findApprovedClaimsByEmployee(employee.getId());
    	double totalClaimedLeaves = claimedLeaves
    			.stream()
    			.map(cl -> cl.getClaimDays())
    			.reduce((cd1, cd2) -> cd1 + cd2)
    			.orElse(0.0);
    	if(totalClaimedLeaves == 0.0) {
    		return false;
    	}
    	LeaveEntitlement compensationEntitlement = leaveEntitlementRepository.getCompensationEntitlement();
    	double totalConsumedClaimedLeaves = leaveRepository.findCompensationLeavesByEmployee(employee.getId(), compensationEntitlement.getId())
    			.stream()
    			.filter(cl -> LeaveStatus.getConsumedStatus().contains(cl.getStatus()))
    			.map(cl -> cl.getNumberOfDays())
    			.reduce((d1, d2) -> d1 + d2)
    			.orElse(0.0);
    	return totalClaimedLeaves > totalConsumedClaimedLeaves;
    }
    @Override
    public void saveOrUpdate(Leave leave) {
        leaveRepository.save(leave);
    }

    @Override
    public void removeBatchByIds(List<Integer> ids) {
        leaveRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public List<LeaveVo> getList(Leave leave) {
    	List<Leave> leaveList = leaveRepository.findAll(getSpecification(leave));
//        List<Leave> leaveList = leaveRepository.findAllApprovedLeavesByMonth(leave.getStart().getMonth().getValue());
        if (CollectionUtil.isEmpty(leaveList)) {
            return null;
        }
        // add VO
        return leaveList.stream().map(item -> {
            LeaveVo vo = new LeaveVo();
            BeanUtils.copyProperties(item, vo);
            vo.setLeaveStatus(LeaveStatusEnum.getByCode(item.getStatus()));
            return vo;
        }).toList();
    }

    @Override
    public Page<Leave> getPage(Leave leave) {
        return leaveRepository.findAll(getSpecification(leave), PageRequest.of(leave.getPageNo() - 1, leave.getPageSize()));
    }

    @Override
    public Leave getOne(Leave leave) {
        return leaveRepository.findOne(getSpecification(leave)).orElse(null);
    }

    private Specification<Leave> getSpecification(Leave leave) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), LeaveStatus.Approved));
            if (leave.getId() != null) {
                predicates.add(cb.equal(root.get("id"), leave.getId()));
            }
            if (leave.getEmployee() != null) {
                predicates.add(cb.equal(root.get("employee"), leave.getEmployee()));
            }
//            if (leave.getStart() != null) {
//                predicates.add(cb.equal(root.get("start"), leave.getStart()));
//            }
            if (leave.getStart() != null) {
                int year = leave.getStart().getYear();
                int month = leave.getStart().getMonthValue();
                Predicate yearPredicate = cb.equal(cb.function("YEAR", Integer.class, root.get("start")), year);
                Predicate monthPredicate = cb.equal(cb.function("MONTH", Integer.class, root.get("start")), month);
                predicates.add(cb.and(yearPredicate, monthPredicate));
            }
            if (leave.getEnd() != null) {
                predicates.add(cb.equal(root.get("end"), leave.getEnd()));
            }
            if (leave.getReasons() != null && !leave.getReasons().isEmpty()) {
                predicates.add(cb.like(root.get("reasons"), "%" + leave.getReasons() + "%"));
            }
            if (leave.getNameOfSupportingCoworker() != null && !leave.getNameOfSupportingCoworker().isEmpty()) {
                predicates.add(cb.like(root.get("nameOfSupportingCoworker"), "%" + leave.getNameOfSupportingCoworker() + "%"));
            }
            if (leave.isOverseas()) {
                predicates.add(cb.equal(root.get("overseas"), leave.isOverseas()));
            }
            if (leave.getOverseasContact() != null && !leave.getOverseasContact().isEmpty()) {
                predicates.add(cb.like(root.get("overseasContact"), "%" + leave.getOverseasContact() + "%"));
            }
            if (leave.getComment() != null && !leave.getComment().isEmpty()) {
                predicates.add(cb.like(root.get("comment"), "%" + leave.getComment() + "%"));
            }
            if (leave.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), leave.getStatus()));
            }
         
            if (leave.isHalfDayLeave()) {
                predicates.add(cb.equal(root.get("halfDayLeave"), leave.isHalfDayLeave()));
            }
            if (leave.getHalfOfDay() != null) {
                predicates.add(cb.equal(root.get("halfOfDay"), leave.getHalfOfDay()));
            }
            if (leave.getEntitlement() != null) {
                predicates.add(cb.equal(root.get("entitlement"), leave.getEntitlement()));
            }
            return cb.and(new Predicate[] {cb.equal(root.get("status"), LeaveStatus.Approved), cb.equal(root.get("status"), LeaveStatus.Approved), cb.equal(cb.function("YEAR", Integer.class, root.get("start")), leave.getStart().getYear()), cb.equal(cb.function("MONTH", Integer.class, root.get("start")), leave.getStart().getMonthValue())});
//            return ;
//            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
    
}
