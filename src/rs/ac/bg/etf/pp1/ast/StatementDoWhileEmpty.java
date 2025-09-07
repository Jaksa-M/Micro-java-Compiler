// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class StatementDoWhileEmpty extends Statement {

    private DoHelper DoHelper;
    private Statement Statement;
    private WhileHelper WhileHelper;

    public StatementDoWhileEmpty (DoHelper DoHelper, Statement Statement, WhileHelper WhileHelper) {
        this.DoHelper=DoHelper;
        if(DoHelper!=null) DoHelper.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileHelper=WhileHelper;
        if(WhileHelper!=null) WhileHelper.setParent(this);
    }

    public DoHelper getDoHelper() {
        return DoHelper;
    }

    public void setDoHelper(DoHelper DoHelper) {
        this.DoHelper=DoHelper;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public WhileHelper getWhileHelper() {
        return WhileHelper;
    }

    public void setWhileHelper(WhileHelper WhileHelper) {
        this.WhileHelper=WhileHelper;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoHelper!=null) DoHelper.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileHelper!=null) WhileHelper.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoHelper!=null) DoHelper.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileHelper!=null) WhileHelper.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoHelper!=null) DoHelper.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileHelper!=null) WhileHelper.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementDoWhileEmpty(\n");

        if(DoHelper!=null)
            buffer.append(DoHelper.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileHelper!=null)
            buffer.append(WhileHelper.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementDoWhileEmpty]");
        return buffer.toString();
    }
}
