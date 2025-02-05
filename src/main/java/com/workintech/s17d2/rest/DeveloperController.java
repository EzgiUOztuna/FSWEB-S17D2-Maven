package com.workintech.s17d2.rest;
import com.workintech.s17d2.dto.DeveloperResponse;
import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/developers")
public class DeveloperController {
    private Taxable taxable;
    public Map<Integer, Developer> developers;

    public Map<Integer, Developer> getDevelopers() {
        return developers;
    }

    // Constructor with Dependency Injection
    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable=taxable;
    }

    // Initialize the developers map
    @PostConstruct
    public void init() {
        this.developers = new HashMap<>();
        this.developers.put(1, new JuniorDeveloper(1, "Ezgi", 50000));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperResponse save(@RequestBody Developer developer){
        Developer createdDeveloper = DeveloperFactory.createdDeveloper(developer, taxable);
        if(Objects.nonNull(createdDeveloper)){
            developers.put(createdDeveloper.getId(), createdDeveloper);
        }
        return new DeveloperResponse(createdDeveloper, HttpStatus.CREATED.value(), "create işlemi başarılı" );
    }


    // Endpoint: Get all developers as a list
    @GetMapping
    public List<Developer> getAllDevelopers(){
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public DeveloperResponse getById(@PathVariable int id){
        Developer foundDeveloper = this.developers.get(id);
        if(foundDeveloper == null){
            return new DeveloperResponse(null, HttpStatus.NOT_FOUND.value(), id + "ile search yapıldığında kayıt bulunamadı.");
        } else {
            return new DeveloperResponse(foundDeveloper, HttpStatus.OK.value(), id + "ile search başarılı.");
        }
    }

    @PutMapping("/{id}")
    public DeveloperResponse updateDeveloper(@PathVariable int id, @RequestBody Developer developer){
        developer.setId(id);
        Developer newDeveloper = DeveloperFactory.createdDeveloper(developer, taxable);
        this.developers.put(id, newDeveloper);
        return new DeveloperResponse(newDeveloper, HttpStatus.OK.value(), "update başarılı");
    }

    @DeleteMapping("/{id}")
    public DeveloperResponse deleteDeveloper(@PathVariable int id){
        Developer removedDeveloper = this.developers.get(id);
        this.developers.remove(id);
        return new DeveloperResponse(removedDeveloper, HttpStatus.NO_CONTENT.value(), "silme işlemi başarılı");
    }

}
