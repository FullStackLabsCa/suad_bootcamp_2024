package io.reactivestax.utility;

import io.reactivestax.types.dto.Trade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    void roundRobin() {
        int roundRobin = Utility.roundRobin();
        System.out.println("Rr" + roundRobin);
        assertTrue(roundRobin >= 0 && roundRobin <= 2);
    }

    @Test
    void random() {
        int random = Utility.random();
        System.out.println("random" + random);
        assertTrue(random >= 1 && random <= 3);
    }

    @Test
    void prepareTrade() {
        String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02, 0";
        Trade trade = Utility.prepareTrade(payload);
        Trade trade1 = new Trade(

                "TDB_00000000",
                "2024-09-19 22:16:18",
                "TDB_CUST_5214938",
                "V",
                "SELL",
                Integer.parseInt("683"),
                Double.parseDouble("638.02"),
                Integer.parseInt("683")
        );

        assertEquals(trade, trade1);

    }
}