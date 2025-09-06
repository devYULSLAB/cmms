package com.cmms.inventory.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.inventory.entity.Stock;
import com.cmms.inventory.entity.StockId;
import com.cmms.inventory.entity.StockTx;
import com.cmms.inventory.repository.StockRepository;
import com.cmms.inventory.repository.StockTxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockTxRepository stockTxRepository;
    private final IdGeneratorService idGeneratorService;

    @Transactional
    public void processStockTransaction(List<StockTx> transactions) {
        for (StockTx tx : transactions) {
            tx.setTxId(idGeneratorService.generateId(tx.getCompanyId(), "8"));

            switch (tx.getActionType()) {
                case "IN":
                    handleInbound(tx);
                    break;
                case "OUT":
                    handleOutbound(tx);
                    break;
                case "MOVE":
                    handleMove(tx);
                    break;
                case "ADJUST":
                    handleAdjustment(tx);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid action type: " + tx.getActionType());
            }
            stockTxRepository.save(tx);
        }
    }

    private void handleInbound(StockTx tx) {
        Stock stock = loadOrCreateStock(tx.getCompanyId(), tx.getSiteId(), tx.getDstStorageId(), tx.getInventoryId());
        BigDecimal currentQty = nz(stock.getQty());
        BigDecimal changeQty = nz(tx.getQtyChange());
        tx.setBeforeQty(currentQty);
        stock.setQty(currentQty.add(changeQty));
        tx.setAfterQty(stock.getQty());
        stockRepository.save(stock);
    }

    private void handleOutbound(StockTx tx) {
        Stock stock = findStockForUpdate(tx.getCompanyId(), tx.getSiteId(), tx.getSrcStorageId(), tx.getInventoryId());
        BigDecimal currentQty = nz(stock.getQty());
        BigDecimal changeQty = nz(tx.getQtyChange());
        if (currentQty.compareTo(changeQty) < 0) {
            throw new IllegalStateException("Insufficient stock for outbound transaction.");
        }
        tx.setBeforeQty(currentQty);
        stock.setQty(currentQty.subtract(changeQty));
        tx.setAfterQty(stock.getQty());
        stockRepository.save(stock);
    }

    private void handleMove(StockTx tx) {
        Stock fromStock = findStockForUpdate(tx.getCompanyId(), tx.getSiteId(), tx.getSrcStorageId(), tx.getInventoryId());
        BigDecimal currentFromQty = nz(fromStock.getQty());
        BigDecimal changeQty = nz(tx.getQtyChange());
        if (currentFromQty.compareTo(changeQty) < 0) {
            throw new IllegalStateException("Insufficient stock for move transaction at source.");
        }
        fromStock.setQty(currentFromQty.subtract(changeQty));
        stockRepository.save(fromStock);

        Stock toStock = loadOrCreateStock(tx.getCompanyId(), tx.getSiteId(), tx.getDstStorageId(), tx.getInventoryId());
        toStock.setQty(nz(toStock.getQty()).add(changeQty));
        stockRepository.save(toStock);
    }

    private void handleAdjustment(StockTx tx) {
        Stock stock = loadOrCreateStock(tx.getCompanyId(), tx.getSiteId(), tx.getDstStorageId(), tx.getInventoryId());
        BigDecimal currentQty = nz(stock.getQty());
        BigDecimal changeQty = nz(tx.getQtyChange());
        BigDecimal newQty = currentQty.add(changeQty);
        if (newQty.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Stock cannot be negative after adjustment.");
        }
        tx.setBeforeQty(currentQty);
        stock.setQty(newQty);
        tx.setAfterQty(stock.getQty());
        stockRepository.save(stock);
    }

    private Stock findStockForUpdate(String companyId, String siteId, String storageId, String inventoryId) {
        return stockRepository.findByIdWithLock(companyId, siteId, storageId, inventoryId)
                .orElseThrow(() -> new IllegalStateException("Stock not found for update."));
    }

    private Stock loadOrCreateStock(String companyId, String siteId, String storageId, String inventoryId) {
        Optional<Stock> existingStock = stockRepository.findByIdWithLock(companyId, siteId, storageId, inventoryId);
        if (existingStock.isPresent()) {
            return existingStock.get();
        } else {
            Stock newStock = new Stock();
            newStock.setId(new StockId(companyId, siteId, storageId, inventoryId));
            newStock.setQty(BigDecimal.ZERO);
            newStock.setUnitPrice(BigDecimal.ZERO);
            return newStock;
        }
    }

    private BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
