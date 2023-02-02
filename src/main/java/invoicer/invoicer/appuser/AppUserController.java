package invoicer.invoicer.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class AppUserController {

    public ResponseEntity<AppUser> updateInformation(UpdateInformationRequest request){
        return null;
    }
}
