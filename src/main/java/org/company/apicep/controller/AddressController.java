package org.company.apicep.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.apicep.dto.AddressDTO;
import org.company.apicep.dto.AddressListDTO;
import org.company.apicep.dto.CreateAddressDTO;
import org.company.apicep.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @PostMapping("/cep/bulk")
    public ResponseEntity<List<AddressDTO>> createAddressBulk(@RequestBody List<AddressDTO> addressDTOS) {
        var response = addressService.createBulkAddress(addressDTOS);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/cep/async/bulk")
    public ResponseEntity<String> createAddressAsyncBulk(@RequestBody List<AddressDTO> addressDTOS) {
        addressService.createAsyncBulkAddress(addressDTOS);
        log.info("creating in progress.......");
        return new ResponseEntity<>("Creating in progress.", HttpStatus.ACCEPTED);
    }

    @PostMapping("/cep")
    public ResponseEntity<List<AddressDTO>> createAddress(@RequestBody List<AddressDTO> addressDTOS) {
        var response = addressService.createAddresses(addressDTOS);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
