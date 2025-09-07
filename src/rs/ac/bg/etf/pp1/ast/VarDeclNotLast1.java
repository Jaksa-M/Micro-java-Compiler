// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class VarDeclNotLast1 extends VarDeclNotLast {

    private VarDeclAssignment VarDeclAssignment;

    public VarDeclNotLast1 (VarDeclAssignment VarDeclAssignment) {
        this.VarDeclAssignment=VarDeclAssignment;
        if(VarDeclAssignment!=null) VarDeclAssignment.setParent(this);
    }

    public VarDeclAssignment getVarDeclAssignment() {
        return VarDeclAssignment;
    }

    public void setVarDeclAssignment(VarDeclAssignment VarDeclAssignment) {
        this.VarDeclAssignment=VarDeclAssignment;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclAssignment!=null) VarDeclAssignment.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclAssignment!=null) VarDeclAssignment.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclAssignment!=null) VarDeclAssignment.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclNotLast1(\n");

        if(VarDeclAssignment!=null)
            buffer.append(VarDeclAssignment.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclNotLast1]");
        return buffer.toString();
    }
}
