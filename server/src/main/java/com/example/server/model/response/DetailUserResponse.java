package com.example.server.model.response;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.view.DeviceInfoView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailUserResponse {
    private BkavUserDto userDetail;
    private PageView<DeviceInfoView> devices;
}
