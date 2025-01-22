package com.example.server.controller;

import com.example.server.config.security.UserPrincipal;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.response.DetailUserResponse;
import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.*;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.ApiEndpoint.COMMON_BASE_PATH)
public class UserController {
    @Autowired
    UserService userService;

    /**
     * @description Get all user
     * @endpoint  GET /api/admin/user
     * @param    gender Gender to filter (default "")
     * @param    search Name to search by name (default "")
     * @param    page Page number (default 1)
     * @return    PageResponse<BkavUserDto>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user")
    public SampleResponse<PageResponse<BkavUserDto>> getUserList(@RequestParam(required = false, defaultValue = "") String gender,
                                                                 @RequestParam(required = false, defaultValue = "") String search,
                                                                 @RequestParam(required = false, defaultValue = "1") Integer page){

        PageResponse<BkavUserDto> data = userService.getAllUser(gender,search,page);
        return new SampleResponse<>(data);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/user")
    public SampleResponse<Boolean> createUser(@RequestBody BkavUserDto request) {
        try{
            userService.save(request);
            return new SampleResponse<>(true);
        } catch (Exception e){
            return new SampleResponse<>(false, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/user")
    public SampleResponse<Boolean> updateUser(@RequestBody BkavUserDto request) {
        try{
            userService.save(request);
            return new SampleResponse<>(true);
        } catch (Exception e){
            return new SampleResponse<>(false, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/reset-password")
    public SampleResponse<Boolean> resetPassword(@RequestBody ResetPasswordRequest request) throws Exception {
        return userService.resetPassword(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/user")
    public SampleResponse<Boolean> deleteUser(@RequestBody ActionByIdRequest request){
        return userService.deleteUserById(request);
    }

    /**
     *
     * @param request DeleteUserRequest
     * @return BkavUserDto
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/user-detail")
    public SampleResponse<DetailUserResponse> getDetailUser(@RequestBody DetailUserRequest request){
        return userService.getDetailUser(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user-name")
    public SampleResponse<List<String>> getAllUsername(){
        return userService.getAllUsername();
    }
}
