package org.company.apicep.controller;

import lombok.AllArgsConstructor;
import org.company.apicep.dto.AddressDTO;
import org.company.apicep.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private AddressService addressService;

    @GetMapping("/cep/{cep}")
    public ResponseEntity<AddressDTO> getAddress(@PathVariable String cep) {
        AddressDTO addressDTO = addressService.getAddressByCEP(cep);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }
}
