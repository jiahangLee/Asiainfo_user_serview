package dolphin.account.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import dolphin.account.domain.permission.Permission;
import dolphin.account.domain.permission.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by lyl on 16/5/25.
 */

@RestController
@RequestMapping("/permissions")
public class PermissionController {


    private PermissionRepository permissionRepository;

    @Autowired
    public PermissionController(PermissionRepository permissionRepository) {

        this.permissionRepository = permissionRepository;
    }

    @RequestMapping(path = "/initFromFile")
    public ResponseEntity initFromFile() throws Exception {

        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource resource=resourceResolver.getResource("/init-permission.json");

        String json = Files.toString(resource.getFile(), Charset.forName("UTF-8"));
        Map map = JSON.parseObject(json, Map.class);

        fillIds(null, map );

        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @PostMapping(path = "/init")
    public ResponseEntity init(@RequestBody String json) throws Exception {

        Map map = JSON.parseObject(json, Map.class);

        fillIds(null, map );

        return new ResponseEntity("ok", HttpStatus.OK);
    }



    private void fillIds(Permission parent, Map map) {
        Permission permission=new Permission();
        permission.setName((String)map.get("name"));
        permission.setLabel((String)map.get("label"));
       if(parent!=null){
           permission.setParentId(parent.getId());
           permission.setUniqueId(parent.getUniqueId()+":"+permission.getName());
       }else{
           permission.setUniqueId(permission.getName());
       }
        permission = this.permissionRepository.save(permission);

        List<Map> childs=(List)map.get("children");
        if(childs!=null){
            for(Map child:childs){
                fillIds(permission, child);
            }
        }

    }


    @GetMapping
    public ResponseEntity<List<Permission>> findAll() {

        return new ResponseEntity<List<Permission>>(this.permissionRepository.findAll(), HttpStatus.OK);

    }


    @PostMapping
    public ResponseEntity<Permission> save(@RequestBody Permission permission){
        int parentId=permission.getParentId();
        if(parentId!=0){
            Permission parent=this.permissionRepository.findOne(parentId);
            permission.setUniqueId(parent.getUniqueId()+":"+permission.getName());
        }else{
            permission.setUniqueId(permission.getName());
        }

        Permission result=this.permissionRepository.save(permission);
        return new ResponseEntity<Permission>(result, HttpStatus.OK);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<Permission> update(@RequestBody Permission permission, @PathVariable("id") Integer id){
        int parentId=permission.getParentId();
        if(parentId!=0){
            Permission parent=this.permissionRepository.findOne(parentId);
            permission.setUniqueId(parent.getUniqueId()+":"+permission.getName());
        }else{
            permission.setUniqueId(permission.getName());
        }

        Permission result=this.permissionRepository.save(permission);
        return new ResponseEntity<Permission>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Permission> delete(@PathVariable("id") Integer id){
        Permission permission=this.permissionRepository.findOne(id);
        this.permissionRepository.delete(id);
        return new ResponseEntity<Permission>(permission, HttpStatus.OK);
    }

    private void recursiveDelete(int id){
        List<Permission> list=this.permissionRepository.findByParentId(id);
        for(Permission permission:list){
            recursiveDelete(permission.getId());
            this.permissionRepository.delete(permission);
        }
    }

}
