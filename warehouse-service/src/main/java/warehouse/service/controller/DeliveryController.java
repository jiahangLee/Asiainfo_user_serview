package warehouse.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warehouse.service.model.Delivery;
import warehouse.service.model.DeliveryRepository;


import java.util.List;

/**
 * Created by lyl on 2017/3/1.
 */

@RestController
@RequestMapping("/deliveries")
@Api(value = "出库信息API")
public class DeliveryController {

    private DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryController(DeliveryRepository deliveryRepository){
        this.deliveryRepository=deliveryRepository;
    }

    @GetMapping
    @ApiOperation(value = "获取出库信息列表")
    public ResponseEntity<List<Delivery>> list(){
        return new ResponseEntity<List<Delivery>>(deliveryRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping
    @ApiOperation(value="增加出库信息")
    public ResponseEntity<Delivery> save(@RequestBody Delivery t){
        try{
            Delivery Delivery=deliveryRepository.save(t);
            return new ResponseEntity<Delivery>(Delivery,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<Delivery>((Delivery)null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping
    @ApiOperation(value="修改出库信息")
    public ResponseEntity<Delivery> update(@RequestBody  Delivery t) throws Exception{
        try{
            Delivery Delivery=deliveryRepository.save(t);
            return new ResponseEntity<Delivery>(Delivery,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<Delivery>((Delivery)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
    @DeleteMapping("/{id}")
    @ApiOperation(value="删除出库信息")
    public ResponseEntity<Delivery> delete(Integer s) throws Exception{
        try{
            Delivery Delivery=deliveryRepository.findOne(s);
            deliveryRepository.delete(s);
            return new ResponseEntity<Delivery>(Delivery,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Delivery>((Delivery)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
