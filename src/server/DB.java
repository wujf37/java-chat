package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DB {
	static String DBUrl="jdbc:mysql://localhost:3306/javadb?charset=utf8mb4&parseTime=True&loc=Local";
	static String DBUser="root";
	static String DBPassword="123456";
	private Connection con;
	public DB(){
		try  // 数据库装载
	    {
        	Class.forName("com.mysql.cj.jdbc.Driver");
	    }
		catch(Exception e) 
		{     System.out.println("数据库装载失败: " + e.toString());	
		}
		try
        {   con = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            
        }
        catch(SQLException s)  
	    {   System.out.println("数据库操作失败: " + s.toString() + s.getErrorCode() + "  " + s.getSQLState());			
		}
	}
    public void DBClose()
    {   try
        {   con.close();
        }
        catch(SQLException s)  
	    {   System.out.println("数据库操作失败: " + s.toString() + s.getErrorCode() + "  " + s.getSQLState());			
		}
    }
    public Connection getcon() {
    	return con;
    }
}
