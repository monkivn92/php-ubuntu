package com.vuongpv.swan;
import java.sql.*;
public class DBHelper
{
    private String db_path = "";

    private Connection conn = null;
    private Statement stmt = null;

    public DBHelper(String db_path)
    {
        this.db_path = db_path;
        getInstance();
    }

    public Connection getInstance()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + this.db_path);
            //conn.setAutoCommit(false);
            this.stmt = conn.createStatement();
            this.stmt.setQueryTimeout(30);  // set timeout to 30 sec.

        }
        catch ( Exception e )
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        return this.conn;
    }

    public ResultSet execute(String query)
    {
        ResultSet rs = null;
        try
        {
            rs = stmt.executeQuery(query);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }

    public PreparedStatement getPrepareStatement(String query)
    {
        PreparedStatement pstmt=null;

        try
        {
            pstmt = this.conn.prepareStatement(query);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return pstmt;
    }

    public void close()
    {
        if(stmt != null)
        {
            try
            {
                stmt.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        if (conn != null)
        {
            try
            {
                conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

}
