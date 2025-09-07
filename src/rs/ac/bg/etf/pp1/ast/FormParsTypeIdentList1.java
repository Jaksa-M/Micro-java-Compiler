// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class FormParsTypeIdentList1 extends FormParsTypeIdentList {

    private FormParsTypeName FormParsTypeName;
    private FormParsTypeIdentList FormParsTypeIdentList;

    public FormParsTypeIdentList1 (FormParsTypeName FormParsTypeName, FormParsTypeIdentList FormParsTypeIdentList) {
        this.FormParsTypeName=FormParsTypeName;
        if(FormParsTypeName!=null) FormParsTypeName.setParent(this);
        this.FormParsTypeIdentList=FormParsTypeIdentList;
        if(FormParsTypeIdentList!=null) FormParsTypeIdentList.setParent(this);
    }

    public FormParsTypeName getFormParsTypeName() {
        return FormParsTypeName;
    }

    public void setFormParsTypeName(FormParsTypeName FormParsTypeName) {
        this.FormParsTypeName=FormParsTypeName;
    }

    public FormParsTypeIdentList getFormParsTypeIdentList() {
        return FormParsTypeIdentList;
    }

    public void setFormParsTypeIdentList(FormParsTypeIdentList FormParsTypeIdentList) {
        this.FormParsTypeIdentList=FormParsTypeIdentList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsTypeName!=null) FormParsTypeName.accept(visitor);
        if(FormParsTypeIdentList!=null) FormParsTypeIdentList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsTypeName!=null) FormParsTypeName.traverseTopDown(visitor);
        if(FormParsTypeIdentList!=null) FormParsTypeIdentList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsTypeName!=null) FormParsTypeName.traverseBottomUp(visitor);
        if(FormParsTypeIdentList!=null) FormParsTypeIdentList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsTypeIdentList1(\n");

        if(FormParsTypeName!=null)
            buffer.append(FormParsTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsTypeIdentList!=null)
            buffer.append(FormParsTypeIdentList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsTypeIdentList1]");
        return buffer.toString();
    }
}
