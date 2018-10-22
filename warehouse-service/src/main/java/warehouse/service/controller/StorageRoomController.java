package warehouse.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warehouse.service.model.StorageRoom;
import warehouse.service.model.StorageRoomRepository;

import java.util.List;

/**
 * Created by lyl on 2017/3/1.
 */

@RestController
@RequestMapping("/storageRooms")
@Api(value = "库房表信息")
public class StorageRoomController {

    private StorageRoomRepository storageRoomRepository;

    @Autowired
    public StorageRoomController(StorageRoomRepository storageRoomRepository){
        this.storageRoomRepository=storageRoomRepository;
    }

    @GetMapping
    @ApiOperation(value = "获取库房列表")
    public ResponseEntity<List<StorageRoom>> list(){
        return new ResponseEntity<List<StorageRoom>>(storageRoomRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping
    @ApiOperation(value="增加库房")
    public ResponseEntity<StorageRoom> save(@RequestBody StorageRoom t){
        try{
            StorageRoom StorageRoom=storageRoomRepository.save(t);
            return new ResponseEntity<StorageRoom>(StorageRoom,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<StorageRoom>((StorageRoom)null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping
    @ApiOperation(value="修改库房")
    public ResponseEntity<StorageRoom> update(@RequestBody  StorageRoom t) throws Exception{
        try{
            StorageRoom StorageRoom=storageRoomRepository.save(t);
            return new ResponseEntity<StorageRoom>(StorageRoom,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<StorageRoom>((StorageRoom)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
    @DeleteMapping("/{id}")
    @ApiOperation(value="删除库房")
    public ResponseEntity<StorageRoom> delete(Integer s) throws Exception{
        try{
            StorageRoom StorageRoom=storageRoomRepository.findOne(s);
            storageRoomRepository.delete(s);
            return new ResponseEntity<StorageRoom>(StorageRoom,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<StorageRoom>((StorageRoom)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
