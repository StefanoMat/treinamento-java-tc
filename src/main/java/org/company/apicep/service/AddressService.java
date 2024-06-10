package org.company.apicep.service;

import lombok.AllArgsConstructor;
import org.company.apicep.dto.AddressDTO;
import org.company.apicep.model.Address;
import org.company.apicep.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AddressService {
    private RestTemplate restTemplate;
    private AddressRepository addressRepository;

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
