// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class ConditionCondTermListOr extends ConditionCondTermList {

    private ConditionCondTermList ConditionCondTermList;
    private CondTerm CondTerm;

    public ConditionCondTermListOr (ConditionCondTermList ConditionCondTermList, CondTerm CondTerm) {
        this.ConditionCondTermList=ConditionCondTermList;
        if(ConditionCondTermList!=null) ConditionCondTermList.setParent(this);
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
    }

    public ConditionCondTermList getConditionCondTermList() {
        return ConditionCondTermList;
    }

    public void setConditionCondTermList(ConditionCondTermList ConditionCondTermList) {
        this.ConditionCondTermList=ConditionCondTermList;
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConditionCondTermList!=null) ConditionCondTermList.accept(visitor);
        if(CondTerm!=null) CondTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionCondTermList!=null) ConditionCondTermList.traverseTopDown(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionCondTermList!=null) ConditionCondTermList.traverseBottomUp(visitor);
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionCondTermListOr(\n");

        if(ConditionCondTermList!=null)
            buffer.append(ConditionCondTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionCondTermListOr]");
        return buffer.toString();
    }
}
