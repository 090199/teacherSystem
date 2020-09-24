package kgc.cn.role.sys.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kgc.cn.role.sys.jdbc.JdbcUtil;
import kgc.cn.role.sys.model.Student;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class Student
 */
@WebServlet("/student")
public class StudentServlet extends HttpServlet {
//  增加成绩
  @Override
  protected void doPost(HttpServletRequest req,
                        HttpServletResponse resp)
          throws ServletException, IOException {
      String id = req.getParameter("id");
      String name = req.getParameter("name");
      String className = req.getParameter("className");
      String sql =
      "INSERT INTO student(id,name,className) VALUES(?,?,?)";
      int result =
              JdbcUtil.update(sql, id,name,className);
      PrintWriter writer = resp.getWriter();
      writer.print(result);
      writer.flush();
      writer.close();
  }
  
  //    查询学生
  @Override
  protected void doGet(HttpServletRequest req,
                       HttpServletResponse resp)
          throws ServletException, IOException {
      String sql = "SELECT id,name,className FROM student " +
              "WHERE 1=1";
      List<Object> params = new ArrayList<>();
      String className = req.getParameter("className");
      if(className != null && !"".equals(className)){
          sql += " AND className = ?";
          params.add(className);
      }
      
      List<Student> classes =
              JdbcUtil.search(sql,Student.class, params.toArray());
      JSONObject json = new JSONObject();
      json.put("classes", classes);
      resp.setCharacterEncoding("UTF-8");
      PrintWriter writer = resp.getWriter();
      writer.print(json);
      writer.flush();
      writer.close();
  }

}
