package server.mysql;

public class DbConnectorSingleton {
    static MySqlDbConnector instance;

    public static MySqlDbConnector getInstance() {
        if (instance == null){
            instance = new MySqlDbConnector();
            try {
                instance.connectDB();
            } catch (Exception e) {
                System.out.println("Ошибка подключения к базе данных");
            }
        }
        return instance;
    }
}
