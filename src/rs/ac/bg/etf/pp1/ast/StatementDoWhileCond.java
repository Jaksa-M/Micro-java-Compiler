// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class StatementDoWhileCond extends Statement {

    private DoHelper DoHelper;
    private Statement Statement;
    private WhileHelper WhileHelper;
    private Condition Condition;

    public StatementDoWhileCond (DoHelper DoHelper, Statement Statement, WhileHelper WhileHelper, Condition Condition) {
        this.DoHelper=DoHelper;
        if(DoHelper!=null) DoHelper.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileHelper=WhileHelper;
        if(WhileHelper!=null) WhileHelper.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
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

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoHelper!=null) DoHelper.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileHelper!=null) WhileHelper.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoHelper!=null) DoHelper.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileHelper!=null) WhileHelper.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoHelper!=null) DoHelper.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileHelper!=null) WhileHelper.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementDoWhileCond(\n");

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

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementDoWhileCond]");
        return buffer.toString();
    }
}
