package springSecuriry.controlles;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prodetec/")
public class ProdetecControllers {

    @PostMapping("bien")
    public String welcome() {

        return "hola estas en la protejida";

    }

}
