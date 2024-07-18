package org.company.apicep.callable;

import lombok.AllArgsConstructor;
import org.company.apicep.model.Address;
import org.company.apicep.service.AddressService;
import org.company.apicep.service.StepAddressService;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class AddressCallable implements Callable<Address> {
    private final Address address;
    private final StepAddressService stepAddressService;

    @Override
    public Address call() throws Exception {
        return stepAddressService.createAddress(this.address);
    }
}
