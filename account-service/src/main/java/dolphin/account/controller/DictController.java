package dolphin.account.controller;


import dolphin.account.domain.dict.Dict;
import dolphin.account.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyl on 16/5/30.
 */

@RestController
@RequestMapping("/dicts")
public class DictController {

    private static Logger logger= LoggerFactory.getLogger(DictController.class);

    @Autowired
    private DictService dictService;

    @RequestMapping(path = "/initFromExcel")
    public void initFromExcel() throws Exception{
        dictService.initFromFile();
    }


    @GetMapping
    public ResponseEntity<List<Dict>> list(){
        logger.debug("list ");
        return new ResponseEntity(dictService.getDicts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Dict> save(@RequestBody Dict dict){
        logger.debug("save dict {}", dict);
        Dict result=this.dictService.saveDict(dict, true);

        logger.debug("save result dict {}", dict);
        return new ResponseEntity<Dict>(result, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Dict> update(@RequestBody Dict dict, @PathVariable("id") Integer id){
        logger.debug("save dict {}", dict);
        Dict result=this.dictService.saveDict(dict, true);

        logger.debug("save result dict {}", dict);
        return new ResponseEntity<Dict>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Dict> delete(@PathVariable("id") int id){
        logger.debug("delete dict {}", id);
        Dict result=this.dictService.deleteDict(id);
        logger.debug("delete end dict {}", result);
        return new ResponseEntity<Dict>(result, HttpStatus.OK);
    }


    @GetMapping(path="/{groupName}")
    //@PreAuthorize("hasAuthority('DICT')")
    public ResponseEntity<List<Dict>> list(@PathVariable("groupName") String groupName) throws Exception{
        List<Dict> list=dictService.getDicts(groupName);
        if(list==null){
            list=new ArrayList<>();
        }
        logger.debug("list groupName {}, resultSize{}", groupName, list);
        return new ResponseEntity<List<Dict>>(list, HttpStatus.OK);

    }
}
