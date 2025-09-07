// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class MethodSignatureFormPars extends MethodSignature {

    private MethodSignatureTypeName MethodSignatureTypeName;
    private FormPars FormPars;

    public MethodSignatureFormPars (MethodSignatureTypeName MethodSignatureTypeName, FormPars FormPars) {
        this.MethodSignatureTypeName=MethodSignatureTypeName;
        if(MethodSignatureTypeName!=null) MethodSignatureTypeName.setParent(this);
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
    }

    public MethodSignatureTypeName getMethodSignatureTypeName() {
        return MethodSignatureTypeName;
    }

    public void setMethodSignatureTypeName(MethodSignatureTypeName MethodSignatureTypeName) {
        this.MethodSignatureTypeName=MethodSignatureTypeName;
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodSignatureTypeName!=null) MethodSignatureTypeName.accept(visitor);
        if(FormPars!=null) FormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodSignatureTypeName!=null) MethodSignatureTypeName.traverseTopDown(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodSignatureTypeName!=null) MethodSignatureTypeName.traverseBottomUp(visitor);
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodSignatureFormPars(\n");

        if(MethodSignatureTypeName!=null)
            buffer.append(MethodSignatureTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodSignatureFormPars]");
        return buffer.toString();
    }
}
