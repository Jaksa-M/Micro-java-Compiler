// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class Condition implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private ConditionCondTermList ConditionCondTermList;

    public Condition (ConditionCondTermList ConditionCondTermList) {
        this.ConditionCondTermList=ConditionCondTermList;
        if(ConditionCondTermList!=null) ConditionCondTermList.setParent(this);
    }

    public ConditionCondTermList getConditionCondTermList() {
        return ConditionCondTermList;
    }

    public void setConditionCondTermList(ConditionCondTermList ConditionCondTermList) {
        this.ConditionCondTermList=ConditionCondTermList;
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
        if(ConditionCondTermList!=null) ConditionCondTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionCondTermList!=null) ConditionCondTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionCondTermList!=null) ConditionCondTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Condition(\n");

        if(ConditionCondTermList!=null)
            buffer.append(ConditionCondTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Condition]");
        return buffer.toString();
    }
}
