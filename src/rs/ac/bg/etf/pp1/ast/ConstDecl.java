// generated with ast extension for cup
// version 0.8
// 21/6/2025 21:31:20


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private ConstValueAssignment ConstValueAssignment;
    private MoreSingleLineConstDeclarations MoreSingleLineConstDeclarations;

    public ConstDecl (Type Type, ConstValueAssignment ConstValueAssignment, MoreSingleLineConstDeclarations MoreSingleLineConstDeclarations) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ConstValueAssignment=ConstValueAssignment;
        if(ConstValueAssignment!=null) ConstValueAssignment.setParent(this);
        this.MoreSingleLineConstDeclarations=MoreSingleLineConstDeclarations;
        if(MoreSingleLineConstDeclarations!=null) MoreSingleLineConstDeclarations.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ConstValueAssignment getConstValueAssignment() {
        return ConstValueAssignment;
    }

    public void setConstValueAssignment(ConstValueAssignment ConstValueAssignment) {
        this.ConstValueAssignment=ConstValueAssignment;
    }

    public MoreSingleLineConstDeclarations getMoreSingleLineConstDeclarations() {
        return MoreSingleLineConstDeclarations;
    }

    public void setMoreSingleLineConstDeclarations(MoreSingleLineConstDeclarations MoreSingleLineConstDeclarations) {
        this.MoreSingleLineConstDeclarations=MoreSingleLineConstDeclarations;
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
        if(Type!=null) Type.accept(visitor);
        if(ConstValueAssignment!=null) ConstValueAssignment.accept(visitor);
        if(MoreSingleLineConstDeclarations!=null) MoreSingleLineConstDeclarations.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstValueAssignment!=null) ConstValueAssignment.traverseTopDown(visitor);
        if(MoreSingleLineConstDeclarations!=null) MoreSingleLineConstDeclarations.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstValueAssignment!=null) ConstValueAssignment.traverseBottomUp(visitor);
        if(MoreSingleLineConstDeclarations!=null) MoreSingleLineConstDeclarations.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstValueAssignment!=null)
            buffer.append(ConstValueAssignment.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MoreSingleLineConstDeclarations!=null)
            buffer.append(MoreSingleLineConstDeclarations.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl]");
        return buffer.toString();
    }
}
