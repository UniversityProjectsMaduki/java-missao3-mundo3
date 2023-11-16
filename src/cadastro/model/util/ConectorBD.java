package cadastro.model.util;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConectorBD {
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                String user = props.getProperty("user");
                String password = props.getProperty("password");
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Properties loadProperties() {
        try (InputStream is = ConectorBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            if (is != null) {
                props.load(is);
            } else {
                throw new FileNotFoundException("Arquivo db.properties não encontrado no classpath.");
            }
            return props;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar as propriedades do banco de dados: " + e.getMessage(), e);
        }
    }
}
