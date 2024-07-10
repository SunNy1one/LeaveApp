package sg.nus.iss.com.Leaveapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import sg.nus.iss.com.Leaveapp.model.*;
import sg.nus.iss.com.Leaveapp.service.HolidayService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class HolidayController {

	 @Autowired
	 private HolidayService holidayService;

	 @GetMapping("/holidays")
	 public String getHolidays(@RequestParam(name = "year", required = false) Integer selectedYear, Model model) {
	     List<Holiday> holidays;
	     List<Integer> availableYears = Arrays.asList(2022, 2023, 2024); // Predefined list of years
	     
	     if (selectedYear != null) {
	         holidays = holidayService.getHolidaysByYear(selectedYear);
	     } else {
	         holidays = holidayService.getAllHolidays();
	     }
	     holidays.sort((h1, h2) -> h1.getDate().isBefore(h2.getDate()) ? -1 : 1);
	     model.addAttribute("holidays", holidays);
	     model.addAttribute("availableYears", availableYears);
	     model.addAttribute("selectedYear", selectedYear);
	     model.addAttribute("canAdd", true);
	     model.addAttribute("action", "add-holidays");
	     return "index"; // Returns holiday.html template
	 }

	    @GetMapping("/holidays/{year}")
	    public String getHolidaysByYear(@PathVariable int year, Model model) {
	        List<Holiday> holidays = holidayService.getHolidaysByYear(year);
	        model.addAttribute("holidays", holidays);
	        model.addAttribute("canAdd", false);
	        model.addAttribute("action", "view-holidays");
		    return "index";
	    }


	    
	    @PostMapping("/holidays/add")
	    public String addHoliday(@ModelAttribute("holiday") Holiday holiday ) {
	        holidayService.addHoliday(holiday);
	        return "redirect:/admin/holidays";
	    }
 
	    
	    @PostMapping("/holidays/delete/{id}")
	    public String deleteHoliday(@PathVariable("id") Long id) {
	        holidayService.deleteHoliday(id);
	        return "redirect:/admin/holidays"; // Redirect to the holiday list page after deletion
	    }
}