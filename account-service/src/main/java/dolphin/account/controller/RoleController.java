package dolphin.account.controller;


import dolphin.account.domain.role.Role;
import dolphin.account.domain.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by lyl on 16/5/25.
 */
@RestController
@RequestMapping("/roles")
public class RoleController implements BaseController<Role,Integer>{

    private RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository){
        this.roleRepository=roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Role>> list(){
        return new ResponseEntity(roleRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Role> save(@RequestBody Role role) throws Exception{
        Role result=this.roleRepository.save(role);
        return Optional.ofNullable(result)
                .map(a -> new ResponseEntity<Role>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("save role error"));
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<Role> update(@RequestBody Role role, @PathVariable("id") Integer id) throws Exception{
        Role result=this.roleRepository.save(role);
        return Optional.ofNullable(result)
                .map(a -> new ResponseEntity<Role>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("save role error"));
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Role> delete(@PathVariable("id") Integer id) throws Exception{
        Role r=this.roleRepository.findOne(id);
        this.roleRepository.delete(id);

        return new ResponseEntity<Role>(r, HttpStatus.OK);
    }

}
