package repository.history;

import model.ScannedItem;

import java.util.Collections;
import java.util.List;

public interface Repository
{
    default ScannedItem add(ScannedItem scannedItem)
    {
        return scannedItem;
    }

    default List<ScannedItem> findByReceiptId(int receiptId)
    {
        return Collections.emptyList();
    }
}
