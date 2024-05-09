package com.example.redisdistributedrock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket {
    private Long id;
    private String name;
    private Integer quantity;
}
