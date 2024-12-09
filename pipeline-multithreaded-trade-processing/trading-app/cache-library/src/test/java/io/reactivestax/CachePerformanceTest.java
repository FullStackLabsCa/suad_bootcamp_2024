//package io.reactivestax;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
//import static org.mockito.Mockito.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class CachePerformanceTest {
//    private CachePerformance cachePerformance;
//
//    @BeforeEach
//    void setUp() {
//        cachePerformance = new CachePerformance();
//    }
//
//    @Test
//    void testAddData() {
//        cachePerformance.addData();
//        assertEquals(4, cachePerformance.cache.size());
//    }
//
//    @Test
//    void testCleanupExpiredEntries() throws InterruptedException {
//        cachePerformance.addData();
//        Thread.sleep(3000); // Let some TTLs expire
//        cachePerformance.cleanupExpiredEntries();
//        assertTrue(cachePerformance.cache.size() < 4);
//    }
//
//    @Test
//    void testRunFlow() {
//        CachePerformance mockCachePerformance = spy(CachePerformance.class);
//        mockCachePerformance.runFlow();
//        verify(mockCachePerformance).addData();
//    }
//
//    @Test
//    void testSpawnDemonThread() {
//        CachePerformance mockCachePerformance = spy(CachePerformance.class);
//        mockCachePerformance.spawnDemonThread();
//        verify(mockCachePerformance).spawnDemonThread();
//    }
//
//
//
//    @Test
//    void testSpawnDemonThreadCatchBlock() throws InterruptedException {
//        CachePerformance cachePerformance = spy(new CachePerformance());
//
//        // Mock the sleep method to throw an InterruptedException
//        doThrow(new InterruptedException()).when(cachePerformance).sleep(anyLong());
//
//        // Run spawnDemonThread
//        cachePerformance.spawnDemonThread();
//
//        // Allow the thread to run briefly
//        Thread.sleep(500);
//
//        // Verify that cleanupExpiredEntries was called at least once
//        verify(cachePerformance, atLeastOnce()).cleanupExpiredEntries();
//    }
//
//
//
//    @Test
//    void testStartApplication() throws InterruptedException {
//        // Capture console output
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        // Spy on the CachePerformance instance
//        CachePerformance cachePerformance = spy(CachePerformance.class);
//
//        // Start the application for a short duration to test
//        cachePerformance.startApplication(5000); // Run for 5 seconds
//
//        // Verify that cleanupExpiredEntries was called multiple times
//        Thread.sleep(6000); // Wait for execution to complete
//        verify(cachePerformance, Mockito.atLeastOnce()).cleanupExpiredEntries();
//
//        // Check console output
////        String output = outContent.toString();
////        assertTrue(output.contains("Cleaning up expired entries"));
////
////        // Restore System.out
////        System.setOut(System.out);
//    }
//
//
//}