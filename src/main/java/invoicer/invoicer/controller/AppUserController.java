package invoicer.invoicer.controller;

import invoicer.invoicer.exception.AppUserNotFoundException;
import invoicer.invoicer.model.AppUser;
import invoicer.invoicer.request.RegistrationRequest;
import invoicer.invoicer.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;


    @GetMapping("all")
    public List<AppUser> getAllUsers(){
        return appUserService.getUsers();
    }

    @GetMapping("{id}")
    public AppUser getUserById(@PathVariable("id") Long id){
        AppUser appUser = appUserService.findById(id)
                .orElseThrow(()->new AppUserNotFoundException("Student with "+id+" is Not Found!"));
        return appUser;
    }

    @PutMapping("{id}")
    public AppUser updateUserInformation(@PathVariable("id") Long id, @RequestBody RegistrationRequest request){
        AppUser appUser = appUserService.findById(id)
                .orElseThrow(()->new AppUserNotFoundException("Student with "+id+" is Not Found!"));

        appUser.setEmail(request.getEmail());
        appUser.setPassword(request.getPassword());
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setCity(request.getCity());
        appUser.setStreet(request.getStreet());
        appUser.setPostalCode(request.getPostalCode());
        appUser.setPhoneNumber(request.getPhoneNumber());

        return appUserService.save(appUser);
    }

    @DeleteMapping("{id}")
    public String deleteUserById(@PathVariable("id") Long id){
        AppUser appUser = appUserService.findById(id)
                .orElseThrow(()->new AppUserNotFoundException("Student with "+id+" is Not Found!"));
        appUserService.deleteById(appUser.getId());
        return "User with ID: "+id+" deleted";
    }
    }

