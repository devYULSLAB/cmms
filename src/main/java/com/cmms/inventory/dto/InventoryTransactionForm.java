package com.cmms.inventory.dto;

import com.cmms.inventory.entity.StockTx;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransactionForm {

    private String siteId;
    private List<StockTx> stockTxs = new ArrayList<>();

}
