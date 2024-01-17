package xin.admin.controller.other;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.other.SysPosType;
import xin.admin.service.other.SysPosTypeService;

import java.util.List;

@RestController
public class SysPosTypeController {

    @Autowired
    SysPosTypeService sysPosTypeService;

    @PostMapping("/admin/getAllPosType")
    public ResponseResult<List<SysPosType>> adminGetAllPosType() {
        return sysPosTypeService.getAllPosType();
    }

    @PostMapping("/h5/getAllPosType")
    public ResponseResult<List<SysPosType>> userGetAllPosType() {
        return sysPosTypeService.getAllPosType();
    }
}
