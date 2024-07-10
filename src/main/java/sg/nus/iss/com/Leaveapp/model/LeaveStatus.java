package sg.nus.iss.com.Leaveapp.model;

import java.util.List;

import sg.nus.iss.com.Leaveapp.Exceptions.TypeNotFoundException;

public class LeaveStatus {
	public static final int Applied = 1;
	public static final int Cancelled = 2;
	public static final int Approved = 3;
	public static final int Rejected = 4;
	public static final int Deleted = 5;
	public static final int Updated = 6;
	
	public static int of(String status) throws TypeNotFoundException {
		switch(status) {
		case "Applied":
			return Applied;
		case "Cancelled":
			return Cancelled;
		case "Approved":
			return Approved;
		case "Rejected":
			return Rejected;
		case "Deleted":
			return Deleted;
		case "Updated":
			return Updated;
		default:
			throw new TypeNotFoundException();
		}
	}
	
	public static String asString(int status){
		switch(status) {
		case 1:
			return "Applied";
		case 2:
			return "Cancelled";
		case 3:
			return "Approved";
		case 4:
			return "Rejected";
		case 5:
			return "Deleted";
		case 6:
			return "Updated";
		default:
			return "";
		}
	}
	
	public static final List<Integer> getConsumedStatus() {
		return List.of(new Integer[] {Applied, Approved, Updated});
	}
}
