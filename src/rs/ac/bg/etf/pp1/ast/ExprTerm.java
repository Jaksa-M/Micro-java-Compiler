// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class ExprTerm extends Expr {

    private ExprAddopTermList ExprAddopTermList;

    public ExprTerm (ExprAddopTermList ExprAddopTermList) {
        this.ExprAddopTermList=ExprAddopTermList;
        if(ExprAddopTermList!=null) ExprAddopTermList.setParent(this);
    }

    public ExprAddopTermList getExprAddopTermList() {
        return ExprAddopTermList;
    }

    public void setExprAddopTermList(ExprAddopTermList ExprAddopTermList) {
        this.ExprAddopTermList=ExprAddopTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprAddopTermList!=null) ExprAddopTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprAddopTermList!=null) ExprAddopTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprAddopTermList!=null) ExprAddopTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprTerm(\n");

        if(ExprAddopTermList!=null)
            buffer.append(ExprAddopTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprTerm]");
        return buffer.toString();
    }
}
