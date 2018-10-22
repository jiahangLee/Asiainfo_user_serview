package warehouse.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warehouse.service.model.Airmaterial;
import warehouse.service.model.AirmaterialRepository;

import java.util.List;

/**
 * Created by lyl on 2017/3/1.
 */

@RestController
@RequestMapping("/airMaterials")
@Api(value = "航材服务")
public class AirmaterialController {

    private AirmaterialRepository airmaterialRepository;

    @Autowired
    public AirmaterialController(AirmaterialRepository airmaterialRepository){
        this.airmaterialRepository=airmaterialRepository;
    }

    @GetMapping
    @ApiOperation(value = "获取航材列表")
    public ResponseEntity<List<Airmaterial>> list(){
        return new ResponseEntity<List<Airmaterial>>(airmaterialRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping
    @ApiOperation(value="增加航材")
    public ResponseEntity<Airmaterial> save(@RequestBody Airmaterial t){
        try{
            Airmaterial airmaterial=airmaterialRepository.save(t);
            return new ResponseEntity<Airmaterial>(airmaterial,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<Airmaterial>((Airmaterial)null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping
    @ApiOperation(value="修改航材")
    public ResponseEntity<Airmaterial> update(@RequestBody  Airmaterial t) throws Exception{
        try{
            Airmaterial airmaterial=airmaterialRepository.save(t);
            return new ResponseEntity<Airmaterial>(airmaterial,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<Airmaterial>((Airmaterial)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
    @DeleteMapping("/{id}")
    @ApiOperation(value="删除航材")
    public ResponseEntity<Airmaterial> delete(Integer s) throws Exception{
        try{
            Airmaterial airmaterial=airmaterialRepository.findOne(s);
            airmaterialRepository.delete(s);
            return new ResponseEntity<Airmaterial>(airmaterial,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Airmaterial>((Airmaterial)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
