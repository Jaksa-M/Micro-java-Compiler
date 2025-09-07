// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class FormPars implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private FormParsTypeIdentList FormParsTypeIdentList;

    public FormPars (FormParsTypeIdentList FormParsTypeIdentList) {
        this.FormParsTypeIdentList=FormParsTypeIdentList;
        if(FormParsTypeIdentList!=null) FormParsTypeIdentList.setParent(this);
    }

    public FormParsTypeIdentList getFormParsTypeIdentList() {
        return FormParsTypeIdentList;
    }

    public void setFormParsTypeIdentList(FormParsTypeIdentList FormParsTypeIdentList) {
        this.FormParsTypeIdentList=FormParsTypeIdentList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsTypeIdentList!=null) FormParsTypeIdentList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsTypeIdentList!=null) FormParsTypeIdentList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsTypeIdentList!=null) FormParsTypeIdentList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormPars(\n");

        if(FormParsTypeIdentList!=null)
            buffer.append(FormParsTypeIdentList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormPars]");
        return buffer.toString();
    }
}
