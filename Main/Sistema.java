package Main;

import functions.Functions;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;


public class Sistema {
    public static void main(String[] args) throws SQLException, IOException, ParseException {
        Functions functions = new Functions();
        functions.options();
        }
    }
