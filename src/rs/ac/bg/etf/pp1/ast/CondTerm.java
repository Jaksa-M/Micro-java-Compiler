// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class CondTerm implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private CondTermCondFactList CondTermCondFactList;

    public CondTerm (CondTermCondFactList CondTermCondFactList) {
        this.CondTermCondFactList=CondTermCondFactList;
        if(CondTermCondFactList!=null) CondTermCondFactList.setParent(this);
    }

    public CondTermCondFactList getCondTermCondFactList() {
        return CondTermCondFactList;
    }

    public void setCondTermCondFactList(CondTermCondFactList CondTermCondFactList) {
        this.CondTermCondFactList=CondTermCondFactList;
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
        if(CondTermCondFactList!=null) CondTermCondFactList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTermCondFactList!=null) CondTermCondFactList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTermCondFactList!=null) CondTermCondFactList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondTerm(\n");

        if(CondTermCondFactList!=null)
            buffer.append(CondTermCondFactList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondTerm]");
        return buffer.toString();
    }
}
