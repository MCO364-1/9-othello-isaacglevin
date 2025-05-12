import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class OthelloModelTest {
    private OthelloModel model;

    @BeforeEach
    public void setUp() {
        model = new OthelloModel();
    }

    @Test
    public void testInitialSetup() {
        int[][] board = model.getBoard();
        assertEquals(OthelloModel.WHITE, board[3][3]);
        assertEquals(OthelloModel.BLACK, board[3][4]);
        assertEquals(OthelloModel.BLACK, board[4][3]);
        assertEquals(OthelloModel.WHITE, board[4][4]);
    }

    @Test
    public void testValidMove() {
        assertTrue(model.isValidMove(2, 3, OthelloModel.BLACK));
        assertFalse(model.isValidMove(0, 0, OthelloModel.BLACK));
    }

    @Test
    public void testMakeMove() {
        assertTrue(model.makeMove(2, 3, OthelloModel.BLACK));
        assertEquals(OthelloModel.BLACK, model.getBoard()[3][3]);
    }

    @Test
    public void testGreedyMoveNotNull() {
        int[] move = model.getGreedyMove(OthelloModel.BLACK);
        assertNotNull(move);
    }

    @Test
    public void testPieceCount() {
        assertEquals(2, model.countPieces(OthelloModel.BLACK));
        assertEquals(2, model.countPieces(OthelloModel.WHITE));
    }
}
