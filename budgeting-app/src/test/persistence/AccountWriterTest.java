package persistence;

import model.Account;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AccountWriterTest {

    // Inspired from JsonSerializationDemo
    @Test
    void testWriterInvalidFile() {
        try {
            Account acc = new Account();
            AccountWriter writer = new AccountWriter("./data/my\0illegal:fileName.json");
            writer.openWriter();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    // Inspired from JsonSerializationDemo
    @Test
    void testWriterEmptyAccount() {
        try {
            Account account = new Account();
            AccountWriter writer = new AccountWriter("./data/testWriterEmptyAccount.json");
            writer.openWriter();
            writer.write(account);
            writer.closeWriter();

            AccountReader reader = new AccountReader("./data/testWriterEmptyAccount.json");
            account = reader.read();
            assertEquals("", account.getAccName());
            assertEquals(0, account.getIncome());
            assertEquals(300, account.getCreditScore());
            assertFalse(account.getAccStatus());
            assertEquals("", account.getCredScoreStatus());

        } catch (IOException e) {
            fail("An unexpected exception was thrown.");
        }
    }

    // Inspired from JsonSerializationDemo
    @Test
    void testWriterGeneralAccount() {
        try {
            Account account = new Account();
            account.setUpAccount("Test", 500, 900);
            account.addBudget(500);
            account.addBudget(5000);
            account.getBudgetList().get(0).addSaving("Cash", 300);
            account.getBudgetList().get(1).addSaving("Loan", 4000);
            account.getBudgetList().get(0).addExpense("Tuition", 3000);
            account.getBudgetList().get(1).addExpense("Babysitter", 30);
            AccountWriter writer = new AccountWriter("./data/testWriterGeneralAccount.json");
            writer.openWriter();
            writer.write(account);
            writer.closeWriter();

            AccountReader reader = new AccountReader("./data/testWriterGeneralAccount.json");
            account = reader.read();
            assertEquals("Test", account.getAccName());
            assertEquals(500, account.getIncome());
            assertEquals(900, account.getCreditScore());
            assertTrue(account.getAccStatus());
            assertEquals("Perfect", account.getCredScoreStatus());
            assertEquals(-1200, account.getBudgetList().get(0).getBalance());
            assertEquals(9970, account.getBudgetList().get(1).getBalance());
            assertEquals(2, account.getBudgetList().size());
        } catch (IOException e) {
            fail("An unexpected exception was thrown.");
        }
    }

    // Inspired from JsonSerializationDemo
    @Test
    void testWriterAccountWithBills () {
        try {
            Account account = new Account();
            account.setUpAccount("Test", 500, 900);

            account.addBill("Heat", 300, LocalDate.of(2012, 12, 31));
            account.addBill("Water", 500, LocalDate.of(2024, 12, 1));
            account.addBill("Electricity", 400, LocalDate.of(2023, 12, 15));

            AccountWriter writer = new AccountWriter("./data/testAccountWithBills.json");
            writer.openWriter();
            writer.write(account);
            writer.closeWriter();

            AccountReader reader = new AccountReader("./data/testAccountWithBills.json");
            account = reader.read();
            assertEquals("Heat", account.getOverdueBillsList().get(0).getBillName());
            assertEquals(300, account.getOverdueBillsList().get(0).getAmountOwed());
            assertEquals(2012, account.getOverdueBillsList().get(0).getDueDate().getYear());
            assertEquals(12, account.getOverdueBillsList().get(0).getDueDate().getMonthValue());
            assertEquals(31, account.getOverdueBillsList().get(0).getDueDate().getDayOfMonth());
            assertEquals(1, account.getOverdueBillsList().size());

            assertEquals("Water", account.getUpcomingBillsList().get(0).getBillName());
            assertEquals(500, account.getUpcomingBillsList().get(0).getAmountOwed());
            assertEquals(2024, account.getUpcomingBillsList().get(0).getDueDate().getYear());
            assertEquals(12, account.getUpcomingBillsList().get(0).getDueDate().getMonthValue());
            assertEquals(1, account.getUpcomingBillsList().get(0).getDueDate().getDayOfMonth());
            assertEquals(1, account.getUpcomingBillsList().size());

            assertEquals("Electricity", account.getApproachingBillsList().get(0).getBillName());
            assertEquals(400, account.getApproachingBillsList().get(0).getAmountOwed());
            assertEquals(2023, account.getApproachingBillsList().get(0).getDueDate().getYear());
            assertEquals(12, account.getApproachingBillsList().get(0).getDueDate().getMonthValue());
            assertEquals(15, account.getApproachingBillsList().get(0).getDueDate().getDayOfMonth());
            assertEquals(1, account.getApproachingBillsList().size());
        } catch (IOException e) {
            fail("An unexpected exception was thrown.");
        }
    }
}
