package com.playware.exercise2.beergame;

public class GameTable {
    final int columns = 3;
    final int rows = 10;
    TableElement[][] table;

    public GameTable(){
        this.table =  new TableElement[this.rows][this.columns];
    }
}
