import java.sql.*;

public class GetConnection {
        private static GetConnection single_instance = null;

        private GetConnection() {
        }

        public Connection getConnection() {
            try {
                String dbURL = "jdbc:oracle:thin:@localhost:1521:xe";
                String username = "system";
                String password = "oracle";
                return DriverManager.getConnection(dbURL, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static GetConnection getInstance()
        {
            if (single_instance == null)
                single_instance = new GetConnection();

            return single_instance;
        }
}