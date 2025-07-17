package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account accountTest;
    private Account accountTest2;

    @BeforeEach
    void runBefore() {
        accountTest = new Account();
        accountTest2 = new Account("Name", 500, 780, true, "Excellent");
    }

    @Test
    void testConstructor() {
        assertEquals("", accountTest.getAccName());
        assertEquals(0, accountTest.getIncome());
        assertEquals(300, accountTest.getCreditScore());
        assertFalse(accountTest.getAccStatus());
        assertEquals("", accountTest.getCredScoreStatus());
    }

    @Test
    void testConstructorWithPara() {
        assertEquals("Name", accountTest2.getAccName());
        assertEquals(500, accountTest2.getIncome());
        assertEquals(780, accountTest2.getCreditScore());
        assertTrue(accountTest2.getAccStatus());
        assertEquals("Excellent", accountTest2.getCredScoreStatus());
    }

    @Test
    void testSetUpPerfectCreditScoreAccount() {
        accountTest.setUpAccount("John", 500, 900);
        assertTrue(accountTest.getAccStatus());
        assertEquals("John", accountTest.getAccName());
        assertEquals(500, accountTest.getIncome());
        assertEquals(900, accountTest.getCreditScore());
        assertEquals("Perfect", accountTest.getCredScoreStatus());
    }

    @Test
    void testSetUpPoorCreditScoreAccount() {
        accountTest.setUpAccount("John", 500, 400);
        assertTrue(accountTest.getAccStatus());
        assertEquals("John", accountTest.getAccName());
        assertEquals(500, accountTest.getIncome());
        assertEquals(400, accountTest.getCreditScore());
        assertEquals("Poor", accountTest.getCredScoreStatus());
    }

    @Test
    void testSetUpAcceptableCreditScoreAccount() {
        accountTest.setUpAccount("John", 500, 560);
        assertTrue(accountTest.getAccStatus());
        assertEquals("John", accountTest.getAccName());
        assertEquals(500, accountTest.getIncome());
        assertEquals(560, accountTest.getCreditScore());
        assertEquals("Acceptable", accountTest.getCredScoreStatus());

        accountTest.setUpAccount("John", 500, 659);
        assertEquals(659, accountTest.getCreditScore());
        assertEquals("Acceptable", accountTest.getCredScoreStatus());

        accountTest.setUpAccount("John", 500, 592);
        assertEquals(592, accountTest.getCreditScore());
        assertEquals("Acceptable", accountTest.getCredScoreStatus());
    }

    @Test
    void testSetUpGoodCreditScoreAccount() {
        accountTest.setUpAccount("John", 500, 660);
        assertTrue(accountTest.getAccStatus());
        assertEquals("John", accountTest.getAccName());
        assertEquals(500, accountTest.getIncome());
        assertEquals(660, accountTest.getCreditScore());
        assertEquals("Good", accountTest.getCredScoreStatus());

        accountTest.setUpAccount("John", 500, 661);
        assertEquals(661, accountTest.getCreditScore());
        assertEquals("Good", accountTest.getCredScoreStatus());
    }

    @Test
    void testSetUpGreatCreditScoreAccount() {
        accountTest.setUpAccount("John", 500, 725);
        assertTrue(accountTest.getAccStatus());
        assertEquals("John", accountTest.getAccName());
        assertEquals(500, accountTest.getIncome());
        assertEquals(725, accountTest.getCreditScore());
        assertEquals("Great", accountTest.getCredScoreStatus());

        accountTest.setUpAccount("John", 500, 759);
        assertEquals(759, accountTest.getCreditScore());
        assertEquals("Great", accountTest.getCredScoreStatus());
    }

    @Test
    void testSetUpExcellentCreditScoreAccount() {
        accountTest.setUpAccount("John", 500, 760);
        assertTrue(accountTest.getAccStatus());
        assertEquals("John", accountTest.getAccName());
        assertEquals(500, accountTest.getIncome());
        assertEquals(760, accountTest.getCreditScore());
        assertEquals("Excellent", accountTest.getCredScoreStatus());

        accountTest.setUpAccount("John", 500, 761);
        assertEquals(761, accountTest.getCreditScore());

        assertEquals("Excellent", accountTest.getCredScoreStatus());

        accountTest.setUpAccount("John", 500, 899);
        assertEquals(899, accountTest.getCreditScore());
        assertEquals("Excellent", accountTest.getCredScoreStatus());
    }

    @Test
    void testAddBudget() {
        accountTest.setUpAccount("Budget", 500, 800);        accountTest.addBudget(5000);
        assertEquals(500, accountTest.getIncome());
        assertEquals(5500, accountTest.getBudgetList().get(0).getBalance());
        assertEquals(500, accountTest.getBudgetList().get(0).getIncome());
        assertEquals(1, accountTest.getBudgetList().size());
    }

    @Test
    void testZeroAddBudget() {
        accountTest.setUpAccount("Budget", 0, 800);
        accountTest.addBudget(0);
        assertEquals(0, accountTest.getIncome());
        assertEquals(0, accountTest.getBudgetList().get(0).getBalance());
        assertEquals(0, accountTest.getBudgetList().get(0).getIncome());
        assertEquals(1, accountTest.getBudgetList().size());
    }

    @Test
    void testNegativeAddBudget() {
        accountTest.setUpAccount("Budget", 500, 800);
        accountTest.setUpAccount("Budget", 500, 700);
        accountTest.addBudget(-5000);
        assertEquals(500, accountTest.getIncome());
        assertEquals(500, accountTest.getBudgetList().get(0).getIncome());
        assertEquals(-4500, accountTest.getBudgetList().get(0).getBalance());
        assertEquals(1, accountTest.getBudgetList().size());
    }

    @Test
    void testAddMultipleBudgets() {
        accountTest.setUpAccount("John", 200, 800);
        accountTest.addBudget(500);
        accountTest.addBudget(600);
        assertEquals(200, accountTest.getIncome());
        assertEquals(700, accountTest.getBudgetList().get(0).getBalance());
        assertEquals(800, accountTest.getBudgetList().get(1).getBalance());
        assertEquals(2, accountTest.getBudgetList().size());
    }

    @Test
    void testRemoveBudgets() {
        accountTest.addBudget(500);
        assertTrue(accountTest.removeBudget(0));
        assertEquals(0, accountTest.getBudgetList().size());
    }

    @Test
    void testFailedRemoveBudgets() {
        assertFalse(accountTest.removeBudget(0));
        assertEquals(0, accountTest.getBudgetList().size());
    }

    @Test
    void testManyRemoveBudgets() {
        accountTest.addBudget(500);
        accountTest.addBudget(400);
        accountTest.addBudget(600);
        assertTrue(accountTest.removeBudget(0));
        assertTrue(accountTest.removeBudget(0));
        assertEquals(1, accountTest.getBudgetList().size());
    }

    @Test
    void testUpcomingAddBill() {
        accountTest.addBill("Water", 500, LocalDate.of(2024, 1 , 2));
        assertEquals("Water", accountTest.getUpcomingBillsList().get(0).getBillName());
        assertEquals(500, accountTest.getUpcomingBillsList().get(0).getAmountOwed());
        assertEquals(2024, accountTest.getUpcomingBillsList().get(0).getDueDate().getYear());
        assertEquals(1, accountTest.getUpcomingBillsList().get(0).getDueDate().getMonthValue());
        assertEquals(2, accountTest.getUpcomingBillsList().get(0).getDueDate().getDayOfMonth());
        assertEquals(1, accountTest.getUpcomingBillsList().size());
    }

    @Test
    void testApproachingAddBill() {
        accountTest.addBill("Electricity", 200, LocalDate.of(2023, 12 , 31));
        assertEquals(0, accountTest.getApproachingBillsList().size());
        accountTest.addBill("Heat", 200, LocalDate.of(2024, 12 , 31));
        assertEquals(0, accountTest.getApproachingBillsList().size());

        accountTest.addBill("Water", 500, LocalDate.of(2023, 12 , 15));
        assertEquals("Water", accountTest.getApproachingBillsList().get(0).getBillName());
        assertEquals(500, accountTest.getApproachingBillsList().get(0).getAmountOwed());
        assertEquals(2023, accountTest.getApproachingBillsList().get(0).getDueDate().getYear());
        assertEquals(12, accountTest.getApproachingBillsList().get(0).getDueDate().getMonthValue());
        assertEquals(15, accountTest.getApproachingBillsList().get(0).getDueDate().getDayOfMonth());
        assertEquals(1, accountTest.getApproachingBillsList().size());
    }

    @Test
    void testOverdueAddBill() {
        accountTest.addBill("Water", 500, LocalDate.of(2022, 11 , 1));
        assertEquals("Water", accountTest.getOverdueBillsList().get(0).getBillName());
        assertEquals(500, accountTest.getOverdueBillsList().get(0).getAmountOwed());
        assertEquals(2022, accountTest.getOverdueBillsList().get(0).getDueDate().getYear());
        assertEquals(11, accountTest.getOverdueBillsList().get(0).getDueDate().getMonthValue());
        assertEquals(1, accountTest.getOverdueBillsList().get(0).getDueDate().getDayOfMonth());
        assertEquals(1, accountTest.getOverdueBillsList().size());
    }

    @Test
    void testAddMultipleDifferentBills() {
        accountTest.addBill("Water", 500, LocalDate.of(2022, 11, 8));
        accountTest.addBill("Electricity", 200, LocalDate.of(2024, 8, 8));
        assertEquals("Water", accountTest.getOverdueBillsList().get(0).getBillName());
        assertEquals(500, accountTest.getOverdueBillsList().get(0).getAmountOwed());
        assertEquals(2022, accountTest.getOverdueBillsList().get(0).getDueDate().getYear());
        assertEquals(11, accountTest.getOverdueBillsList().get(0).getDueDate().getMonthValue());
        assertEquals(8, accountTest.getOverdueBillsList().get(0).getDueDate().getDayOfMonth());

        assertEquals("Electricity", accountTest.getUpcomingBillsList().get(0).getBillName());
        assertEquals(200, accountTest.getUpcomingBillsList().get(0).getAmountOwed());
        assertEquals(2024, accountTest.getUpcomingBillsList().get(0).getDueDate().getYear());
        assertEquals(8, accountTest.getUpcomingBillsList().get(0).getDueDate().getMonthValue());
        assertEquals(8, accountTest.getUpcomingBillsList().get(0).getDueDate().getDayOfMonth());

        assertEquals(1, accountTest.getOverdueBillsList().size());
        assertEquals(1, accountTest.getUpcomingBillsList().size());
    }

    @Test
    void testAddSaving() {
        accountTest.setUpAccount("John", 500, 800);
        accountTest.addBudget(500);
        accountTest.getBudgetList().get(0).addSaving("Saving1", 500);
        assertEquals("Saving1", accountTest.getBudgetList().get(0).getListOfSavings().get(0).getName());
        assertEquals(500, accountTest.getBudgetList().get(0).getListOfSavings().get(0).getAmount());
        assertEquals(1500, accountTest.getBudgetList().get(0).getBalance());
        assertEquals(1, accountTest.getBudgetList().get(0).getListOfSavings().size());
    }

    @Test
    void testAddExpense() {
        accountTest.setUpAccount("John", 500, 800);
        accountTest.addBudget(500);
        accountTest.getBudgetList().get(0).addExpense("Expense1", 500);
        assertEquals("Expense1", accountTest.getBudgetList().get(0).getListOfExpenses().get(0).getName());
        assertEquals(500, accountTest.getBudgetList().get(0).getListOfExpenses().get(0).getAmount());
        assertEquals(500, accountTest.getBudgetList().get(0).getBalance());
        assertEquals(1, accountTest.getBudgetList().get(0).getListOfExpenses().size());
    }

    @Test
    void testManyAddSavings() {
        accountTest.setUpAccount("John", 500, 800);
        accountTest.addBudget(500);
        accountTest.getBudgetList().get(0).addSaving("Saving1", 700);
        accountTest.getBudgetList().get(0).addSaving("Saving2", 600);
        assertEquals("Saving1", accountTest.getBudgetList().get(0).getListOfSavings().get(0).getName());
        assertEquals("Saving2", accountTest.getBudgetList().get(0).getListOfSavings().get(1).getName());
        assertEquals(700, accountTest.getBudgetList().get(0).getListOfSavings().get(0).getAmount());
        assertEquals(600, accountTest.getBudgetList().get(0).getListOfSavings().get(1).getAmount());
        assertEquals(2300, accountTest.getBudgetList().get(0).getBalance());
        assertEquals(2, accountTest.getBudgetList().get(0).getListOfSavings().size());
    }

    @Test
    void testManyAddExpenses() {
        accountTest.setUpAccount("John", 500, 800);
        accountTest.addBudget(500);
        accountTest.getBudgetList().get(0).addExpense("Expense1", 200);
        accountTest.getBudgetList().get(0).addExpense("Expense2", 100);
        assertEquals("Expense1", accountTest.getBudgetList().get(0).getListOfExpenses().get(0).getName());
        assertEquals("Expense2", accountTest.getBudgetList().get(0).getListOfExpenses().get(1).getName());
        assertEquals(200, accountTest.getBudgetList().get(0).getListOfExpenses().get(0).getAmount());
        assertEquals(100, accountTest.getBudgetList().get(0).getListOfExpenses().get(1).getAmount());
        assertEquals(700, accountTest.getBudgetList().get(0).getBalance());
        assertEquals(2, accountTest.getBudgetList().get(0).getListOfExpenses().size());
    }

    @Test
    void testPrintSummary() {
        assertNull(accountTest.printSummary());
        accountTest.setUpAccount("John", 500, 800);
        accountTest.addBudget(500);
        assertEquals("Budget Index\t|\t Balance\t\t\n\t\t0\t\t|\t1000.0\t\n", accountTest.printSummary());
    }
}