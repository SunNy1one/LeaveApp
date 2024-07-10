package sg.nus.iss.com.Leaveapp.interceptor;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sg.nus.iss.com.Leaveapp.model.Action;
import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.service.EmployeeService;

@Component
public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired
	EmployeeService employeeService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String uri = request.getRequestURI();
		if(uri.equalsIgnoreCase("/") || uri.equalsIgnoreCase("") || uri.equalsIgnoreCase("/login")) {
			return true;
		}
		Employee loggedInEmployee = (Employee) request.getSession().getAttribute("loggedInEmployee");
		if(loggedInEmployee == null) {
			try {
				response.sendRedirect("/");
			} catch(IOException e) {
				System.out.println("IOException: Cannot send redirect to /");
			}
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		Employee loggedInEmployee = (Employee) request.getSession().getAttribute("loggedInEmployee");
		if(loggedInEmployee != null && modelAndView != null) {
			modelAndView.addObject("isLoggedIn", true);
			modelAndView.addObject("employeeName", loggedInEmployee.getName());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
	}

}
