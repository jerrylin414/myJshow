package com.lzy.jshow.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FillDataDTO {
    private String name;
    private String val;
    private Double total;
    private String description;
}
