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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
//    处理GET请求
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
//    }
//    处理POST请求
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String sql ="SELECT username,password FROM user WHERE username = ?";
        List<User> users = JdbcUtil.search(sql, User.class, username);
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        if(users.isEmpty()){
            writer.print("账号不存在，请先注册");
            writer.flush();
            writer.close();
            return;
        }
        if(users.size() > 1){
            writer.print("系统异常，请联系管理员");
        } else {
            User user = users.get(0);
            String pw = EncryptUtil.encrypt(password);
            if(pw.equals(user.getPassword())){
//                将当前登录用户的用户名存入session中，方便后面获取登录用户信息
                req.getSession().setAttribute("username", username);
                writer.print("登录成功");
            }else {
                writer.print("账号或密码错误");
            }
            writer.flush();
            writer.close();
        }
    }
}
