package com.workintech.s17d2.rest;
import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/workintech/developers")
public class DeveloperController {
    private Taxable taxable;
    private Map<Integer, Developer> developers;

    public Map<Integer, Developer> getDevelopers() {
        return developers;
    }

    // Initialize the developers map
    @PostConstruct
    public void init() {
        developers = new HashMap<>();
        this.developers.put(1, new Developer(1, "Ezgi", 50000, Experience.JUNIOR));
    }

    // Constructor with Dependency Injection
    public DeveloperController(Taxable taxable) {
        this.taxable=taxable;
    }

    // Endpoint: Get all developers as a list
    @GetMapping
    public List<Developer> getAllDevelopers(){
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getAllDeveloper(@PathVariable int id){
        /*if(id<0){
            return null;
        }*/
        return this.developers.get(id);
    }

    @PostMapping
    public void addDeveloper(@RequestBody Developer developer){
        double salary = developer.getSalary();
       switch (developer.getExperience()){
           case JUNIOR:
               salary -= salary* taxable.getSimpleTaxRate();
               developer =new JuniorDeveloper(developer.getId(), developer.getName(), salary);
               break;
           case MID:
               salary -= salary* taxable.getMiddleTaxRate();
               developer =new MidDeveloper(developer.getId(), developer.getName(), salary);
               break;
           case SENIOR:
               salary -= salary* taxable.getUpperTaxRate();
               developer =new SeniorDeveloper(developer.getId(), developer.getName(), salary);
               break;
       }
        // Add the developer to the map after modification
        this.developers.put(developer.getId(), developer);
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer newDeveloper){
        developers.replace(id, newDeveloper);
        return newDeveloper;
    }

    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable int id){
        this.developers.remove(id);
    }

}
