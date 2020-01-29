package pl.mdj.rejestrbiurowy.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mdj.rejestrbiurowy.repository.ClientRepository;
import pl.mdj.rejestrbiurowy.service.interfaces.ClientService;

@AllArgsConstructor
@RestController
@RequestMapping("client")
public class ClientController {

    /*
    * Client
    * Location
     */

    ClientService clientService;
}
