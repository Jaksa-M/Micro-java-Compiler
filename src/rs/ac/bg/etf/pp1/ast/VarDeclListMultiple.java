// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListMultiple extends VarDeclList {

    private VarDeclNotLast VarDeclNotLast;
    private VarDeclList VarDeclList;

    public VarDeclListMultiple (VarDeclNotLast VarDeclNotLast, VarDeclList VarDeclList) {
        this.VarDeclNotLast=VarDeclNotLast;
        if(VarDeclNotLast!=null) VarDeclNotLast.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public VarDeclNotLast getVarDeclNotLast() {
        return VarDeclNotLast;
    }

    public void setVarDeclNotLast(VarDeclNotLast VarDeclNotLast) {
        this.VarDeclNotLast=VarDeclNotLast;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclNotLast!=null) VarDeclNotLast.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclNotLast!=null) VarDeclNotLast.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclNotLast!=null) VarDeclNotLast.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListMultiple(\n");

        if(VarDeclNotLast!=null)
            buffer.append(VarDeclNotLast.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListMultiple]");
        return buffer.toString();
    }
}
