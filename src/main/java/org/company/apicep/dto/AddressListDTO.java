package org.company.apicep.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddressListDTO {
    private long totalPages;
    private long totalElements;
    private List<AddressDTO> content;
}
