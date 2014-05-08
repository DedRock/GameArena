package server.mysql;

public class SqlErrorPrinter {
    public static void print(String message){
        System.out.println("Ошибка выполнения SQL-запроса: " + message);
    }
}
