package com.cmms.inventory.repository;

import com.cmms.inventory.entity.StockTx;
import com.cmms.inventory.entity.StockTxId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockTxRepository extends JpaRepository<StockTx, StockTxId> {

    List<StockTx> findByCompanyIdAndInventoryIdOrderByCreatedAtDesc(String companyId, String inventoryId);

    @Query(value = """
        WITH BeginningBalance AS (
            SELECT
                t.inventory_id,
                t.dst_storage_id AS storage_id,
                SUM(t.qty_change) AS beginning_qty
            FROM stock_tx t
            WHERE t.company_id = :companyId
              AND t.created_at < :startDate
              AND (:siteId IS NULL OR t.site_id = :siteId)
              AND (:storageId IS NULL OR t.dst_storage_id = :storageId)
              AND (:inventoryId IS NULL OR t.inventory_id = :inventoryId)
            GROUP BY t.inventory_id, t.dst_storage_id
        ),
        PeriodMovement AS (
            SELECT
                t.inventory_id,
                t.dst_storage_id AS storage_id,
                SUM(CASE WHEN t.action_type = 'RECEIVED' THEN t.qty_change ELSE 0 END) AS received_qty,
                SUM(CASE WHEN t.action_type = 'ISSUED' THEN t.qty_change ELSE 0 END) AS issued_qty,
                SUM(CASE WHEN t.action_type = 'ADJUSTED' THEN t.qty_change ELSE 0 END) AS adjusted_qty
            FROM stock_tx t
            WHERE t.company_id = :companyId
              AND t.created_at >= :startDate AND t.created_at < :endDate
              AND (:siteId IS NULL OR t.site_id = :siteId)
              AND (:storageId IS NULL OR t.dst_storage_id = :storageId)
              AND (:inventoryId IS NULL OR t.inventory_id = :inventoryId)
            GROUP BY t.inventory_id, t.dst_storage_id
        )
        SELECT
            COALESCE(bb.inventory_id, pm.inventory_id) AS inventoryId,
            i.item_name AS itemName,
            COALESCE(bb.storage_id, pm.storage_id) AS storageId,
            COALESCE(bb.beginning_qty, 0) AS beginningQty,
            COALESCE(pm.received_qty, 0) AS receivedQty,
            COALESCE(pm.issued_qty, 0) AS issuedQty,
            COALESCE(pm.adjusted_qty, 0) AS adjustedQty,
            (COALESCE(bb.beginning_qty, 0) + COALESCE(pm.received_qty, 0) + COALESCE(pm.issued_qty, 0) + COALESCE(pm.adjusted_qty, 0)) AS endingQty
        FROM BeginningBalance bb
        FULL OUTER JOIN PeriodMovement pm ON bb.inventory_id = pm.inventory_id AND bb.storage_id = pm.storage_id
        LEFT JOIN inventory i ON i.company_id = :companyId AND i.inventory_id = COALESCE(bb.inventory_id, pm.inventory_id)
    """, nativeQuery = true)
    List<Object[]> getInventoryLedgerData(
            @Param("companyId") String companyId,
            @Param("siteId") String siteId,
            @Param("storageId") String storageId,
            @Param("inventoryId") String inventoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT tx.inventoryId, tx.dstStorageId, SUM(tx.qtyChange) FROM StockTx tx " +
           "WHERE tx.companyId = :companyId " +
           "AND tx.siteId = :siteId " +
           "AND (:storageId IS NULL OR tx.dstStorageId = :storageId) " +
           "AND tx.createdAt >= :startDate AND tx.createdAt < :endDate " +
           "GROUP BY tx.inventoryId, tx.dstStorageId")
    List<Object[]> getMonthlyMovements(
            @Param("companyId") String companyId,
            @Param("siteId") String siteId,
            @Param("storageId") String storageId,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate
    );
}
