package sg.nus.iss.com.Leaveapp.controller;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.com.Leaveapp.model.Result;
import sg.nus.iss.com.Leaveapp.model.Leave;
import sg.nus.iss.com.Leaveapp.model.LeaveVo;
import sg.nus.iss.com.Leaveapp.service.LeaveService;

import java.util.List;

@RestController
@RequestMapping("/api/leave")
public class RestLeaveController {
    @Resource
    private LeaveService leaveService;

    
    //  Add and update leave   
    //  @param leave 
    //  @return result
    
    @PostMapping
    public Result<Void> save(@RequestBody Leave leave) {
        leaveService.saveOrUpdate(leave);
        return Result.success();
    }
    // delete leave
    // @param ids ID list
    // @return result r
    @DeleteMapping("/{ids}")
    public Result<Void> removeBatchByIds(@PathVariable List<Integer> ids) {
        leaveService.removeBatchByIds(ids);
        return Result.success();
    }

    
    // search list of leave
     
    // @param leave 
    // @return 
     
    @GetMapping("/list")
    public Result<List<LeaveVo>> getList(Leave leave) {
        List<LeaveVo> list = leaveService.getList(leave);
        return Result.success(list);
    }

    
    // search page of leave
    
    // @param leave 
    // @return 
    
    @GetMapping("/page")
    public Result<Page<Leave>> getPage(Leave leave) {
        Page<Leave> page = leaveService.getPage(leave);
        return Result.success(page);
    }

    
    // search one leave
    
    // @param leave 
    // @return 
    
    @GetMapping
    public Result<Leave> getOne(Leave leave) {
        leave = leaveService.getOne(leave);
        return Result.success(leave);
    }
}
