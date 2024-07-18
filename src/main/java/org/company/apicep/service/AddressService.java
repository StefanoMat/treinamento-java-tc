package org.company.apicep.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.apicep.dto.AddressDTO;
import org.company.apicep.dto.AddressListDTO;
import org.company.apicep.dto.CreateAddressDTO;
import org.company.apicep.model.Address;
import org.company.apicep.orchestrator.AddressOrchestrator;
import org.company.apicep.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AddressService {
    private RestTemplate restTemplate;
    private AddressRepository addressRepository;
    private AddressOrchestrator addressOrchestrator;

    public AddressDTO getAddressByCEP(String cep) {
        //Se existir o cep, nao precisa salvar.
        Optional<Address> optionalAddress = addressRepository.findByCep(cep);
        if(optionalAddress.isEmpty())  {
            //chamar o servico VIACEP
            //viacep.com.br/ws/01001000/json/
            String url = UriComponentsBuilder.fromHttpUrl("https://viacep.com.br/ws/{cep}/json")
                    .buildAndExpand(cep)
                    .toUriString();

            AddressDTO addressDTO = restTemplate.getForObject(url, AddressDTO.class);
            //Salvar o address vindo do ViaCep na tabela address.
            addressRepository.save(mapEntity(addressDTO));
            return addressDTO;
        } else {
            return mapDto(optionalAddress.get());
        }
    }

    public Address createAddress(Address address) throws Exception {
        Address responseEntity = addressRepository.save(address);
        return responseEntity;
    }

    public List<AddressDTO> createAddresses(List<AddressDTO> addressDTOS) {
        var listEntity = addressDTOS.stream().map(a -> mapEntity(a)).collect(Collectors.toList());
        List<Address> addressResponse = new ArrayList<>();
        for (Address a:listEntity) {
            var saved = addressRepository.save(a);
            addressResponse.add(saved);
        }
        return addressResponse.stream().map(address -> mapDto(address)).collect(Collectors.toList());
    }

    public List<AddressDTO> createBulkAddress(List<AddressDTO> addressDTOS) {
        log.info("Starting createBulkAddress");
        var listEntity = addressDTOS.stream().map(a -> mapEntity(a)).collect(Collectors.toList());
        var response = addressOrchestrator.createAddresses(listEntity);
        log.info("finish bulkAddress");
        return response.stream().map(address -> mapDto(address)).collect(Collectors.toList());
    }

    public void createAsyncBulkAddress(List<AddressDTO> addressDTOS) {
        try {
            var listEntity = addressDTOS.stream().map(a -> mapEntity(a)).collect(Collectors.toList());
            addressOrchestrator.createAddressesAsync(listEntity);
        } catch ( Exception e) {
            log.error("error creating product", e);
        }

    }

    public void deleteAddressById(Long id) {
        //Código para deletar
        Optional<Address> address = addressRepository.findById(id);
        if(address.isEmpty()) {
            //throw
        }
        addressRepository.delete(address.get());
    }

    public void softDeleteAddressById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        Address addressObj = address.get();

        if(address.isEmpty() || addressObj.isDeleted()) {
            //throw
        }
        addressObj.setDeleted(true);
        addressObj.setLocalidade(addressObj.getLocalidade() + " -- deletado");
        addressRepository.save(addressObj);
    }

    public AddressListDTO listAddress(Pageable pageable) {
        Page<Address> page = addressRepository.findAll(pageable);
        List<AddressDTO> listAddress = page.map(a -> mapDto(a)).toList();

        return AddressListDTO.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(listAddress)
                .build();
    }

    private Address mapEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setCep(addressDTO.getCep());
        address.setLogradouro(addressDTO.getLogradouro());
        address.setComplemento(address.getComplemento());
        address.setBairro(addressDTO.getBairro());
        address.setUf(addressDTO.getUf());
        address.setLocalidade(addressDTO.getLocalidade());
        address.setDdd(Integer.parseInt(addressDTO.getDdd()));
        return address;
    }

    private AddressDTO mapDto(Address address) {
        return AddressDTO.builder().cep(address.getCep())
                .localidade(address.getLocalidade())
                .uf(address.getUf())
                .ddd(String.valueOf(address.getDdd()))
                .bairro(address.getBairro())
                .logradouro(address.getLogradouro())
                .complemento(address.getComplemento())
                .build();
    }
}
