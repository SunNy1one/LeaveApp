package sg.nus.iss.com.Leaveapp.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sg.nus.iss.com.Leaveapp.Exceptions.TypeNotFoundException;
import sg.nus.iss.com.Leaveapp.model.Action;
import sg.nus.iss.com.Leaveapp.model.Employee;

@Component
public class ActionsInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
			
		if(modelAndView != null) {
			Employee loggedInEmployee = (Employee) request.getSession().getAttribute("loggedInEmployee");
			if(loggedInEmployee != null && modelAndView != null) {
				try {
					modelAndView.addObject("actions", Action.getActionByRole(loggedInEmployee.getRole().getName()));
				} catch (TypeNotFoundException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
	}

}
