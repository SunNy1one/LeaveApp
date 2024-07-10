package sg.nus.iss.com.Leaveapp.model;

public class HalfOfDay {
	public static final int Morning = 1;
	public static final int Afternoon = 2;
	
	public static String asString(int half) {
		switch (half) {
		case Morning:
			return "AM Leave";
		case Afternoon:
			return "PM Leave";
		default:
			return "";
		}
	}
}
