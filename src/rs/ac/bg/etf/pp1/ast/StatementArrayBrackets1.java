// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class StatementArrayBrackets1 extends StatementArrayBrackets {

    private ElseHelper ElseHelper;
    private Statement Statement;

    public StatementArrayBrackets1 (ElseHelper ElseHelper, Statement Statement) {
        this.ElseHelper=ElseHelper;
        if(ElseHelper!=null) ElseHelper.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ElseHelper getElseHelper() {
        return ElseHelper;
    }

    public void setElseHelper(ElseHelper ElseHelper) {
        this.ElseHelper=ElseHelper;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ElseHelper!=null) ElseHelper.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ElseHelper!=null) ElseHelper.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ElseHelper!=null) ElseHelper.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementArrayBrackets1(\n");

        if(ElseHelper!=null)
            buffer.append(ElseHelper.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementArrayBrackets1]");
        return buffer.toString();
    }
}
