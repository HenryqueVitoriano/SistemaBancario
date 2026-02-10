package Main;

import functions.Functions;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;


public class Sistema {
    public static void main(String[] args) throws SQLException, IOException, ParseException {
        Functions RedBank = new Functions();
        RedBank.options();
        }
    }
