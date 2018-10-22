package warehouse.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warehouse.service.model.Location;
import warehouse.service.model.LocationRepository;

import java.util.List;

/**
 * Created by lyl on 2017/3/1.
 */

@RestController
@RequestMapping("/locations")
@Api(value = "货位表信息")
public class LocationController {

    private LocationRepository locationRepository;

    @Autowired
    public LocationController(LocationRepository locationRepository){
        this.locationRepository=locationRepository;
    }

    @GetMapping
    @ApiOperation(value = "获取货位列表")
    public ResponseEntity<List<Location>> list(){
        return new ResponseEntity<List<Location>>(locationRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping
    @ApiOperation(value="增加货位")
    public ResponseEntity<Location> save(@RequestBody Location t){
        try{
            Location Location=locationRepository.save(t);
            return new ResponseEntity<Location>(Location,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<Location>((Location)null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping
    @ApiOperation(value="修改货位")
    public ResponseEntity<Location> update(@RequestBody  Location t) throws Exception{
        try{
            Location Location=locationRepository.save(t);
            return new ResponseEntity<Location>(Location,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<Location>((Location)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
    @DeleteMapping("/{id}")
    @ApiOperation(value="删除货位")
    public ResponseEntity<Location> delete(Integer s) throws Exception{
        try{
            Location Location=locationRepository.findOne(s);
            locationRepository.delete(s);
            return new ResponseEntity<Location>(Location,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Location>((Location)null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
