package com.example.server.controller;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.SelectUser;
import com.example.server.model.response.PageView;
import com.example.server.model.resquest.*;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @GetMapping(Constants.ApiEndpoint.ADMIN_USER_PATH)
    public ResponseEntity<PageView<BkavUserDto>> getUserList(@RequestParam String gender,
                                                             @RequestParam String search,
                                                             @RequestParam(defaultValue = Constants.Common.NUMBER_1_STRING) Integer page){

        PageView<BkavUserDto> data = userService.findAllUser(gender,search,page);
//        throw new RuntimeException("Test loi");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * @description Create, update a new user
     * @endpoint  POST /api/admin/user
     * @param    request User information to create
     * @return   Boolean true if successful, otherwise false with error message
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(Constants.ApiEndpoint.ADMIN_USER_PATH)
    public ResponseEntity<?> createOrUpdateUser(@RequestBody BkavUserDto request) {
        return userService.save(request);
    }


    /**
     * @description Reset user password
     * @endpoint  PUT /api/admin/reset-password
     * @param    request View ResetPasswordRequest to see details of the required fields
     * @return   Boolean true if password reset is successful
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(Constants.ApiEndpoint.ADMIN_RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }

    /**
     * @description Delete a user by ID
     * @endpoint  DELETE /api/admin/user
     * @param    id ID of the user to delete
     * @return   Boolean true if deletion is successful
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(Constants.ApiEndpoint.ADMIN_USER_PATH)
    public ResponseEntity<?> deleteUser(@RequestParam UUID id ){
        return userService.deleteById(id);
    }

    /**
     * @description Get detailed user information
     * @endpoint  POST /api/admin/user-detail
     * @param    userId id of user to see detail
     * @param    page page of device list
     * @return   DetailUserResponse containing user details
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(Constants.ApiEndpoint.ADMIN_USER_DETAIL)
    public ResponseEntity<?> getDetailUser(@RequestParam String userId,
                                                            @RequestParam(defaultValue = Constants.Common.NUMBER_1_STRING) Integer page){
        return userService.findDetailUser(userId,page);
    }

    /**
     * @description Get all usernames
     * @endpoint  GET /api/admin/user-name
     * @return   List<?> list of all usernames
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(Constants.ApiEndpoint.ADMIN_USER_SELECT)
    public ResponseEntity<List<SelectUser>> getAllUserSelect(){
        return new ResponseEntity<>(userService.findAllSelectUser(),HttpStatus.OK);
    }
}
