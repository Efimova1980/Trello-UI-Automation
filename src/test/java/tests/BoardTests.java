package tests;

import dto.Board;
import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.BoardsPage;
import pages.MyBoardPage;
import utils.TestNGListener;

@Listeners(TestNGListener.class)

public class BoardTests extends AppManager {

    private BoardsPage boardsPage;
    private String boardTitle;   // shared by creation and delete tests

    // one login per class (the browser stays open between tests)
    @BeforeClass(alwaysRun = true)
    public void loginOnce(){
        loginTrello();
    }

    @BeforeMethod(alwaysRun = true)
    public void goToTrello(){
        boardsPage = openTrello();
    }

    @Test(groups = "smoke")
    public void createNewBoardPositiveTest(){
        boardTitle = "12345-" + System.currentTimeMillis();
        Board board = Board.builder()
                .boardTitle(boardTitle).build();
        MyBoardPage myBoardPage = boardsPage.createNewBoard(board);
        Assert.assertTrue(myBoardPage.validateBoardName(boardTitle, 15));
    }

    // deletes the board created by the previous test; skipped if creation failed
    @Test(groups = "smoke", dependsOnMethods = "createNewBoardPositiveTest")
    public void deleteBoardPositiveTest(){
        boardsPage.openBoard(boardTitle);
        new MyBoardPage(getDriver()).deleteBoard();
        Assert.assertTrue(boardsPage.validateURL("boards"));
        Assert.assertTrue(boardsPage.validateBoardNotPresent(boardTitle, 10));
    }
}
