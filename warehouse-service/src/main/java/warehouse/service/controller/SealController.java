package warehouse.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warehouse.service.model.Seal;
import warehouse.service.model.SealRepository;

import java.util.List;

/**
 * Created by lyl on 2017/3/1.
 */

@RestController
@RequestMapping("/seals")
@Api(value = "油封表信息")
public class SealController {

    private SealRepository sealRepository;

    @Autowired
    public SealController(SealRepository sealRepository){
        this.sealRepository=sealRepository;
    }

    @GetMapping
    @ApiOperation(value = "获取油封列表")
    public ResponseEntity<List<Seal>> list(){
        return new ResponseEntity<List<Seal>>(sealRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping
    @ApiOperation(value="增加油封")
    public ResponseEntity<Seal> save(@RequestBody Seal t){
        try{
            Seal Seal=sealRepository.save(t);
            return new ResponseEntity<Seal>(Seal,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<Seal>((Seal)null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping
    @ApiOperation(value="修改油封")
    public ResponseEntity<Seal> update(@RequestBody  Seal t) throws Exception{
        try{
            Seal Seal=sealRepository.save(t);
            return new ResponseEntity<Seal>(Seal,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<Seal>((Seal)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
    @DeleteMapping("/{id}")
    @ApiOperation(value="删除油封")
    public ResponseEntity<Seal> delete(Integer s) throws Exception{
        try{
            Seal Seal=sealRepository.findOne(s);
            sealRepository.delete(s);
            return new ResponseEntity<Seal>(Seal,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Seal>((Seal)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
