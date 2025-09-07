// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class StatementIfElseError extends Statement {

    private Statement Statement;
    private StatementArrayBrackets StatementArrayBrackets;

    public StatementIfElseError (Statement Statement, StatementArrayBrackets StatementArrayBrackets) {
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.StatementArrayBrackets=StatementArrayBrackets;
        if(StatementArrayBrackets!=null) StatementArrayBrackets.setParent(this);
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public StatementArrayBrackets getStatementArrayBrackets() {
        return StatementArrayBrackets;
    }

    public void setStatementArrayBrackets(StatementArrayBrackets StatementArrayBrackets) {
        this.StatementArrayBrackets=StatementArrayBrackets;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Statement!=null) Statement.accept(visitor);
        if(StatementArrayBrackets!=null) StatementArrayBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(StatementArrayBrackets!=null) StatementArrayBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(StatementArrayBrackets!=null) StatementArrayBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementIfElseError(\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementArrayBrackets!=null)
            buffer.append(StatementArrayBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementIfElseError]");
        return buffer.toString();
    }
}
