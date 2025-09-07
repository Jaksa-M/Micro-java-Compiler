package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import rs.etf.pp1.symboltable.concepts.Struct;

public class ActParsCounter extends VisitorAdaptor {
	List<Struct> last_act_pars_list =  new ArrayList<>();
	Stack<List<Struct>> act_pars_lists_stack = new Stack<>();
	
	@Override
	public void visit(ActPar act_par) { // Adding parameter to the list
		act_pars_lists_stack.peek().add(act_par.getExpr().struct);
	}
	
	@Override
	public void visit(ActParsHelper act_pars_helper) {
		// For each nested call of function inside parameter, create a new list and push it to the stack
		act_pars_lists_stack.push(new ArrayList<>());
	}
	
	@Override
	public void visit(ActParsExist act_pars) { // In the end, we only need most top parameter list
		last_act_pars_list = act_pars_lists_stack.pop();
	}
	
	@Override
	public void visit(ActParsEmpty act_pars_empty) { // Case when there are no act pars
		if (act_pars_lists_stack.isEmpty() == false) {
			last_act_pars_list = act_pars_lists_stack.pop();
		}
	}
}
