package com.axe.price_history;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceHistoryService {
    private final PriceHistoryRepository priceHistoryRepository;

    public PriceHistoryService(PriceHistoryRepository priceHistoryRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
    }

    public PriceHistory createPriceHistory(PriceHistory priceHistory) {
        return priceHistoryRepository.save(priceHistory);
    }

    public List<PriceHistory> getAllPriceHistory() {
        return priceHistoryRepository.findAll();
    }

    public PriceHistory getPriceHistoryById(Long id) {
        Optional<PriceHistory> optional = priceHistoryRepository.findById(id);
        return optional.orElse(null);
    }

    public PriceHistory updatePriceHistory(Long id, PriceHistory updatedRecord) {
        return priceHistoryRepository.findById(id).map(existingRecord -> {
            // Update fields as necessary
            existingRecord.setUserId(updatedRecord.getUserId());
            existingRecord.setProductId(updatedRecord.getProductId());
            existingRecord.setChangedAt(updatedRecord.getChangedAt());
            existingRecord.setOldPrice(updatedRecord.getOldPrice());
            existingRecord.setNewPrice(updatedRecord.getNewPrice());

            return priceHistoryRepository.save(existingRecord);
        }).orElse(null);
    }

    public boolean deletePriceHistory(Long id) {
        if (priceHistoryRepository.existsById(id)) {
            priceHistoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void recordPriceChange(Long userId, Long productId,
                                 java.math.BigDecimal oldPrice,
                                 java.math.BigDecimal newPrice) {
        PriceHistory history = new PriceHistory();
        history.setUserId(userId);
        history.setProductId(productId);
        history.setChangedAt(java.time.LocalDateTime.now());
        history.setOldPrice(oldPrice);
        history.setNewPrice(newPrice);
        priceHistoryRepository.save(history);
    }

    @Transactional
    public void deleteAllByProductId(Long productId) {
        priceHistoryRepository.deleteAllByProductId(productId);
    }


}
