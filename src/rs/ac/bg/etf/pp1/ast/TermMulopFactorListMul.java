// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class TermMulopFactorListMul extends TermMulopFactorList {

    private TermMulopFactorList TermMulopFactorList;
    private Mulop Mulop;
    private Factor Factor;

    public TermMulopFactorListMul (TermMulopFactorList TermMulopFactorList, Mulop Mulop, Factor Factor) {
        this.TermMulopFactorList=TermMulopFactorList;
        if(TermMulopFactorList!=null) TermMulopFactorList.setParent(this);
        this.Mulop=Mulop;
        if(Mulop!=null) Mulop.setParent(this);
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
    }

    public TermMulopFactorList getTermMulopFactorList() {
        return TermMulopFactorList;
    }

    public void setTermMulopFactorList(TermMulopFactorList TermMulopFactorList) {
        this.TermMulopFactorList=TermMulopFactorList;
    }

    public Mulop getMulop() {
        return Mulop;
    }

    public void setMulop(Mulop Mulop) {
        this.Mulop=Mulop;
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TermMulopFactorList!=null) TermMulopFactorList.accept(visitor);
        if(Mulop!=null) Mulop.accept(visitor);
        if(Factor!=null) Factor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TermMulopFactorList!=null) TermMulopFactorList.traverseTopDown(visitor);
        if(Mulop!=null) Mulop.traverseTopDown(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TermMulopFactorList!=null) TermMulopFactorList.traverseBottomUp(visitor);
        if(Mulop!=null) Mulop.traverseBottomUp(visitor);
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TermMulopFactorListMul(\n");

        if(TermMulopFactorList!=null)
            buffer.append(TermMulopFactorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mulop!=null)
            buffer.append(Mulop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TermMulopFactorListMul]");
        return buffer.toString();
    }
}
