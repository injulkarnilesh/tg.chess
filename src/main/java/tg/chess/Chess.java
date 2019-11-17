package tg.chess;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Chess {

    public static Chess createNew() {
        return new Chess();
    }

    public List<String> findAllNextPositions(final Piece piece, final String currentPosition) {
        final Position position = Position.parse(currentPosition);
        return piece.getPossibleMoves()
                .stream()
                .map(move -> move.perform(position))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(p -> p.toString())
                .collect(Collectors.toList());
    }

    public enum  Piece {
        KING(Move.LEFT, Move.RIGHT, Move.UP, Move.DOWN, Move.UP_LEFT,
                Move.UP_RIGHT, Move.DOWN_LEFT, Move.DOWN_RIGHT),

        PAWN(Move.UP),

        HORSE(Move.UP_LEFT.then(Move.UP), Move.UP_LEFT.then(Move.LEFT),
                Move.UP_RIGHT.then(Move.UP), Move.UP_RIGHT.then(Move.RIGHT),
                Move.DOWN_LEFT.then(Move.DOWN), Move.DOWN_LEFT.then(Move.LEFT),
                Move.DOWN_RIGHT.then(Move.DOWN), Move.DOWN_RIGHT.then(Move.RIGHT)),

        ROOK(Move.UP.keepGoing(), Move.DOWN.keepGoing(),
                Move.LEFT.keepGoing(), Move.RIGHT.keepGoing()),

        BISHOP(Move.UP_LEFT.keepGoing(), Move.UP_RIGHT.keepGoing(),
                Move.DOWN_LEFT.keepGoing(), Move.DOWN_RIGHT.keepGoing()),

        QUEEN(Move.UP.keepGoing(), Move.DOWN.keepGoing(), Move.LEFT.keepGoing(), Move.RIGHT.keepGoing(),
                Move.UP_LEFT.keepGoing(), Move.UP_RIGHT.keepGoing(),
                Move.DOWN_LEFT.keepGoing(), Move.DOWN_RIGHT.keepGoing());


        private List<Move> possibleMoves;

        Piece(final Move ...possibleMoves) {
            this.possibleMoves = Arrays.asList(possibleMoves);
        }

        public List<Move> getPossibleMoves() {
            return possibleMoves;
        }
    }

    static class Move {

        private static final Move LEFT = new Move(Position::canMoveLeft, Position::moveLeft);
        private static final Move RIGHT = new Move(Position::canMoveRight, Position::moveRight);
        private static final Move UP = new Move(Position::canMoveUp, Position::moveUp);
        private static final Move DOWN = new Move(Position::canMoveDown, Position::moveDown);
        private static final Move UP_LEFT = new Move(Position::canMoveUpLeft, Position::moveUpLeft);
        private static final Move UP_RIGHT = new Move(Position::canMoveUpRight, Position::moveUpRight);
        private static final Move DOWN_LEFT = new Move(Position::canMoveDownLeft, Position::moveDownLeft);
        private static final Move DOWN_RIGHT = new Move(Position::canMoveDownRight, Position::moveDownRight);

        private Predicate<Position> canMove;
        private Function<Position, Position> move;

        public Move(final Predicate<Position> canMove, final Function<Position, Position> move) {
            this.canMove = canMove;
            this.move = move;
        }

        public Optional<Position> perform(final Position currentPosition) {
            if (canMove.test(currentPosition)) {
                return Optional.of(move.apply(currentPosition));
            }
            return Optional.empty();
        }

        public Move keepGoing() {
            final Predicate<Position> andPredicate = (p) ->  {
                Position temp = p;
                while (canMove.test(temp)) {
                    temp = this.move.apply(temp);
                }
                return !p.equals(temp);
            };
            final Function<Position, Position> andMove = (p) -> {
                Position temp = p;
                while (canMove.test(temp)) {
                    temp = this.move.apply(temp);
                }
                return temp;
            };
            return new Move(andPredicate, andMove);
        }

        public Move then(final Move anotherMove) {
            final Predicate<Position> andPredicate = (p) -> canMove.test(p) && anotherMove.canMove.test(move.apply(p));
            final Function<Position, Position> andMove = (p) -> anotherMove.move.apply(this.move.apply(p));
            return new Move(andPredicate, andMove);
        }
    }

    static class Position {

        private static int A_ASCII = (int) 'A';
        private static int H_ASCII = (int) 'H';

        private char column;
        private int row;

        private Position(final char column, final int row) {
            this.column = column;
            this.row = row;
        }

        public static Position parse(final String stringPosition) {
            if (stringPosition == null || stringPosition.length() != 2) {
                throw new IllegalArgumentException("Incorrect Position " + stringPosition);
            }

            final char column = stringPosition.charAt(0);
            final char row = stringPosition.charAt(1);
            try {
                final int rowNumber = Integer.parseInt(String.valueOf(row));

                if (column < A_ASCII || column > H_ASCII || rowNumber < 1 || rowNumber > 8) {
                    throw new IllegalArgumentException("Incorrect Position " + stringPosition + " out of range");
                }

                return new Position(column, rowNumber);
            } catch (final NumberFormatException nfe) {
                throw new IllegalArgumentException("Incorrect Position " + stringPosition, nfe);
            }
        }

        boolean canMoveLeft() {
            return column > A_ASCII;
        }

        Position moveLeft() {
            return new Position((char) (column - 1), row);
        }

        Position moveRight() {
            return new Position((char) (column + 1), row);
        }

        Position moveUp() {
            return new Position(column, row + 1);
        }

        Position moveDown() {
            return new Position(column, row - 1);
        }

        Position moveUpLeft() {
            return this.moveUp().moveLeft();
        }

        Position moveUpRight() {
            return this.moveUp().moveRight();
        }

        Position moveDownLeft() {
            return this.moveDown().moveLeft();
        }

        Position moveDownRight() {
            return this.moveDown().moveRight();
        }

        boolean canMoveRight() {
            return column < H_ASCII;
        }

        boolean canMoveUp() {
            return row < 8;
        }

        boolean canMoveDown() {
            return row > 1;
        }

        boolean canMoveUpLeft() {
            return canMoveLeft() && canMoveUp();
        }

        boolean canMoveUpRight() {
            return canMoveUp() && canMoveRight();
        }

        boolean canMoveDownLeft() {
            return canMoveDown() && canMoveLeft();
        }

        boolean canMoveDownRight() {
            return canMoveDown() && canMoveRight();
        }

        @Override
        public String toString() {
            return String.valueOf(column) + row;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Position position = (Position) o;
            return column == position.column &&
                    row == position.row;
        }

        @Override
        public int hashCode() {
            return Objects.hash(column, row);
        }
    }

}



