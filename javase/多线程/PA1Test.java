package javase.多线程;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
public class PA1Test {
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
    }

    // 测试：初始仓库为空
    @Test
    void testInitialStockIsZero() {
        assertEquals(0, warehouse.getStock());
        assertTrue(warehouse.isEmpty());
        assertFalse(warehouse.isFull());
    }

    // 测试：生产1个产品
    @Test
    void testProduceOne() throws InterruptedException {
        warehouse.produce(1);
        assertEquals(1, warehouse.getStock());
    }

    // 测试：消费1个产品
    @Test
    void testConsumeOne() throws InterruptedException {
        warehouse.produce(100);
        warehouse.consume();
        assertEquals(0, warehouse.getStock());
    }

    // 测试：仓库满了之后不能再生产
    @Test
    void testWarehouseFull() throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            warehouse.produce(i);
        }
        assertTrue(warehouse.isFull());
    }

    // 测试：消费后仓库不满，可以继续生产
    @Test
    void testConsumeAfterFull() throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            warehouse.produce(i);
        }
        warehouse.consume();
        assertFalse(warehouse.isFull());
        assertEquals(4, warehouse.getStock());
    }
}
