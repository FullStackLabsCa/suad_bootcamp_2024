package io.reactivestax;

import io.reactivestax.contract.EvictionPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheFactoryTest {

    @InjectMocks
    private CacheFactory cacheFactory;

    @Mock
    private Cache<String, String> mockCache;

    @Mock
    private EvictionPolicy<String, String> mockPolicy;

    @Mock
    private DemonThreadService  mockDemonThreadService;


    @Test
    @DisplayName("Should generate unique key correctly")
    void testGenerateUniqueKey() {
        String uniqueKey = cacheFactory.generateUniqueKey(mockPolicy);
        assertEquals(mockPolicy.getClass().getName(), uniqueKey);
    }


    @Test
    @DisplayName("Should register eviction policy and start eviction thread")
    void testApplyEvictionPolicy() throws InterruptedException {
            long evictionInterval = 50L;
        try (MockedStatic<DemonThreadService> demonThreadServiceMockedStatic = mockStatic(DemonThreadService.class)) {
            demonThreadServiceMockedStatic.when(() -> DemonThreadService.spawnDemonOnEviction(any(), any(), anyLong())).thenReturn(new AtomicBoolean(true));

            cacheFactory.applyEvictionPolicy(mockCache, mockPolicy, evictionInterval);
            Thread.sleep(evictionInterval * 2);
            demonThreadServiceMockedStatic.verify(()->DemonThreadService.spawnDemonOnEviction(Mockito.any(), Mockito.any(), anyLong()), times(1));
        }
    }


    @Test
    @DisplayName("Should not create duplicate eviction tasks for the same policy")
    void testAvoidDuplicateEvictionTasks() {
        long evictionInterval = 50L;

        cacheFactory.applyEvictionPolicy(mockCache, mockPolicy, evictionInterval);
        cacheFactory.applyEvictionPolicy(mockCache, mockPolicy, evictionInterval);

        // Ensure only one task/thread is created
        assertEquals(1, cacheFactory.evictionTasks.size());
    }

}