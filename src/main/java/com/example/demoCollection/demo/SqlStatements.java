package com.example.demoCollection.demo;

/**
 * Created by JWBlue.Liu on 15/12/1.
 */
public class SqlStatements {
    public static final String CREATE_TABLE = "CREATE TABLE";

    public static final String TABLE_NAME_STIKER = "Stiker";
    public static final String CREATE_STIKER =
            CREATE_TABLE + TABLE_NAME_STIKER +
                    "{" +
                    "id integer primary key autoincrement, " +
                    "author text, " +
                    "price real, " +
                    "pages " +
                    "} ";
}

