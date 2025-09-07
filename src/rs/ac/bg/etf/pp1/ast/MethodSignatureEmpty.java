// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class MethodSignatureEmpty extends MethodSignature {

    private MethodSignatureTypeName MethodSignatureTypeName;

    public MethodSignatureEmpty (MethodSignatureTypeName MethodSignatureTypeName) {
        this.MethodSignatureTypeName=MethodSignatureTypeName;
        if(MethodSignatureTypeName!=null) MethodSignatureTypeName.setParent(this);
    }

    public MethodSignatureTypeName getMethodSignatureTypeName() {
        return MethodSignatureTypeName;
    }

    public void setMethodSignatureTypeName(MethodSignatureTypeName MethodSignatureTypeName) {
        this.MethodSignatureTypeName=MethodSignatureTypeName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodSignatureTypeName!=null) MethodSignatureTypeName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodSignatureTypeName!=null) MethodSignatureTypeName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodSignatureTypeName!=null) MethodSignatureTypeName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodSignatureEmpty(\n");

        if(MethodSignatureTypeName!=null)
            buffer.append(MethodSignatureTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodSignatureEmpty]");
        return buffer.toString();
    }
}
