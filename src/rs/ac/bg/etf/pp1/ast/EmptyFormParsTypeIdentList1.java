// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class EmptyFormParsTypeIdentList1 extends FormParsTypeIdentList {

    private FormParsTypeName FormParsTypeName;

    public EmptyFormParsTypeIdentList1 (FormParsTypeName FormParsTypeName) {
        this.FormParsTypeName=FormParsTypeName;
        if(FormParsTypeName!=null) FormParsTypeName.setParent(this);
    }

    public FormParsTypeName getFormParsTypeName() {
        return FormParsTypeName;
    }

    public void setFormParsTypeName(FormParsTypeName FormParsTypeName) {
        this.FormParsTypeName=FormParsTypeName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsTypeName!=null) FormParsTypeName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsTypeName!=null) FormParsTypeName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsTypeName!=null) FormParsTypeName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EmptyFormParsTypeIdentList1(\n");

        if(FormParsTypeName!=null)
            buffer.append(FormParsTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EmptyFormParsTypeIdentList1]");
        return buffer.toString();
    }
}
