package ph.parcs.rmhometiles.entity.inventory.item;

public class ItemPageEntry {

    private final long toEntry;
    private final long fromEntry;

    public ItemPageEntry(long toEntry, long fromEntry) {
        this.toEntry = toEntry;
        this.fromEntry = fromEntry;
    }

    public long getToEntry() {
        return toEntry;
    }

    public long getFromEntry() {
        return fromEntry;
    }
}
