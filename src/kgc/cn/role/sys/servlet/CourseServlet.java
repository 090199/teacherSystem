package kgc.cn.role.sys.servlet;



import kgc.cn.role.sys.jdbc.JdbcUtil;
import kgc.cn.role.sys.model.Course;
import kgc.cn.role.sys.model.Score;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class CourseServlet
 */
@WebServlet("/course")
public class CourseServlet extends HttpServlet {

//  增加成绩
  @Override
  protected void doPost(HttpServletRequest req,
                        HttpServletResponse resp)
          throws ServletException, IOException {
      String number = req.getParameter("number");
      String name = req.getParameter("name");
      String times =req.getParameter("times");
      String score = req.getParameter("score");
//      Double addCourse =
//       "".equals(course) ? null : Double.parseDouble(course);
      String sql =
      "INSERT INTO course(number,name,times,score) VALUES(?,?,?,?)";
      int result =
              JdbcUtil.update(sql, number, name, times, score);
      PrintWriter writer = resp.getWriter();
      writer.print(result);
      writer.flush();
      writer.close();
  }

	//查询
	@Override
	protected void doGet(HttpServletRequest req,
	                     HttpServletResponse resp)
	        throws ServletException, IOException {
		
	    String sql = "SELECT number,name,times,score FROM course " +
	            "WHERE 1=1";
	    List<Object> params = new ArrayList<>();
	    String number = req.getParameter("number");
	    if(number != null && !"".equals(number)){
	        sql += " AND number = ?";
	        params.add(number);
	    }
	    String name = req.getParameter("name");
	    if(name != null && !"".equals(name)){
	        sql += " AND name=?";
	        params.add(name);
	    }
	    String score = req.getParameter("score");
	    if(score != null && !"".equals(score)){
	        sql += " AND score = ?";
	        params.add(score);
	    }
	    List<Course> courses =
	            JdbcUtil.search(sql,Course.class, params.toArray());
	    JSONObject json = new JSONObject();
	    json.put("courses", courses);
	    resp.setCharacterEncoding("UTF-8");
	    PrintWriter writer = resp.getWriter();
	    writer.print(json);
	    writer.flush();
	    writer.close();
	}
//  修改
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String number = req.getParameter("number");
      String name = req.getParameter("name");
      String times = req.getParameter("times");
      String score = req.getParameter("score");
      String sql = "UPDATE course SET number=?,name=?,times=?,score=? WHERE number=?";
      int result = JdbcUtil.update(sql, number,name,times,score,number);
      PrintWriter writer = resp.getWriter();
      writer.print(result);
      writer.flush();
      writer.close();
  }
  
  // 删除
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String number = req.getParameter("number");
      String sql = "DELETE FROM course WHERE number=?";
      int result = JdbcUtil.update(sql, number);
      PrintWriter writer = resp.getWriter();
      writer.print(result);
      writer.flush();
      writer.close();
  }
}
