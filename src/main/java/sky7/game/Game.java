package sky7.game;

import com.badlogic.gdx.math.Vector2;
import sky7.Client.Client;
import sky7.board.IBoard;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.robots.RobotTile;
import sky7.card.ICard;
import sky7.card.ProgramCard;
import sky7.host.Host;

import java.util.*;

public class Game implements IGame {

    private Host host;
    private Client client;
    private static final int NR_OF_PHASES = 5;
    private IBoard board;

    /**
     * The construct for a game engine on host.
     *
     * @param host
     * @param board
     */
    public Game(Host host, IBoard board) {
        this.host = host;
        this.board = board;
    }

    /**
     * The constructor for a game engine on client.
     *
     * @param client
     * @param board
     */
    public Game(Client client, IBoard board) {
        this.client = client;
        this.board = board;
    }


    @Override
    public void process(HashMap<Integer, ArrayList<ICard>> playerRegistrys) {
        Queue<Queue<Pair>> allPhases = findPlayerSequence(playerRegistrys);
        for (Queue<Pair> phase : allPhases) {
            for (Pair player : phase) {
                tryToMove(player);
                expressConveyor();
                normalAndExpressConveyor();
                activatePushers();
                activateCogwheels();
                activateLasers();
                placeMarkers();
                flags();
                if (foundWinner()) break;
            }
        }
        //after 5th phaze
        repairRobotsOnRepairSite();

        if (host != null) {
            host.finishedProcessing(board);
        } else client.finishedProcessing(board);
    }

    private boolean foundWinner() {
        //TODO winner if the clients id, can be found from RoboTile.playerNr.
        /*int winner = 0;
        if (host != null) {
            host.setWinner(0);
            return true;
        } else return true;*/
        return false;
    }

    private void flags() {
        // check winning condition.
        //TODO

        render();
    }

    private void placeMarkers() {
        render();
        //REPAIR SITES: A robot on a repair site places their Archive marker (where they respawn) there. (They do NOT repair.)
    }

    private void repairRobotsOnRepairSite() {
        // REPAIR SITES: A robot on a repair site repairs 1 point of damage. A robot on a double tool repair site also draws 1 Option card.
        //TODO
        render();
    }

    private void activateCogwheels() {
        //TODO
        render();
    }

    private void activateLasers() {
        //TODO
        render();
    }

    private void activatePushers() {
        //TODO
        render();
    }

    private void normalAndExpressConveyor() {
        //TODO
        render();
    }

    private void expressConveyor() {
        // TODO check if this robot is on a conveyor belt and there is another robot in front that is also on the convoyer belt
        render();
    }

    /**
     * Try to move a robot a step.
     *
     * @param player a Pair containing the playerId and ProgramCard
     */
    private void tryToMove(Pair player) {
        if (player.card.moveType()) {
            int steps = player.card.move();
            while (steps > 0) {
                if (canGo(player.id)) {
                    movePlayer(player.id, board.getRobots()[player.id].getOrientation());
                    steps--;
                    render();
                }
            }
        } else {
            rotatePlayer(player);
            render();
        }

    }

    private void movePlayer(int player, DIRECTION dir) {
        // move player 1 step in direction dir
        Vector2 ahead = board.getDestination(board.getRobotPos()[player], dir, 1);

        if (board.containsPosition(ahead)) {
            for (ICell cell : board.getCell(ahead)) {
                if (cell instanceof RobotTile) {
                    int newPlayer = ((RobotTile) cell).getId();
                    movePlayer(newPlayer, dir);
                }
            }
            board.moveRobot(player, dir);
        } else {
            // TODO move robot out of the board. currently just stops.
        }
    }

    private void rotatePlayer(Pair player) {
        board.rotateRobot(player.id, player.card.rotate());
    }

    private boolean canGo(Integer id) {

        Vector2 here = board.getRobotPos()[id];
        RobotTile robot = board.getRobots()[id];
        DIRECTION movementDir = robot.getOrientation();

        if (facingWall(here, movementDir)) return false;

        if (occupiedNextCell(here, movementDir)) return false;

        return true;
    }

    /**
     * Check if next cell can be moved into
     *
     * @param from      the position the robot wants to move from.
     * @param direction the direction the robot want to move.
     * @return true if the
     */
    private boolean occupiedNextCell(Vector2 from, DIRECTION direction) {
        Vector2 ahead = board.getDestination(from, direction, 1);

        // if we are facing a wall, then we cannot move a robot.
        if (!facingWall(ahead, direction.reverse()))

            // check if there is an immovable robot in front
            if (board.containsPosition(ahead))
                for (ICell cell : board.getCell(ahead)) {
                    if (cell instanceof RobotTile) {
                        return occupiedNextCell(ahead, direction);
                    }

                }
        return false;
    }

    /**
     * check if current cell does not hinder movement in the direction {@param direction}. eks if
     *
     * @param pos       the current position
     * @param direction the direction to check
     * @return true if there is no obstacle, such as a wall in the {@param direction}
     */
    private boolean facingWall(Vector2 pos, DIRECTION direction) {
        // TODO check if there is a wall facing movement direction in the current cell
        System.out.println(board.getWidth());
        System.out.println(board.getHeight());
        System.out.println(pos);
        if (board.containsPosition(pos))
            for (ICell cell : board.getCell(pos)) {
                if (cell instanceof Wall && ((Wall) cell).getDirection() == direction) {
                    return true;
                }
            }
        return false;
    }

    @Override
    public void render() {
        // TODO call render if this game belongs to a client, else ignore.
        if (client != null) {
            client.updateBoard(board);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Queues all the phases, where each phase contains one card from each player sorted by the cards priority
     *
     * @param playerRegistries the registry of all players
     * @return All 5 phases queued, where each phase is a queue of Pair containing player Id and A card.
     */
    private Queue<Queue<Pair>> findPlayerSequence(HashMap<Integer, ArrayList<ICard>> playerRegistries) {
        // TODO make test for this.

        Queue<Queue<Pair>> phases = new LinkedList<>();
        for (int i = 0; i < NR_OF_PHASES; i++) {
            Queue<Pair> phase = new LinkedList<>();
            for (Integer playerID : playerRegistries.keySet()) {
                phase.add(new Pair(playerID, (ProgramCard) playerRegistries.get(playerID).get(i)));
            }
            phases.add(phase);
        }
        return phases;
    }


    private class Pair implements Comparable<Pair> {
        private Integer id;
        private ProgramCard card;

        private Pair(Integer id, ProgramCard card) {
            this.id = id;
            this.card = card;
        }

        @Override
        public int compareTo(Pair other) {
            return Integer.compare(card.priorityN(), other.card.priorityN());
        }
    }
}
