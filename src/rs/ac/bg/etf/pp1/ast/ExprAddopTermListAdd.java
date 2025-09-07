// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class ExprAddopTermListAdd extends ExprAddopTermList {

    private ExprAddopTermList ExprAddopTermList;
    private Addop Addop;
    private Term Term;

    public ExprAddopTermListAdd (ExprAddopTermList ExprAddopTermList, Addop Addop, Term Term) {
        this.ExprAddopTermList=ExprAddopTermList;
        if(ExprAddopTermList!=null) ExprAddopTermList.setParent(this);
        this.Addop=Addop;
        if(Addop!=null) Addop.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public ExprAddopTermList getExprAddopTermList() {
        return ExprAddopTermList;
    }

    public void setExprAddopTermList(ExprAddopTermList ExprAddopTermList) {
        this.ExprAddopTermList=ExprAddopTermList;
    }

    public Addop getAddop() {
        return Addop;
    }

    public void setAddop(Addop Addop) {
        this.Addop=Addop;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprAddopTermList!=null) ExprAddopTermList.accept(visitor);
        if(Addop!=null) Addop.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprAddopTermList!=null) ExprAddopTermList.traverseTopDown(visitor);
        if(Addop!=null) Addop.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprAddopTermList!=null) ExprAddopTermList.traverseBottomUp(visitor);
        if(Addop!=null) Addop.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprAddopTermListAdd(\n");

        if(ExprAddopTermList!=null)
            buffer.append(ExprAddopTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Addop!=null)
            buffer.append(Addop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprAddopTermListAdd]");
        return buffer.toString();
    }
}
