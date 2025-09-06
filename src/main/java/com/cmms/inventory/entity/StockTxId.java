package com.cmms.inventory.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTxId implements Serializable {
    private String companyId;
    private String txId;
}
