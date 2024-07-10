package com.example.holidaycalendar.controller;





import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.holidaycalendar.model.Holiday;
import com.example.holidaycalendar.service.HolidayService;

import java.util.Arrays;
import java.util.List;

@Controller
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
	     
	     model.addAttribute("holidays", holidays);
	     model.addAttribute("availableYears", availableYears);
	     model.addAttribute("selectedYear", selectedYear);
	     model.addAttribute("canAdd", true);
	     return "holiday"; // Returns holiday.html template
	 }

	    @GetMapping("/holidays/{year}")
	    public String getHolidaysByYear(@PathVariable int year, Model model) {
	        List<Holiday> holidays = holidayService.getHolidaysByYear(year);
	        model.addAttribute("holidays", holidays);
	        model.addAttribute("canAdd", false);
	        return "holiday";
	    }


	    
	    @PostMapping("/holidays/add")
	    public String addHoliday(@ModelAttribute("holiday") Holiday holiday ) {
	        holidayService.addHoliday(holiday);
	        return "redirect:/holidays";
	    }
   
	    
	    @PostMapping("/holidays/delete/{id}")
	    public String deleteHoliday(@PathVariable("id") Long id) {
	        holidayService.deleteHoliday(id);
	        return "redirect:/holidays"; // Redirect to the holiday list page after deletion
	    }
}
