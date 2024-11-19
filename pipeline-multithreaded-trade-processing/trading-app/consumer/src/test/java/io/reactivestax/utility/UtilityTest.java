package io.reactivestax.utility;

import io.reactivestax.types.dto.Trade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilityTest {
    String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02, 0";

    @Test
    void checkPrepareTrade() {
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

    @Test
    void testCheckValidity(){
        String[] split = payload.split(",");
        assertTrue(Utility.checkValidity(split));
    }
}