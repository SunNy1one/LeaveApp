package sg.nus.iss.com.Leaveapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import sg.nus.iss.com.Leaveapp.model.LeaveEntitlement;
import sg.nus.iss.com.Leaveapp.model.Role;

@SpringBootTest
public class LeaveEntitlementTest {

    @Test
    public void testSetAndGetAnnualLeave() {
        LeaveEntitlement leaveEntitlement = new LeaveEntitlement();
        leaveEntitlement.setAnnualLeave(10);
        assertEquals(10, leaveEntitlement.getAnnualLeave());
    }

    @Test
    public void testSetAndGetSickLeave() {
        LeaveEntitlement leaveEntitlement = new LeaveEntitlement();
        leaveEntitlement.setSickLeave(5);
        assertEquals(5, leaveEntitlement.getSickLeave());
    }

//    @Test
//    public void testSetAndGetCompensationLeave() {
//        LeaveEntitlement leaveEntitlement = new LeaveEntitlement();
//        leaveEntitlement.setCompensationLeave(2);
//        assertEquals(2, leaveEntitlement.getCompensationLeave());
//    }

    @Test
    public void testSetAndGetYear() {
        LeaveEntitlement leaveEntitlement = new LeaveEntitlement();
        leaveEntitlement.setYear(2022);
        assertEquals(2022, leaveEntitlement.getYear());
    }

    @Test
    public void testSetAndGetLeaveType() {
        LeaveEntitlement leaveEntitlement = new LeaveEntitlement();
        leaveEntitlement.setLeaveType("annual");
        assertEquals("annual", leaveEntitlement.getLeaveType());
    }

    @Test
    public void testSetAndGetRole() {
        Role role = new Role();
        LeaveEntitlement leaveEntitlement = new LeaveEntitlement();
        leaveEntitlement.setRole(role);
        assertEquals(role, leaveEntitlement.getRole());
    }

    @Test
    public void testSetAndGetNumberOfDays() {
        LeaveEntitlement leaveEntitlement = new LeaveEntitlement();
        leaveEntitlement.setNumberOfDays(20);
        assertEquals(20, leaveEntitlement.getNumberOfDays());
    }
}
