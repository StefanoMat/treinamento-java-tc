package org.company.apicep.service;

import lombok.AllArgsConstructor;
import org.company.apicep.model.Address;
import org.company.apicep.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class StepAddressService {
    private AddressRepository addressRepository;
    private static final Logger log = LoggerFactory.getLogger(StepAddressService.class);

    public Address createAddress(Address address) throws Exception {
        Address responseEntity = addressRepository.save(address);
        log.info("address processed: {}", responseEntity.getId());
        Thread.sleep(500); // Simula uma operação demorada

        return responseEntity;
    }
}
