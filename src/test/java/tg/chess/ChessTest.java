package tg.chess;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ChessTest {

    private Chess chess = Chess.createNew();

    @Test
    public void kingShouldMoveInAll8DirectionsByOneStep() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.KING, "D5");

        assertThat(nextPositions, hasSize(8));
        assertThat(nextPositions, hasItems("C6", "D6", "E6", "E5", "E4", "D4", "C4", "C5"));
    }

    @Test
    public void kingShouldMoveInAvailableDirectionsFromCorner() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.KING, "A8");

        assertThat(nextPositions, hasSize(3));
        assertThat(nextPositions, hasItems("B8", "B7", "A7"));
    }

    @Test
    public void kingShouldMoveInAvailableDirectionsFromCloseToBorder() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.KING, "H4");

        assertThat(nextPositions, hasSize(5));
        assertThat(nextPositions, hasItems("H5", "G5", "G4", "G3", "H3"));
    }

    @Test
    public void pawnShouldMoveVerticallyUp() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.PAWN, "D4");

        assertThat(nextPositions, hasSize(1));
        assertThat(nextPositions, hasItems("D5"));
    }

    @Test
    public void pawnShouldNotMoveVerticallyUpWhenAtTheEnd() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.PAWN, "D8");

        assertThat(nextPositions, is(empty()));
    }

    @Test
    public void horseShouldGo2AndHalfPositionsIn8Directions() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.HORSE, "E3");

        assertThat(nextPositions, hasSize(8));
        assertThat(nextPositions, hasItems("D5", "F5", "G4", "G2", "F1", "D1", "C2", "C4"));
    }

    @Test
    public void horseShouldGo2AndHalfPositionsIn8DirectionsIfPossible() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.HORSE, "A4");

        assertThat(nextPositions, hasSize(4));
        assertThat(nextPositions, hasItems("B6", "C5", "C3", "B2"));
    }

    @Test
    public void horseShouldGo2AndHalfPositionsIn8DirectionsIfPossibleFromNearBorder() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.HORSE, "B3");

        assertThat(nextPositions, hasSize(6));
        assertThat(nextPositions, hasItems("A5", "C5", "D4", "D2", "A1", "C1"));
    }

    @Test
    public void rookCanMoveHorizontallyOrVertically() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.ROOK, "D5");

        assertThat(nextPositions, hasSize(4));
        assertThat(nextPositions, hasItems("A5", "H5", "D8", "D1"));
    }

    @Test
    public void rookCanMoveHorizontallyOrVerticallyFromCorner() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.ROOK, "A1");

        assertThat(nextPositions, hasSize(2));
        assertThat(nextPositions, hasItems("A8", "H1"));
    }

    @Test
    public void rookCanMoveHorizontallyOrVerticallyFromNearBorder() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.ROOK, "B2");

        assertThat(nextPositions, hasSize(4));
        assertThat(nextPositions, hasItems("A2", "B1", "B8", "H2"));
    }

    @Test
    public void bishopCanMoveDiagonally() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.BISHOP, "D4");

        assertThat(nextPositions, hasSize(4));
        assertThat(nextPositions, hasItems("A7", "G1", "H8", "A1"));
    }

    @Test
    public void bishopCanMoveDiagonallyIfPossible() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.BISHOP, "A6");

        assertThat(nextPositions, hasSize(2));
        assertThat(nextPositions, hasItems("C8", "F1"));
    }

    @Test
    public void queenCanMoveInAllDirections() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.QUEEN, "D5");

        assertThat(nextPositions, hasSize(8));
        assertThat(nextPositions, hasItems("D8", "H5", "D1", "A5", "G8", "H1", "A2", "A8"));
    }

    @Test
    public void queenCanMoveInAllDirectionsIfPossible() {
        final List<String> nextPositions = chess.findAllNextPositions(Chess.Piece.QUEEN, "A2");

        assertThat(nextPositions, hasSize(5));
        assertThat(nextPositions, hasItems("A1", "H2", "A8", "B1", "G8"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIncorrectLocationOfMoreThan2Length() {
        chess.findAllNextPositions(Chess.Piece.PAWN, "A2A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIncorrectLocationOfNull() {
        chess.findAllNextPositions(Chess.Piece.PAWN, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIncorrectLocationIncomplete() {
        chess.findAllNextPositions(Chess.Piece.PAWN, "A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIncorrectLocationNonNumber() {
        chess.findAllNextPositions(Chess.Piece.PAWN, "AA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIncorrectLocationBeforeA() {
        chess.findAllNextPositions(Chess.Piece.PAWN, "x1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIncorrectLocationBeyondH() {
        chess.findAllNextPositions(Chess.Piece.PAWN, "I2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIncorrectLocationBelow1Row() {
        chess.findAllNextPositions(Chess.Piece.PAWN, "A0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIncorrectLocationAbove8Row() {
        chess.findAllNextPositions(Chess.Piece.PAWN, "A9");
    }
}
