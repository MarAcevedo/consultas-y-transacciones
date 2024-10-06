package com.cuenta.transacciones;

import java.sql.*;

public class Cuenta {

    private static final String SQL_TABLE_CREATE = "DROP TABLE IF EXISTS CUENTAS; " +
            "CREATE TABLE CUENTAS (" +
            "ID INT PRIMARY KEY," +
            "NUMERO_CUENTA INT NOT NULL," +
            "TITULAR VARCHAR(100) NOT NULL," +
            "SALDO NUMERIC(10,2) NOT NULL" +
            ")";

    private static final String SQL_INSERT = "INSERT INTO CUENTAS VALUES (?,?,?,?)";

    private static  final String SQL_SELECT = "SELECT * FROM CUENTAS";

    private static final String SQL_UPDATE = "UPDATE CUENTAS SET SALDO =? WHERE ID =?";

    public static void main (String[] args){

        Connection connection = null;

        try{

            connection = getConnection();

            Statement statement = connection.createStatement();

            statement.execute(SQL_TABLE_CREATE);

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);

            preparedStatement.setInt(1,1);
            preparedStatement.setInt(2,38);
            preparedStatement.setString(3,"Stefan");
            preparedStatement.setDouble(4,70000.69);

            preparedStatement.execute();

            ResultSet rs = statement.executeQuery(SQL_SELECT);

            while (rs.next()){

                System.out.println("Datos de cuenta y saldo: " +
                "\n -ID: " + rs.getInt(1)
                + "\n -Nº de cuenta: " + rs.getInt(2)
                + "\n -Titular de la cuenta: " + rs.getString(3)
                + "\n -Saldo total de la cuenta: " + rs.getDouble(4));
            }

            connection.setAutoCommit(false);

            PreparedStatement preparedStatementUpdate = connection.prepareStatement(SQL_UPDATE);

            preparedStatementUpdate.setDouble(1, 75000);
            preparedStatementUpdate.setInt(2, 1);

            preparedStatementUpdate.execute();

            int exception = 4/0;

            connection.commit();

            //buena practica
            connection.setAutoCommit(true);

            ResultSet rs1 = statement.executeQuery(SQL_SELECT);

            while (rs1.next()){
                System.out.println("Saldo de la cuenta con ID "
                        + rs1.getInt(1) +" y numero de cuenta "
                        + rs1.getInt(2)+" perteneciente a "
                        + rs1.getString(3) + " actualizado a "
                        + rs1.getDouble(4));

            }


        }catch (Exception e){
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
        }finally {
            try{
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        try {
            connection = getConnection();

            Statement statement = connection.createStatement();
            ResultSet rs2 = statement.executeQuery(SQL_SELECT);

            while (rs2.next()){
                System.out.println("Saldo de la cuenta con ID "
                        + rs2.getInt(1) +" y numero de cuenta "
                        + rs2.getInt(2)+" perteneciente a "
                        + rs2.getString(3) + " actualizado a "
                        + rs2.getDouble(4));

            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private static Connection getConnection() throws Exception{
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/ConsultasYTransacciones", "sa", "sa");
    }
}
