package com.example.server.controller;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.CreateNewUserRequest;
import com.example.server.model.resquest.DeleteByIdRequest;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    /**
     * @description Create new user
     * @endpoint  POST /api/admin/user
     * @param    request CreateNewUserRequest object containing user information
     * @return    Boolean
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/user")
    public SampleResponse<Boolean> createUser(@RequestBody CreateNewUserRequest request) {
        return userService.saveNewUser(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/user")
    public SampleResponse<Boolean> deleteUser(@RequestBody DeleteByIdRequest request){
        return userService.deleteUserById(request);
    }

    /**
     *
     * @param request DeleteUserRequest
     * @return BkavUserDto
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/user-detail")
    public SampleResponse<BkavUserDto> getDetailUser(@RequestBody DeleteByIdRequest request){
        try{
            UUID id = UUID.fromString(request.getId());
            BkavUserDto data = userService.getById(id);
            if(data == null) return new SampleResponse<>(null,"Not found user");
            return new SampleResponse<>(data);
        } catch (Exception e){
            return new SampleResponse<>(null,"Bad request");
        }
    }
}
