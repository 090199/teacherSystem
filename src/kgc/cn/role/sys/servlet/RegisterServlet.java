package kgc.cn.role.sys.servlet;

import kgc.cn.role.sys.jdbc.JdbcUtil;
import kgc.cn.role.sys.md5.EncryptUtil;
import kgc.cn.role.sys.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/createUser")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        从请求信息中获取用户名
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");
//        先设置好响应使用的字符集编码为UTF-8，防止出现中文乱码
        resp.setCharacterEncoding("UTF-8");
//        从响应对象中获取一个写的流，写的目的地就是客户端的浏览器
        PrintWriter writer = resp.getWriter();
        if(password == null || confirm == null
                || !confirm.equals(password)){
//            返回信息 两次密码输入不一致
            writer.print("两次密码输入不一致");
//            强制将信息从内存中刷出去
            writer.flush();
//            响应结束，连接关闭
            writer.close();
            return;
        }
        String sql ="SELECT username,password FROM user WHERE username = ?";
        List<User> users = JdbcUtil.search(sql, User.class, username);
        if(users.isEmpty()){
            sql = "INSERT INTO user(username,password) VALUES(?,?)";
            String pw = EncryptUtil.encrypt(password);
            int result = JdbcUtil.update(sql, username, pw);
            if(result == 1){
//                保存成功的情况下需要给该用户一个默认的角色
                sql = "INSERT INTO user_role(username,role_id) VALUES(?, ?)";
                JdbcUtil.update(sql, username, 3);
                writer.print("注册成功");
            } else {
                writer.print("注册失败");
            }
            writer.flush();
            writer.close();
        } else {
            writer.print("账号已被注册");
            writer.flush();
            writer.close();
        }
    }
}
