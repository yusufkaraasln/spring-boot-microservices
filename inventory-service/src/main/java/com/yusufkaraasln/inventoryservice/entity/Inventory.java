package com.yusufkaraasln.inventoryservice.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Inventory {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String skuCode;
        private Integer quantity;


}
