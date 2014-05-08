package server.mysql;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 06.05.14
 * Time: 9:55
 * To change this template use File | Settings | File Templates.
 */
public class MySqlDbConnector {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gamearena";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "password";

    private Statement sqlQuery;
    private Connection conn;

    /**
     * Connect to DataBase MySql
     *
     * @throws ClassNotFoundException
     */
    public void connectDB(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            sqlQuery = conn.createStatement();
            // Set manual control of data changes
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException e){
            System.out.println("Не найден класс: com.mysql.jdbc.Driver ");
        } catch (SQLException e){
            System.out.println("Не удалось подключить к базе данных");
        }
    }

    /**
     * Execute user's SQL-query with result data
     *
     * @param query - text of query
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet executeQuery(String query)throws SQLException{
        return sqlQuery.executeQuery(query);
    }

    /**
     * Execute user's SQL-query without result data
     * @param query
     * @return
     * @throws SQLException
     */
    public boolean execute(String query) throws SQLException {
        return sqlQuery.execute(query);
    }

    /**
     * Commit database changes
     */
    public  void commit(){
        try {
            conn.commit();
        } catch (SQLException e) {
            System.out.println("SQL commit error");
        }
    }

    /**
     * Rollback database changes
     */
    public void rollback(){
        try {
            conn.rollback();
        } catch (SQLException e) {
            System.out.println("SQL rollback error");
        }
    }
}
