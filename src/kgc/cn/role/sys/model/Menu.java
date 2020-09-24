package kgc.cn.role.sys.model;

import java.util.List;

public class Menu {

    private int id;

    private String icon;

    private String name;

    private String router;
    
    private int parent_id;
    
    public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	private List<Menu> children;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

	@Override
	public String toString() {
		return "Menu [id=" + id + ", icon=" + icon + ", name=" + name + ", router=" + router + ", parent_id="
				+ parent_id + ", children=" + children + "]";
	}

	
    
}
