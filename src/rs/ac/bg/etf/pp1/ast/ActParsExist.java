// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class ActParsExist extends ActPars {

    private ActParsHelper ActParsHelper;
    private ActPar ActPar;
    private ActParsExprList ActParsExprList;

    public ActParsExist (ActParsHelper ActParsHelper, ActPar ActPar, ActParsExprList ActParsExprList) {
        this.ActParsHelper=ActParsHelper;
        if(ActParsHelper!=null) ActParsHelper.setParent(this);
        this.ActPar=ActPar;
        if(ActPar!=null) ActPar.setParent(this);
        this.ActParsExprList=ActParsExprList;
        if(ActParsExprList!=null) ActParsExprList.setParent(this);
    }

    public ActParsHelper getActParsHelper() {
        return ActParsHelper;
    }

    public void setActParsHelper(ActParsHelper ActParsHelper) {
        this.ActParsHelper=ActParsHelper;
    }

    public ActPar getActPar() {
        return ActPar;
    }

    public void setActPar(ActPar ActPar) {
        this.ActPar=ActPar;
    }

    public ActParsExprList getActParsExprList() {
        return ActParsExprList;
    }

    public void setActParsExprList(ActParsExprList ActParsExprList) {
        this.ActParsExprList=ActParsExprList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActParsHelper!=null) ActParsHelper.accept(visitor);
        if(ActPar!=null) ActPar.accept(visitor);
        if(ActParsExprList!=null) ActParsExprList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActParsHelper!=null) ActParsHelper.traverseTopDown(visitor);
        if(ActPar!=null) ActPar.traverseTopDown(visitor);
        if(ActParsExprList!=null) ActParsExprList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActParsHelper!=null) ActParsHelper.traverseBottomUp(visitor);
        if(ActPar!=null) ActPar.traverseBottomUp(visitor);
        if(ActParsExprList!=null) ActParsExprList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParsExist(\n");

        if(ActParsHelper!=null)
            buffer.append(ActParsHelper.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActPar!=null)
            buffer.append(ActPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsExprList!=null)
            buffer.append(ActParsExprList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParsExist]");
        return buffer.toString();
    }
}
