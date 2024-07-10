package sg.nus.iss.com.Leaveapp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum LeaveStatusEnum {
   
    APPLIED(1, "Applied"),
  
    CANCELLED(2, "Cancelled"),
  
    APPROVED(3, "Approved"),
  
    REJECTED(4, "Rejected"),
 
    DELETED(5, "Deleted"),
  
    UPDATED(6, "Deleted");

    private static final Map<Integer, LeaveStatusEnum> map = new HashMap<>();

    static {
        for (LeaveStatusEnum item : LeaveStatusEnum.values()) {
            map.put(item.getCode(), item);
        }
    }

    private Integer code;
    @JsonValue
    private String msg;

    @JsonCreator
    private static LeaveStatusEnum jacksonInstance(final JsonNode jsonNode) {
        Integer code = Integer.valueOf(jsonNode.asText());
        return map.get(code);
    }

    public static LeaveStatusEnum getByCode(Integer code) {
        return map.getOrDefault(code, LeaveStatusEnum.APPLIED);
    }

	public Integer getCode() {
		return code;
	}
    
    
}
