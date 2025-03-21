import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShowFavouriteItemTest {

  private UserDatabase db;

  // setup the database
  @Before
  public void setUp() {
    db = new UserDatabase();
  }

  // make sure its instantiates correctly
  @Test
  public void testUserDatabaseInstantiation() {
    assertNotNull(db);
  }

  // test adding a favourite item
  @Test
  public void testAddFavouriteItem() {
    db.addFavourite("user", 5);
    int[] favouriteIDs = db.getFavourites("user");
    assertArrayEquals(new int[]{5}, favouriteIDs);
  }

  // test getting a users favourites
  @Test
  public void testGetFavouriteItems() {
    db.addFavourite("user", 5);
    db.addFavourite("user", 10);
    int[] favouriteIDs = db.getFavourites("user");
    assertArrayEquals(new int[]{5, 10}, favouriteIDs);
  }
}