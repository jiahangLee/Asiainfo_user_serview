package dolphin.account.controller;


import dolphin.account.domain.organization.Organization;
import dolphin.account.domain.organization.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by lyl on 16/5/26.
 */
@RestController
@RequestMapping("/organizations")
public class OrganizationController implements BaseController<Organization,Integer>{

    private OrganizationRepository organizationRepository;
    
    @Autowired
    public OrganizationController(OrganizationRepository organizationRepository){
        this.organizationRepository=organizationRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<Organization>> list(){
        return new ResponseEntity(organizationRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Organization> save(@RequestBody Organization organization) throws Exception{
        Organization result=this.organizationRepository.save(organization);
        return Optional.ofNullable(result)
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("save organization error"));
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<Organization> update(@RequestBody Organization organization, @PathVariable("id") Integer id) throws Exception{
        Organization result=this.organizationRepository.save(organization);
        return Optional.ofNullable(result)
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("save organization error"));
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Organization> delete(@PathVariable("id") Integer id) throws Exception{
        Organization o=this.organizationRepository.findOne(id);
        this.organizationRepository.delete(id);
        recursiveDelete(id);
        return new ResponseEntity<Organization>(o, HttpStatus.OK);
    }
    private void recursiveDelete(int id){
        List<Organization> list=this.organizationRepository.findByParentId(id);
        for(Organization org:list){
            recursiveDelete(org.getId());
            this.organizationRepository.delete(org);
        }
    }
}
