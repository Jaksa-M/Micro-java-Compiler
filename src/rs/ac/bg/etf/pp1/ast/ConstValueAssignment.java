// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class ConstValueAssignment implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private ConstAssignedVal ConstAssignedVal;

    public ConstValueAssignment (String I1, ConstAssignedVal ConstAssignedVal) {
        this.I1=I1;
        this.ConstAssignedVal=ConstAssignedVal;
        if(ConstAssignedVal!=null) ConstAssignedVal.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public ConstAssignedVal getConstAssignedVal() {
        return ConstAssignedVal;
    }

    public void setConstAssignedVal(ConstAssignedVal ConstAssignedVal) {
        this.ConstAssignedVal=ConstAssignedVal;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstAssignedVal!=null) ConstAssignedVal.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstAssignedVal!=null) ConstAssignedVal.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstAssignedVal!=null) ConstAssignedVal.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstValueAssignment(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(ConstAssignedVal!=null)
            buffer.append(ConstAssignedVal.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstValueAssignment]");
        return buffer.toString();
    }
}
