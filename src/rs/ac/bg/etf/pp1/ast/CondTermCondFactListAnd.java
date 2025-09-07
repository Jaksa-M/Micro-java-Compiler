// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class CondTermCondFactListAnd extends CondTermCondFactList {

    private CondTermCondFactList CondTermCondFactList;
    private CondFact CondFact;

    public CondTermCondFactListAnd (CondTermCondFactList CondTermCondFactList, CondFact CondFact) {
        this.CondTermCondFactList=CondTermCondFactList;
        if(CondTermCondFactList!=null) CondTermCondFactList.setParent(this);
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
    }

    public CondTermCondFactList getCondTermCondFactList() {
        return CondTermCondFactList;
    }

    public void setCondTermCondFactList(CondTermCondFactList CondTermCondFactList) {
        this.CondTermCondFactList=CondTermCondFactList;
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTermCondFactList!=null) CondTermCondFactList.accept(visitor);
        if(CondFact!=null) CondFact.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTermCondFactList!=null) CondTermCondFactList.traverseTopDown(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTermCondFactList!=null) CondTermCondFactList.traverseBottomUp(visitor);
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondTermCondFactListAnd(\n");

        if(CondTermCondFactList!=null)
            buffer.append(CondTermCondFactList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondTermCondFactListAnd]");
        return buffer.toString();
    }
}
