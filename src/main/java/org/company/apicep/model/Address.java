package org.company.apicep.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The cep field is required.")
    @Size(max = 9, message = "The cep field required a max length of 9.")
    @Column(unique = true)
    private String cep;

    private String logradouro;
    private String complemento;

    @Size(max=255, message = "The bairro field required a max length of 255.")
    private String bairro;

    @NotBlank(message = "The localidade field is required.")
    @Size(max=255, message = "The localidade field required a max length of 255.")
    private String localidade;

    @NotBlank(message = "The uf field is required.")
    @Size(max=2, message = "The uf field required a max length of 2.")
    private String uf;

    @NotNull(message = "The ddd field is required.")
    private Integer ddd;

    @Column(name = "deleted", nullable = false)
    @Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
    private boolean deleted = false;
}
