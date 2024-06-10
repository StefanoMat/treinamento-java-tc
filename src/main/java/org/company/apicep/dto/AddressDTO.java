package org.company.apicep.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddressDTO {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String ddd;
}
