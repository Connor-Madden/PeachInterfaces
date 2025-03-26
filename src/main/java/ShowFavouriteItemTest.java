import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShowFavouriteItemTest {

  private UserDatabase db;

  //////////////////// "RED"  /////////////////

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

  // Test adding a favorite item when the user has no previous favorites
  @Test
  public void testAddFavouriteItemToEmptyList() {
    db.addFavourite("user", 7);
    int[] favouriteIDs = db.getFavourites("user");
    assertArrayEquals(new int[]{7}, favouriteIDs);
  }

  // Test removing a favorite item from user's list
  @Test
  public void testRemoveFavouriteItem() {
    db.addFavourite("user", 5);
    db.addFavourite("user", 10);
    db.removeFavourite("user", 5); // Remove item 5

    int[] favouriteIDs = db.getFavourites("user");
    assertArrayEquals(new int[]{10}, favouriteIDs); // Item 5 removed, item 10 should remain
  }

  //////////////////// "REPEAT"  /////////////////

//  @Test
//  public void testAddDuplicateFavouriteItem() {
//    db.addFavourite("user", 5);
//    db.addFavourite("user", 5); // Add the same item again
//
//    int[] favouriteIDs = db.getFavourites("user");
//    assertArrayEquals(new int[]{5, 5}, favouriteIDs); // Should allow duplicates
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void testAddFavouriteItemWithNullUser() {
//    db.addFavourite(null, 5); // Should throw an exception
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void testGetFavouritesWithNullUser() {
//    db.getFavourites(null); // Should throw an exception
//  }

//  @Test(expected = IllegalArgumentException.class)
//  public void testRemoveFavouriteItemWithNullUser() {
//    db.removeFavourite(null, 5); // Should throw an exception
//  }
}