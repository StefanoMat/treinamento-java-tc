package org.company.apicep.controller;

import lombok.AllArgsConstructor;
import org.company.apicep.dto.AddressDTO;
import org.company.apicep.dto.AddressListDTO;
import org.company.apicep.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping()
    public ResponseEntity<AddressListDTO> listAddress(Pageable pageable) {
        AddressListDTO addressListDTO = addressService.listAddress(pageable);
        return new ResponseEntity<>(addressListDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAddress(@PathVariable String id) {
        addressService.deleteAddressById(Long.valueOf(id));
        return new ResponseEntity(null, HttpStatus.OK);
    }
    @DeleteMapping("/soft/{id}")
    public ResponseEntity softDeleteAddress(@PathVariable String id) {
        addressService.softDeleteAddressById(Long.valueOf(id));
        return new ResponseEntity(null, HttpStatus.OK);
    }

}
