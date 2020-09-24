package kgc.cn.role.sys.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtil {
//    数据库地址
//    private static final String url = "jdbc:mysql://localhost:3306/shixun?serverTimezone=Asia/Shanghai";//http://www.baidu.com

	private static final String url = "jdbc:mysql://localhost:3306/shixun?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";
    private static final String username = "root";
    private static final String password = "root";

    public static <T> List<T> search(String sql, Class<T> cls, Object...params){
        List<T> dataList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql);
            for(int i=0; i<params.length; i++){
                ps.setObject(i+1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()){
//                通过调用类的无参构造创建对象
                T t = cls.getConstructor().newInstance();
                for(int i=1; i<=rsmd.getColumnCount(); i++){
//                    获取指定位置的列的名称，注意：列名称必须与类中定义的字段名保持一致
                    String label = rsmd.getColumnLabel(i);
//                    根据列名去字节码中寻找对应的字段
                    Field field = cls.getDeclaredField(label);
//                    首字母变大写
                    String firstLetter = label.substring(0,1).toUpperCase();
                    String methodName = "set" + firstLetter + label.substring(1);
//                    根据方法名和参数列表类型去字节码中查找对应的方法
                    Method m = cls.getDeclaredMethod(methodName, field.getType());
//                    调用方法为对象注入值
                    m.invoke(t, rs.getObject(label));
                }
                dataList.add(t);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static int update(String sql, Object...params){
        int result = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql);
            for(int i=0; i<params.length; i++){
                ps.setObject(i+1, params[i]);
            }
//            执行更新操作，得到一个更新的结果：返回的是受影响的行数
            result = ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
