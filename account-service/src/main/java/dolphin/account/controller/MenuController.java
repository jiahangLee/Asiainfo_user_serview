package dolphin.account.controller;



import dolphin.account.domain.menu.Menu;
import dolphin.account.domain.menu.MenuRepository;
import dolphin.account.domain.permission.Permission;
import dolphin.account.domain.permission.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by lyl on 16/6/8.
 */
@RestController
@RequestMapping("/menus")
public class MenuController implements BaseController<Menu, Integer>{
    private static Logger logger= LoggerFactory.getLogger(MenuController.class);
    private MenuRepository menuRepository;
    private PermissionRepository permissionRepository;
    @Autowired
    public MenuController(MenuRepository menuRepository, PermissionRepository permissionRepository){
        this.menuRepository=menuRepository;
        this.permissionRepository=permissionRepository;
    }
    @Override
    @GetMapping
    public ResponseEntity<List<Menu>> list() {
        return new ResponseEntity(menuRepository.findAll(), HttpStatus.OK);
    }

    @Override
    @PostMapping
    public ResponseEntity<Menu> save(@RequestBody Menu menu) throws Exception {
        logger.debug("menu={}", menu);
        ensureMenuPermission(menu);
        Menu result=this.menuRepository.save(menu);



        return Optional.ofNullable(result)
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("save menu error"));
    }

    private void findMenuPermissionUniqueId(Menu menu, LinkedList<String> names){
        int pid=menu.getParentId();
        names.addFirst(menu.getName());
        if(pid!=0){
            Menu parent=this.menuRepository.findOne(pid);
            findMenuPermissionUniqueId(parent, names);
        }
    }

    private void  ensureMenuPermission(Menu menu){
        LinkedList<String> names= new LinkedList<>();
        findMenuPermissionUniqueId(menu,names);
        String permissionUid="";
        int pid=menu.getParentId();
        if(pid!=0){
            StringBuffer parentUniqueId=new StringBuffer();
            for(int i=0;i<names.size()-1;i++){
                parentUniqueId.append(names.get(i)).append(":");
            }
            parentUniqueId.deleteCharAt(parentUniqueId.length()-1);

            Permission parentPermission=this.permissionRepository.findByUniqueId(parentUniqueId.toString());
            permissionUid=parentUniqueId.append(":").append(menu.getName()).toString();
            Permission permission=this.permissionRepository.findByUniqueId(permissionUid);
            if(permission==null){
                permission=new Permission();
                permission.setName(menu.getName());
                permission.setLabel(menu.getLabel());
                permission.setUniqueId(permissionUid);
                permission.setParentId(parentPermission.getId());
                this.permissionRepository.save(permission);
            }
        }else{
            permissionUid=menu.getName();
            Permission permission=this.permissionRepository.findByUniqueId(permissionUid);
            if(permission==null){
                permission=new Permission();
                permission.setName(menu.getName());
                permission.setLabel(menu.getLabel());
                permission.setUniqueId(permissionUid);
                this.permissionRepository.save(permission);
            }
        }
        menu.setPermissionId(permissionUid);


    }


    @Override
    @PutMapping(path = "/{id}")
    public ResponseEntity<Menu> update(@RequestBody Menu menu, @PathVariable("id") Integer id) throws Exception {

        ensureMenuPermission(menu);
        Menu result=this.menuRepository.save(menu);
        return Optional.ofNullable(result)
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("save menu error"));
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Menu> delete(@PathVariable("id") Integer id) throws Exception {
        Menu m=this.menuRepository.findOne(id);
        this.menuRepository.delete(id);
        ensureDeleteMenuPermission(m);
        return new ResponseEntity<Menu>(m, HttpStatus.OK);
    }


    private void    ensureDeleteMenuPermission(Menu menu){
        LinkedList<String> names= new LinkedList<>();
        findMenuPermissionUniqueId(menu,names);

        int pid=menu.getParentId();
        String uniqueId="";
        if(pid!=0){
            StringBuffer parentUniqueId=new StringBuffer();
            for(int i=0;i<names.size()-1;i++){
                parentUniqueId.append(names.get(i)).append(":");
            }
            parentUniqueId.deleteCharAt(parentUniqueId.length()-1);

            uniqueId=parentUniqueId.append(":").append(menu.getName()).toString();

        }else{
            uniqueId=menu.getName();
        }

        Permission permission=this.permissionRepository.findByUniqueId(uniqueId);
        if(permission!=null){
            this.permissionRepository.delete(permission);
            recursiveDelete(permission.getId());
        }
    }

    private void recursiveDelete(int id){
        List<Permission> list=this.permissionRepository.findByParentId(id);
        for(Permission permission:list){
            recursiveDelete(permission.getId());
            this.permissionRepository.delete(permission);
        }
    }


}
