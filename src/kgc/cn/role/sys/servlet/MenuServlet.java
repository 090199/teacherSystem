package kgc.cn.role.sys.servlet;

import kgc.cn.role.sys.jdbc.JdbcUtil;
import kgc.cn.role.sys.model.Menu;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/getUserMenu")
public class MenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        如何获取当前登录的用户？
//        1.先获取session
        HttpSession session = req.getSession();
//        2.从session取出当前登录的用户（因为登录的时候已经存入）
        String username = (String) session.getAttribute("username");
        String sql = "SELECT d.id,d.`name`,d.icon,d.router FROM " +
                "user_role a,role b,role_menu c,menu d " +
                "WHERE a.role_id = b.id AND b.id = c.role_id " +
                "AND c.menu_id = d.id AND a.username = ? " +
                "AND d.parent_id = ?";
//        查询一级菜单
        List<Menu> menus =
                JdbcUtil.search(sql,Menu.class,username, 0);
//        循环查找子菜单
        for(int i=0; i<menus.size(); i++){
            Menu m = menus.get(i);
            int id = m.getId();
            System.out.println(id);
            List<Menu> children =
                    JdbcUtil.search(sql,Menu.class,username, id);
            m.setChildren(children);
            children.forEach(System.out::println);
        }
//        构建JSON格式的数据
        JSONObject json = new JSONObject();
        json.put("menus", menus);
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
//        将JSON格式的数据传回客户端
        writer.print(json);
        writer.flush();
        writer.close();
    }
}
