package kgc.cn.role.sys.servlet;

import kgc.cn.role.sys.jdbc.JdbcUtil;
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

//根据发送请求的方法来执行对应的方法，这种风格我们称之为RESTFUL风格
@WebServlet("/score")
public class ScoreServlet extends HttpServlet {
//    查询
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        String sql = "SELECT id,username,course,score FROM score " +
                "WHERE 1=1";
        List<Object> params = new ArrayList<>();
        String username = req.getParameter("username");
        if(username != null && !"".equals(username)){
            sql += " AND username = ?";
            params.add(username);
        }
        String scoreFrom = req.getParameter("from");
        if(scoreFrom != null && !"".equals(scoreFrom)){
            sql += " AND score >= ?";
            params.add(scoreFrom);
        }
        String scoreTo = req.getParameter("to");
        if(scoreTo != null && !"".equals(scoreTo)){
            sql += " AND score <= ?";
            params.add(scoreTo);
        }
        List<Score> scores =
                JdbcUtil.search(sql,Score.class, params.toArray());
        JSONObject json = new JSONObject();
        json.put("scores", scores);
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.print(json);
        writer.flush();
        writer.close();
    }

//    增加成绩
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String course = req.getParameter("course");
        String score = req.getParameter("score");
        Double addScore =
         "".equals(score) ? null : Double.parseDouble(score);
        String sql =
        "INSERT INTO score(course,score,username) VALUES(?,?,?)";
        int result =
                JdbcUtil.update(sql, course, addScore, username);
        PrintWriter writer = resp.getWriter();
        writer.print(result);
        writer.flush();
        writer.close();
    }
//    修改
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String username = req.getParameter("username");
        String course = req.getParameter("course");
        String score = req.getParameter("score");
//        String sql = "UPDATE score SET username=?,course=?,score=? WHERE id=?";
        String sql = "UPDATE score SET username=?,course=?,score=? WHERE username=?";
        int result = JdbcUtil.update(sql, username,course,score,username);
        PrintWriter writer = resp.getWriter();
        writer.print(result);
        writer.flush();
        writer.close();
    }
//    删除
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String sql = "DELETE FROM score WHERE id=?";
        int result = JdbcUtil.update(sql, id);
        PrintWriter writer = resp.getWriter();
        writer.print(result);
        writer.flush();
        writer.close();
    }
}
