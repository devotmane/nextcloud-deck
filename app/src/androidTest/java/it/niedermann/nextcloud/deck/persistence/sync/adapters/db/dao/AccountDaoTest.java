package it.niedermann.nextcloud.deck.persistence.sync.adapters.db.dao;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.niedermann.nextcloud.deck.model.Account;
import it.niedermann.nextcloud.deck.persistence.sync.adapters.db.DeckDatabaseTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class AccountDaoTest extends AbstractDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void testCreate() {
        final Account accountToCreate = new Account();
        accountToCreate.setName("test@example.com");
        accountToCreate.setUserName("test");
        accountToCreate.setUrl("https://example.com");

        long id = db.getAccountDao().insert(accountToCreate);
        final Account account = db.getAccountDao().getAccountByIdDirectly(id);

        assertEquals("test", account.getUserName());
        assertEquals("https://example.com", account.getUrl());
        assertEquals(Integer.valueOf(0), account.getColor());
        assertEquals(Integer.valueOf(0), account.getTextColor());
        assertEquals("0.6.4", account.getServerDeckVersion());
        assertEquals("https://example.com/index.php/avatar/test/1337", account.getAvatarUrl(1337));
        assertEquals(1, db.getAccountDao().countAccountsDirectly());
        assertNull(account.getEtag());
        assertFalse(account.isMaintenanceEnabled());
    }

    @Test
    public void testGetAccountById() throws InterruptedException {
        final Account account = DeckDatabaseTestUtil.createAccount(db.getAccountDao());
        assertEquals(account.getName(), DeckDatabaseTestUtil.getOrAwaitValue(db.getAccountDao().getAccountById(account.getId())).getName());
    }

    @Test
    public void testGetAccountByName() throws InterruptedException {
        final Account account = DeckDatabaseTestUtil.createAccount(db.getAccountDao());
        assertEquals(account.getUserName(), DeckDatabaseTestUtil.getOrAwaitValue(db.getAccountDao().getAccountByName(account.getName())).getUserName());
    }

    @Test
    public void testGetAllAccounts() throws InterruptedException {
        final int expectedCount = 13;
        for (int i = 0; i < expectedCount; i++) {
            DeckDatabaseTestUtil.createAccount(db.getAccountDao());
        }
        assertEquals(expectedCount, DeckDatabaseTestUtil.getOrAwaitValue(db.getAccountDao().getAllAccounts()).size());
    }

    @Test
    public void testCountAccountsDirectly() {
        final int expectedCount = 12;
        for (int i = 0; i < expectedCount; i++) {
            DeckDatabaseTestUtil.createAccount(db.getAccountDao());
        }
        assertEquals(expectedCount, db.getAccountDao().countAccountsDirectly());
    }

    @Test
    public void testCountAccounts() throws InterruptedException {
        final int expectedCount = 13;
        for (int i = 0; i < expectedCount; i++) {
            DeckDatabaseTestUtil.createAccount(db.getAccountDao());
        }
        assertEquals(Integer.valueOf(expectedCount), DeckDatabaseTestUtil.getOrAwaitValue(db.getAccountDao().countAccounts()));
    }

    @Test
    public void testGetAllAccountsDirectly() {
        final int expectedCount = 12;
        for (int i = 0; i < expectedCount; i++) {
            DeckDatabaseTestUtil.createAccount(db.getAccountDao());
        }
        assertEquals(expectedCount, db.getAccountDao().getAllAccountsDirectly().size());
    }

    @Test
    public void testGetAccountByNameDirectly() {
        final Account account = DeckDatabaseTestUtil.createAccount(db.getAccountDao());
        assertEquals(account.getName(), db.getAccountDao().getAccountByNameDirectly(account.getName()).getName());
    }
}
