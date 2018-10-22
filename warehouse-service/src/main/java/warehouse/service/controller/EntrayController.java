package warehouse.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warehouse.service.model.Entray;
import warehouse.service.model.EntrayRepository;

import java.util.List;

/**
 * Created by lyl on 2017/3/1.
 */

@RestController
@RequestMapping("/entraies")
@Api(value = "入库信息API")
public class EntrayController {

    private EntrayRepository entrayRepository;

    @Autowired
    public EntrayController(EntrayRepository entrayRepository){
        this.entrayRepository=entrayRepository;
    }

    @GetMapping
    @ApiOperation(value = "获取入库信息列表")
    public ResponseEntity<List<Entray>> list(){
        return new ResponseEntity<List<Entray>>(entrayRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping
    @ApiOperation(value="增加入库信息")
    public ResponseEntity<Entray> save(@RequestBody Entray t){
        try{
            Entray Entray=entrayRepository.save(t);
            return new ResponseEntity<Entray>(Entray,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<Entray>((Entray)null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping
    @ApiOperation(value="修改入库信息")
    public ResponseEntity<Entray> update(@RequestBody  Entray t) throws Exception{
        try{
            Entray Entray=entrayRepository.save(t);
            return new ResponseEntity<Entray>(Entray,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<Entray>((Entray)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
    @DeleteMapping("/{id}")
    @ApiOperation(value="删除入库信息")
    public ResponseEntity<Entray> delete(Integer s) throws Exception{
        try{
            Entray Entray=entrayRepository.findOne(s);
            entrayRepository.delete(s);
            return new ResponseEntity<Entray>(Entray,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Entray>((Entray)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
