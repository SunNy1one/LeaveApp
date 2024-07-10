package sg.nus.iss.com.Leaveapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sg.nus.iss.com.Leaveapp.io.ContextIO;
import sg.nus.iss.com.Leaveapp.repository.ClaimRepository;
import sg.nus.iss.com.Leaveapp.repository.EmployeeRepository;
import sg.nus.iss.com.Leaveapp.repository.HolidayRepository;
import sg.nus.iss.com.Leaveapp.repository.LeaveEntitlementRepository;
import sg.nus.iss.com.Leaveapp.repository.LeaveRepository;
import sg.nus.iss.com.Leaveapp.repository.RoleRepository;


@SpringBootApplication
public class LeaveapplicationApplication {

	@Value("${spring.jpa.hibernate.ddl-auto}") // Injecting the value of app.greeting from application.properties
    private String ddlauto;
	
	public static void main(String[] args) {
		SpringApplication.run(LeaveapplicationApplication.class, args);
	}

	@Bean
	CommandLineRunner loadContext(HolidayRepository hr, ClaimRepository cr, LeaveRepository lr, EmployeeRepository er, RoleRepository rr, LeaveEntitlementRepository ler) {
		return args -> {
			if(ddlauto.compareTo("create") == 0) {
				String path = "C:\\Users\\user\\init-kopico\\Library\\java-spring-workspace\\LeaveAppSystem";
				String employeeCsv = "employee_dummy.csv";
				ContextIO empIO = new ContextIO(path+ "\\" + employeeCsv);
				empIO.LoadRoles(rr);
				empIO.LoadCsv(er,rr);
				empIO.AssignManagers(er);
				empIO.LoadLeaveEntitlement(ler);
				String leaveCsv = "leave_dummy.csv";
				ContextIO leaveIO = new ContextIO(path + "\\" + leaveCsv);
				leaveIO.LoadLeaves(lr,er,ler,rr);
				String claimCsv = "claim_dummy.csv";
				ContextIO claimIO = new ContextIO(path + "\\" + claimCsv);
				claimIO.LoadClaims(cr,er);
				String holidayCsv = "holiday.csv";
				ContextIO holidayIO = new ContextIO(path + "\\" + holidayCsv);
				holidayIO.LoadHolidays(hr);
				return ;
			}
			System.out.println("Skipped context load. ddl-auto: " + ddlauto);
		};
	}
}
