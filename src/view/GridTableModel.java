package view;

import battle.game.players.Square;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 * The type Grid table model.
 */
public class GridTableModel extends AbstractTableModel {
    /**
     * The constant PATH.
     */
    private static final String PATH = "/rss/";
    /**
     * The Grid.
     */
    private Square[][] grid;
    /**
     * The Image busy.
     */
    private String imageBusy = "busy.png";
    /**
     * The Image busy and hit.
     */
    private String imageBusyAndHit = "busyHit.png";
    /**
     * The Image free.
     */
    private String imageFree = "free.png";
    /**
     * The Image hit and free.
     */
    private String imageHitAndFree = "freeHit.png";
    /**
     * The No of rows.
     */
    private int noOfRows, /**
     * The No of cols.
     */
    noOfCols;

    /**
     * Instantiates a new Grid table model.
     *
     * @param grid the grid
     */
    /*
     * Constructor
     * @param grid : the table to display
     */
    public GridTableModel(Square[][] grid) {
        this.grid = grid;
        noOfRows = this.grid.length;
        noOfCols = this.grid[0].length;
    }

    // Implementing the tree abstract methods:

    /**
     * Gets row count.
     *
     * @return the row count
     */
    public int getRowCount() {
        return (noOfRows);
    }

    /**
     * Gets column count.
     *
     * @return the column count
     */
    public int getColumnCount() {
        return (noOfCols);
    }

    /**
     * Gets value at.
     *
     * @param r the r
     * @param c the c
     * @return the value at
     */
    public Object getValueAt(int r, int c) {
        Object result = new Object();
        Square sq = grid[r][c];
        if ((sq.isFree()) && (!sq.isHit())) result = new ImageIcon(GridTableModel.class.getResource(PATH + imageFree));
        else if (sq.isFree() && (sq.isHit())) result = new ImageIcon(GridTableModel.class.getResource(PATH + imageHitAndFree));
        else if (!sq.isFree() && (!sq.isHit())) result = new ImageIcon(GridTableModel.class.getResource(PATH + imageBusy));
        else result = new ImageIcon(GridTableModel.class.getResource(PATH + imageBusyAndHit));
        return result;
    }

    /**
     * get the name of the column
     *
     * @param c : the number of the column
     * @return a String for the name of the column
     */
    public String getColumnName(int c) {
        return (new Integer(c).toString());
    }

    /**
     * get the class of the object at column c
     *
     * @param c : the number of the column
     * @return the class of the object at column c
     */
    public Class getColumnClass(int c) {
        return this.getValueAt(0, c).getClass();
    }
}