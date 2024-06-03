package org.company.apicep.service;

import lombok.AllArgsConstructor;
import org.company.apicep.dto.AddressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@Service
public class AddressService {
    private RestTemplate restTemplate;

    public AddressDTO getAddressByCEP(String cep) {
        //chamar o servico VIACEP
        //viacep.com.br/ws/01001000/json/
        String url = UriComponentsBuilder.fromHttpUrl("https://viacep.com.br/ws/{cep}/json")
                .buildAndExpand(cep)
                .toUriString();
        return restTemplate.getForObject(url, AddressDTO.class);
    }
}
