package rs.ac.bg.etf.pp1;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {
	Logger log = Logger.getLogger(getClass());
	private boolean error_detected = false;
	int printCallCount = 0;
	private Obj curr_method = null;
	boolean returnFound = false;
	private Obj prog_name;
	private Struct curr_type;
	private int constant;
	private Struct constant_type;
	private Struct bool_type = Tab.find("bool").getType();
	private Struct set_type = Tab.find("set").getType();
	private Obj main_method;
	private boolean return_check; // set to true when we have returned value in functions;
	private int while_loop_counter = 0; // counts in how many nested while loops we are currently in
	int n_vars;
	
	//------------------------ ERROR handling ------------------------
	public void report_error(String message, SyntaxNode info) {
		error_detected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public boolean passed() {
		return !error_detected;
	}
	
	SemanticAnalyzer() {
        // Adding add method to symbol table
        Obj add_obj = new Obj(Obj.Meth, "add", Tab.noType, 0, 2);
		Tab.openScope();
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "s", set_type, 0, 1));
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "val", Tab.intType, 0, 1));
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "size", Tab.intType, 0, 1)); // Hidden parameter
        add_obj.setLocals(Tab.currentScope.getLocals());
        Tab.closeScope();
        Tab.currentScope.addToLocals(add_obj);
        
        // Adding addAll method to symbol table
        Obj addAll_obj = new Obj(Obj.Meth, "addAll", Tab.noType, 0, 2);
		Tab.openScope();
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "s", set_type, 0, 1));
        Struct array_struct = new Struct(Struct.Array, Tab.intType);
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "arr", array_struct, 0, 1));
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "temp1", Tab.intType, 0, 1)); // Hidden parameter
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "temp2", Tab.intType, 0, 1)); // Hidden parameter
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "temp3", Tab.intType, 0, 1)); // Hidden parameter
        addAll_obj.setLocals(Tab.currentScope.getLocals());
        Tab.closeScope();
        Tab.currentScope.addToLocals(addAll_obj);
        
        // Adding union method to symbol table
        Obj union_obj = new Obj(Obj.Meth, "union", Tab.noType, 0, 3);
		Tab.openScope();
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "s1", set_type, 0, 1));
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "s2", set_type, 0, 1));
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "s3", set_type, 0, 1));
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "temp1", Tab.intType, 0, 1)); // Hidden parameter
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "temp2", Tab.intType, 0, 1)); // Hidden parameter
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "temp3", Tab.intType, 0, 1)); // Hidden parameter
        union_obj.setLocals(Tab.currentScope.getLocals());
        Tab.closeScope();
        Tab.currentScope.addToLocals(union_obj);
        
        // Adding map method to symbol table
        Obj map_obj = new Obj(Obj.Meth, "map", Tab.intType, 0, 2);
		Tab.openScope();
		//Obj func_type = new Obj(Obj.Meth, "func_type", Tab.intType, 0, 1);
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "func", Tab.intType, 0, 1));
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "arr", array_struct, 0, 1));
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "temp1", Tab.intType, 0, 1)); // Hidden parameter
        Tab.currentScope.addToLocals(new Obj(Obj.Var, "temp2", Tab.intType, 0, 1)); // Hidden parameter
        //Tab.currentScope.addToLocals(new Obj(Obj.Var, "temp3", Tab.intType, 0, 1)); // Hidden parameter
        map_obj.setLocals(Tab.currentScope.getLocals());
        Tab.closeScope();
        Tab.currentScope.addToLocals(map_obj);
        
        
        // Setting the FpPos parameter of ord, len and chr functions (it is not set by default)
        List<String> uni_meths = new ArrayList<String>();
        uni_meths.add("ord");
        uni_meths.add("len");
        uni_meths.add("chr");
        uni_meths.add("add");
        uni_meths.add("addAll");
        uni_meths.add("union");
        uni_meths.add("map");
        for (String meth: uni_meths) {
        	// Since these methods don't have local parameters, only formal parameters, we will
        	// set the FP field of those Obj nodes to 1. That way they are marked as formal pars.
        	for (Obj form_pars: Tab.find(meth).getLocalSymbols()) {
        		form_pars.setFpPos(1);
        	}
        }
	}
	
	//------------------------ Program ------------------------
	@Override
	public void visit(Program program) {
		// getnVars() tells us how many variables are in that scope and since this is global scope
		// we will get only count of global variables (variable level 0).
		n_vars = Tab.currentScope().getnVars();
		Tab.chainLocalSymbols(prog_name);
		Tab.closeScope();
		
		if (main_method == null || main_method.getLevel() > 0) {
			report_error("Greska [Program]: Main metoda programa nije ispravna ili ne postoji", program);
		}
	}
	
	@Override
	public void visit(ProgramName program_name) {
		prog_name = Tab.insert(Obj.Prog, program_name.getI1(), Tab.noType);
		Tab.openScope();     	
	}
	
	//------------------------ Constants ------------------------
	@Override
	public void visit(ConstValueAssignment const_val_assignment) {
		Obj const_obj = Tab.find(const_val_assignment.getI1()); // Get name of constant
		if (const_obj != Tab.noObj) {
			report_error("Greska [ConstValueAssignment]: konstanta: " + const_val_assignment.getI1() + " sa ovim imenom vec postoji.", const_val_assignment);
		}
		else { // If name wasn't found in symbol table
			if (constant_type.assignableTo(curr_type)) {
				const_obj = Tab.insert(Obj.Con, const_val_assignment.getI1(), curr_type);
				const_obj.setAdr(constant); // For constants we need to write it's value to Adr field
			}
			else {
				report_error("Greska [ConstValueAssignment]: konstanti: " + const_val_assignment.getI1() + " se ne moze dodeliti data vrednost.", const_val_assignment);
			}
		}	
	}
	
	@Override
	public void visit(ConstIntegerValue const_integer_value) {
		constant = const_integer_value.getN1();
		constant_type = Tab.intType;
	}
	
	@Override
	public void visit(ConstCharValue const_char_value) {
		constant = const_char_value.getC1();
		constant_type = Tab.charType;
	}
	
	@Override
	public void visit(ConstBooleanValue const_bool_value) {
		constant = const_bool_value.getB1();
		constant_type = bool_type;
	}
	
	//------------------------ Variable declarations ------------------------
	@Override
	public void visit(VarDeclAssignmentVariable var_decl) {
		Obj var_obj = null;
		if (curr_method == null) { // Global scope
			// We are currently in global scope, so we check if variable with that name already exists in global scope
			var_obj = Tab.find(var_decl.getI1());
		}
		else { // Local scope
			// We are currently in local scope, so we check if variable with that name already exists in local scope
			var_obj = Tab.currentScope().findSymbol(var_decl.getI1());
		}
		
		if (var_obj == Tab.noObj || var_obj == null) {
			// variable does not exist in neither global/local scope, so we create it
			var_obj = Tab.insert(Obj.Var, var_decl.getI1(), curr_type);
		}
		else {
			report_error("Greska [VarDeclAssignmentVariable]: dvostruka definicija promenljive: " + var_decl.getI1(), var_decl);
		}	
	}
	
	@Override
	public void visit(VarDeclAssignmentArray var_decl) {
		Obj var_obj = null;
		if (curr_method == null) { // Global scope
			// We are currently in global scope, so we check if variable with that name already exists in global scope
			var_obj = Tab.find(var_decl.getI1());
		}
		else { // Local scope
			// We are currently in local scope, so we check if variable with that name already exists in local scope
			var_obj = Tab.currentScope().findSymbol(var_decl.getI1());
		}
		
		if (var_obj == Tab.noObj || var_obj == null) {
			// variable does not exist in neither global/local scope, so we create it
			var_obj = Tab.insert(Obj.Var, var_decl.getI1(), new Struct(Struct.Array, curr_type));
		}
		else {
			report_error("Greska [VarDeclAssignmentArray]: dvostruka definicija promenljive " + var_decl.getI1(), var_decl);
		}	
	}
	
	//------------------------ Function(Method) declarations ------------------------
	@Override
	public void visit(MethodSignatureTypeNameVoid method_void) {
		curr_method = Tab.insert(Obj.Meth, method_void.getI1(), Tab.noType);
		method_void.obj = curr_method; // Saving it here for Code Generation phase
		Tab.openScope();
		
		if (method_void.getI1().equalsIgnoreCase("main")) { // Checking if it's main method
			main_method = curr_method;
		}	
	}
	
	@Override
	public void visit(MethodSignatureTypeNameType method_type) {
		curr_method = Tab.insert(Obj.Meth, method_type.getI2(), curr_type);
		method_type.obj = curr_method; // Saving it here for Code Generation phase
		Tab.openScope();
	}
	
	@Override
	public void visit(MethodDecl method_decl) {
		Tab.chainLocalSymbols(curr_method);
		Tab.closeScope();
		if (curr_method.getType() != Tab.noType && !return_check) { // Checks if function is not Void, than it needs to have return value
			report_error("Greska [MethodDecl]: funkcija: " + curr_method.getName() + " nema return vrednost.", method_decl);
		}
		curr_method = null;
		return_check = false;
	}
	
	//------------------------ Formal Parameters(function parameters) ------------------------
	@Override
	public void visit(FormParsTypeNameVariable form_par) {
		Obj par_obj = null;
		if (curr_method == null) {
			report_error("Greska [FormParsTypeNameVariable]: formalni parametar se ne nalazi u metodi.", form_par);
		}
		else {
			// check if variable with that name already exists in local scope
			par_obj = Tab.currentScope().findSymbol(form_par.getI2());
		}

		if (par_obj == Tab.noObj || par_obj == null) {
			par_obj = Tab.insert(Obj.Var, form_par.getI2(), curr_type);
			par_obj.setFpPos(1); // Formal pars have FpPos set to 1
			curr_method.setLevel(curr_method.getLevel() + 1); // Field that specifies how many formal parameters function has
		}
		else {
			report_error("Greska [FormParsTypeNameVariable]: parametar: " + form_par.getI2() + " je vec definisan.", form_par);
		}
	}
	
	@Override
	public void visit(FormParsTypeNameArray form_par) {
		Obj par_obj = null;
		if (curr_method == null) {
			report_error("Greska [FormParsTypeNameArray]: formalni parametar se ne nalazi u metodi.", form_par);
		}
		else {
			// check if variable with that name already exists in local scope
			par_obj = Tab.currentScope().findSymbol(form_par.getI2());
		}

		if (par_obj == Tab.noObj || par_obj == null) {
			par_obj = Tab.insert(Obj.Var, form_par.getI2(), new Struct(Struct.Array, curr_type));
			par_obj.setFpPos(1);
			curr_method.setLevel(curr_method.getLevel() + 1); // Field that specifies how many formal parameters function has
		}
		else {
			report_error("Greska [FormParsTypeNameArray]: parametar: " + form_par.getI2() + " je vec definisan.", form_par);
		}
	}
	
	//------------------------ Type ------------------------
	@Override
	public void visit(Type type) {
		Obj type_obj = Tab.find(type.getI1());
		if (type_obj == Tab.noObj) {
			report_error("Greska [Type]: tip: " + type.getI1() + " ne postoji.", type);
			curr_type = Tab.noType;
			type.struct = curr_type; // This is needed for Code Generation phase
		}
		else if (type_obj.getKind() != Obj.Type) {
			report_error("Greska [Type]: Ime: " + type.getI1() + " ne predstavlja tip.", type);
			curr_type = Tab.noType;
			type.struct = curr_type; // This is needed for Code Generation phase
		}
		else {
			curr_type = type_obj.getType();
			type.struct = curr_type; // This is needed for Code Generation phase
		}
	}
	
	//------------------------ Factor declarations ------------------------
	@Override
	public void visit(FactorDesignator factor_designator) {
		factor_designator.struct = factor_designator.getDesignator().obj.getType();
	}
	
	@Override
	public void visit(FactorDesignatorMinus factor_designator_minus) {
		factor_designator_minus.struct = factor_designator_minus.getDesignator().obj.getType();
	}
	
	@Override
	public void visit(FactorDesignatorActPars factor_act_pars) {
		if (factor_act_pars.getDesignator().obj.getKind() != Obj.Meth) { // Check if Designator is not function
			report_error("Greska [FactorDesignatorActPars]: pokusan poziv metode: " + factor_act_pars.getDesignator().obj.getName() + " koja ne postoji.", factor_act_pars);
			factor_act_pars.struct = Tab.noType;
		}
		else {
			factor_act_pars.struct = factor_act_pars.getDesignator().obj.getType();
			
			// Code for checking validity of function parameters
			List<Struct> form_pars_list = new ArrayList<>(); // List of types of local parameters
			for (Obj local_param: factor_act_pars.getDesignator().obj.getLocalSymbols()) { // Iterating through every local param of function we are calling
				if (local_param.getKind() == Obj.Var && local_param.getLevel() == 1 && local_param.getFpPos() == 1) {
					form_pars_list.add(local_param.getType());
				}
			}
			ActParsCounter act_pars_counter = new ActParsCounter();
			factor_act_pars.getActPars().traverseBottomUp(act_pars_counter);
			List<Struct> act_pars_list = act_pars_counter.last_act_pars_list;
			
			try {
				if (form_pars_list.size() != act_pars_list.size()) { // Function call and function signature have to have same number of parameters
					throw new Exception("Greska u velicini");
				}
				// Checking if parameters or function call and function signature are of same type
				for (int i = 0; i < act_pars_list.size(); i++) {
					Struct act_pars_struct = act_pars_list.get(i);
					Struct form_pars_struct = form_pars_list.get(i);
					
					if (!act_pars_struct.assignableTo(form_pars_struct)) {
						throw new Exception("Greska u tipovima parametara");
					}
				}
			}
			catch (Exception e) {
				report_error("Greska [FactorDesignatorActPars]: neispravni parametri pri pozivu funkcije: " + factor_act_pars.getDesignator().obj.getName(), factor_act_pars);
			}
		}
	}
	
	@Override
	public void visit(FactorNewExpr factor_new_expr) { // Defining the new Array or Set
		if (factor_new_expr.getType().struct.equals(set_type)) {
			factor_new_expr.struct = new Struct(Struct.Array, Tab.intType); // Set is always of type int
		}
		else if (!factor_new_expr.getExpr().struct.equals(Tab.intType)) {
			report_error("Greska [FactorNewExpr]: velicina niza nije int tipa.", factor_new_expr);
			factor_new_expr.struct = Tab.noType;
		}
		else {
			factor_new_expr.struct = new Struct(Struct.Array, curr_type);
		}
	}
	
	@Override
	public void visit(FactorNumConst factor_num_const) {
		factor_num_const.struct = Tab.intType;
	}
	
	@Override
	public void visit(FactorNumConstMinus factor_num_const) {
		factor_num_const.struct = Tab.intType;
	}
	
	@Override
	public void visit(FactorCharConst factor_char_const) {
		factor_char_const.struct = Tab.charType;
	}
	
	@Override
	public void visit(FactorBoolConst factor_bool_const) {
		factor_bool_const.struct = bool_type;
	}
	
	@Override
	public void visit(FactorBracketExpression factor_expr) {
		factor_expr.struct = factor_expr.getExpr().struct;
	}
	
	//------------------------ Designator declarations ------------------------
	@Override
	public void visit(DesignatorVar designator_var) {
		Obj var_obj = Tab.find(designator_var.getI1());
		if (var_obj == Tab.noObj) { // Nothing was found
			report_error("Greska [DesignatorVar]: pokusan pristup nedefinisanoj promenljivi " + designator_var.getI1(), designator_var);
			designator_var.obj = Tab.noObj;
		}
		else if (var_obj.getKind() != Obj.Var && var_obj.getKind() != Obj.Con && var_obj.getKind() != Obj.Meth) {
			report_error("Greska [DesignatorVar]: promenljivu " + designator_var.getI1() + " nije moguce koristiti.", designator_var);
			designator_var.obj = Tab.noObj;
		}
		else {
			designator_var.obj = var_obj;
			//report_info("Pristup promenljivoj: " + var_obj.getName() + ", objektni cvor: " + designator_var.obj.getKind(), designator_var);
		}
	}
	
	@Override
	public void visit(DesignatorArrayName designator_arr_name) {
		Obj arr_obj = Tab.find(designator_arr_name.getI1());
		if (arr_obj == Tab.noObj) {
			report_error("Greska [DesignatorArrayName]: pokusan pristup nedefinisanoj promenljivi(nizu): " + designator_arr_name.getI1(), designator_arr_name);
			designator_arr_name.obj = Tab.noObj;
		}
		// Every Array must be Var kind and their type must be Array
		else if (arr_obj.getKind() != Obj.Var || arr_obj.getType().getKind() != Struct.Array) {
			report_error("Greska [DesignatorArrayName]: promenljiva niza nije ispravno napravljena: " + designator_arr_name.getI1(), designator_arr_name);
			designator_arr_name.obj = Tab.noObj;
		}
		else {
			designator_arr_name.obj = arr_obj;
		}
	}
	
	@Override
	public void visit(DesignatorElem designator_elem) {
		Obj arr_obj = designator_elem.getDesignatorArrayName().obj;
		if (arr_obj == Tab.noObj) { // First check if error occurred in child node, DesignatorArrayName
			designator_elem.obj = Tab.noObj;
		}	
		else if (!designator_elem.getExpr().struct.equals(Tab.intType)) {
			report_error("Greska [DesignatorElem]: indeks niza nije int vrednost.", designator_elem);
			designator_elem.obj = Tab.noObj;
		}
		else { // We already have an array, now we take element from that array
			designator_elem.obj = new Obj(Obj.Elem, arr_obj.getName() + "[index]", arr_obj.getType().getElemType());
			//report_info("Pristup elementu niza: " + arr_obj.getName() + ", objektni cvor: " + designator_elem.obj.getKind(), designator_elem);
		}
	}
	
	//------------------------ Term declarations ------------------------
	@Override
	public void visit(Term term) {
		term.struct = term.getTermMulopFactorList().struct;
	}
	
	@Override
	public void visit(TermMulopFactorListMul term_mulop_factor_list_mul) {
		Struct right_factor = term_mulop_factor_list_mul.getFactor().struct;
		Struct left_factor = term_mulop_factor_list_mul.getTermMulopFactorList().struct;
		if (right_factor.equals(Tab.intType) && left_factor.equals(Tab.intType)) {
			term_mulop_factor_list_mul.struct = Tab.intType;
		}
		else {
			report_error("Greska [TermMulopFactorListMul]:  operandi mnozenja nisu int vrednosti.", term_mulop_factor_list_mul);
			term_mulop_factor_list_mul.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(TermMulopFactorListFactor term_mulop_factor_list_factor) {
		term_mulop_factor_list_factor.struct = term_mulop_factor_list_factor.getFactor().struct;
	}
	
	//------------------------ Expression declarations ------------------------
	@Override
	public void visit(ExprTerm expr) {
		expr.struct = expr.getExprAddopTermList().struct;
	}
	
	@Override
	public void visit(ExprMap expr) {
		int designator_left_kind = expr.getDesignator().obj.getKind();
		String designator_left_name = expr.getDesignator().obj.getName();
		Obj meth = expr.getDesignator().obj;
		int designator_right_kind = expr.getDesignator1().obj.getType().getKind();
		boolean conditions = true;
		
		Obj func_obj = Tab.find(expr.getDesignator().obj.getName());
		report_info("adresa: " + func_obj.getAdr(), expr);
		
		if (designator_left_kind != Obj.Meth) {
			report_error("Greska [ExprMap]: " + expr.getDesignator().obj.getName() + " nije metoda.", expr);
			expr.struct = Tab.noType;
			conditions = false;
		}
		
		//Obj meth = Tab.find(designator_left_name);
		if (!meth.getType().equals(Tab.intType)) {
	        report_error("Greska [ExprMap]: metoda " + designator_left_name + " ne vraca int.", expr);
	        expr.struct = Tab.noType;
	        conditions = false;
	    }
	    if (meth.getLevel() != 1) {
	        report_error("Greska [ExprMap]: metoda " + designator_left_name + " mora imati tacno jedan parametar tipa int.", expr);
	        expr.struct = Tab.noType;
	        conditions = false;
	    }
	    
	    Obj first_param = meth.getLocalSymbols().iterator().next();
	    if (first_param.getType() != Tab.intType) {
	        report_error("Greska [ExprMap]: prvi parametar metode " + designator_left_name + " nije tipa int.", expr);
	        expr.struct = Tab.noType;
	        conditions = false;
	    }
	    
		if (designator_right_kind != Struct.Array && expr.getDesignator1().obj.getType().getElemType() != Tab.intType) {
			report_error("Greska [ExprMap]: " + expr.getDesignator1().obj.getName() + " nije niz celobrojnih vrednosti.", expr);
			expr.struct = Tab.noType;
			conditions = false;
		}
		
		if (conditions == true) {
			expr.struct = Tab.intType;
		}
	}
	
	@Override
	public void visit(ExprAddopTermListAdd expr_addop_term_list_add) {
		Struct right_term = expr_addop_term_list_add.getTerm().struct;
		Struct left_term = expr_addop_term_list_add.getExprAddopTermList().struct;
		if (right_term.equals(Tab.intType) && left_term.equals(Tab.intType)) {
			expr_addop_term_list_add.struct = Tab.intType;
		}
		else {
			report_error("Greska [ExprAddopTermListAdd]: operandi sabiranja nisu int vrednosti.", expr_addop_term_list_add);
			expr_addop_term_list_add.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(ExprAddopTermListTerm expr_addop_term_list_term) {
		expr_addop_term_list_term.struct = expr_addop_term_list_term.getTerm().struct;
	}
	
	//------------------------ Designator Statement declarations ------------------------
	@Override
	public void visit(DesignatorAssignOperation designator_assign) {
		int obj_kind = designator_assign.getDesignator().obj.getKind();
		if (obj_kind != Obj.Elem && obj_kind != Obj.Var) {
			report_error("Greska [DesignatorAssignOperation]: vrednost se ne moze dodeliti promenljivoj: " + designator_assign.getDesignator().obj.getName(), designator_assign);
		}
		else if (!designator_assign.getExpr().struct.assignableTo(designator_assign.getDesignator().obj.getType())) { // Check if expression is assignable to designator
			report_error("Greska [DesignatorAssignOperation]: dodeljeni tip se ne moze dodeliti tipu promenljive: " + designator_assign.getDesignator().obj.getName(), designator_assign);
		}
	}
	
	@Override
	public void visit(DesignatorPostIncrement designator_post_inc) {
		int obj_kind = designator_post_inc.getDesignator().obj.getKind();
		if (obj_kind != Obj.Elem && obj_kind != Obj.Var) {
			report_error("Greska [DesignatorPostIncrement]: promenljiva: " + designator_post_inc.getDesignator().obj.getName() + " ne moze biti inkrementirana.", designator_post_inc);
		}
		else if (!designator_post_inc.getDesignator().obj.getType().equals(Tab.intType)) {
			report_error("Greska [DesignatorPostIncrement]: promenljiva: " + designator_post_inc.getDesignator().obj.getName() + " nije tipa int.", designator_post_inc);
		}	
	}
	
	@Override
	public void visit(DesignatorPostDecrement designator_post_dec) {
		int obj_kind = designator_post_dec.getDesignator().obj.getKind();
		if (obj_kind != Obj.Elem && obj_kind != Obj.Var) {
			report_error("Greska [DesignatorPostDecrement]: promenljiva: " + designator_post_dec.getDesignator().obj.getName() + " ne moze biti dekrementirana.", designator_post_dec);
		}
		else if(!designator_post_dec.getDesignator().obj.getType().equals(Tab.intType)) {
			report_error("Greska [DesignatorPostDecrement]: promenljiva: " + designator_post_dec.getDesignator().obj.getName() + " nije tipa int.", designator_post_dec);
		}
	}
	
	@Override
	public void visit(DesignatorFunctionCallActPars designator_func_call_pars) { // Function call with parameters
		if (designator_func_call_pars.getDesignator().obj.getKind() != Obj.Meth) { // Check if designator is even a function
			report_error("Greska [DesignatorFunctionCallActPars]: funkcija: " + designator_func_call_pars.getDesignator().obj.getName() + " ne postoji.", designator_func_call_pars);
		}
		else {
			List<Struct> form_pars_list = new ArrayList<>(); // List of types of local parameters
			for (Obj local_param: designator_func_call_pars.getDesignator().obj.getLocalSymbols()) { // Iterating through every local param of function we are calling
				if (local_param.getKind() == Obj.Var && local_param.getLevel() == 1 && local_param.getFpPos() == 1) {
					form_pars_list.add(local_param.getType());
				}
			}
			ActParsCounter act_pars_counter = new ActParsCounter();
			designator_func_call_pars.getActPars().traverseBottomUp(act_pars_counter);
			List<Struct> act_pars_list = act_pars_counter.last_act_pars_list;
			
			// Have to manually add hidden parameters for add and addAll
			if (designator_func_call_pars.getDesignator().obj.getName().equals("add")) {
				act_pars_list.add(Tab.find("int").getType());
			}
			else if (designator_func_call_pars.getDesignator().obj.getName().equals("addAll")) {
				act_pars_list.add(Tab.find("int").getType());
				act_pars_list.add(Tab.find("int").getType());
				act_pars_list.add(Tab.find("int").getType());
			}
//			else if (designator_func_call_pars.getDesignator().obj.getName().equals("union")) {
//				act_pars_list.add(Tab.find("int").getType());
//				act_pars_list.add(Tab.find("int").getType());
//				act_pars_list.add(Tab.find("int").getType());
//			}
			
			try {
//				// Print formal parameters
//			    StringBuilder formParamsStr = new StringBuilder("Formal parameters: ");
//			    for (Struct param : form_pars_list) {
//			        formParamsStr.append(param.toString()).append(" ");
//			    }
//			    report_info(formParamsStr.toString(), designator_func_call_pars);
//			    
//			    // Print actual parameters
//			    StringBuilder actParamsStr = new StringBuilder("Actual parameters: ");
//			    for (Struct param : act_pars_list) {
//			        actParamsStr.append(param.toString()).append(" ");
//			    }
//			    report_info(actParamsStr.toString(), designator_func_call_pars);
				if (form_pars_list.size() != act_pars_list.size()) { // Function call and function signature have to have same number of parameters
//					report_info("formalni " + form_pars_list.size(), designator_func_call_pars);
//					report_info("act " + act_pars_list.size(), designator_func_call_pars);
					throw new Exception("Greska u velicini");
				}
				// Checking if parameters or function call and function signature are of same type
				for (int i = 0; i < act_pars_list.size(); i++) {
					Struct act_pars_struct = act_pars_list.get(i);
					Struct form_pars_struct = form_pars_list.get(i);
					
					if (!act_pars_struct.assignableTo(form_pars_struct)) {
//						report_info("formalni " + form_pars_list.size(), designator_func_call_pars);
//						report_info("act " + act_pars_list.size(), designator_func_call_pars);
						throw new Exception("Greska u tipovima parametara");
					}
				}
			}
			catch (Exception e) {
				report_error(e.getMessage() + " [DesignatorFunctionCallActPars]: neispravni parametri pri pozivu funkcije: " + designator_func_call_pars.getDesignator().obj.getName(), designator_func_call_pars);
			}
		}
	}
	
	@Override
	public void visit(DesignatorAssignopSetop designator_assign_set) {
		Struct obj1_type = designator_assign_set.getDesignator().obj.getType();
		Struct obj2_type = designator_assign_set.getDesignator1().obj.getType();
		Struct obj3_type = designator_assign_set.getDesignator2().obj.getType();

		if (obj1_type != set_type) {
			report_error("Greska [DesignatorAssignopSetop]: tip promenljive: " + designator_assign_set.getDesignator().obj.getName() + " nije 'set'", designator_assign_set);
		}
		if (obj2_type != set_type) {
			report_error("Greska [DesignatorAssignopSetop]: tip promenljive: " + designator_assign_set.getDesignator1().obj.getName() + " nije 'set'", designator_assign_set);
		}
		if (obj3_type != set_type) {
			report_error("Greska [DesignatorAssignopSetop]: tip promenljive: " + designator_assign_set.getDesignator2().obj.getName() + " nije 'set'", designator_assign_set);
		}
	}
	
	//------------------------ Statement declarations ------------------------
	@Override
	public void visit(StatementRead statement_read) {
		int obj_kind = statement_read.getDesignator().obj.getKind();
		Struct type = statement_read.getDesignator().obj.getType();
		if (obj_kind != Obj.Fld && obj_kind != Obj.Elem && obj_kind != Obj.Var) {
			report_error("Greska [StatementRead]: nad promenljivom: " + statement_read.getDesignator().obj.getName() + " se ne moze izvrsiti Read.", statement_read);
		}
		else if (!type.equals(Tab.intType) && !type.equals(Tab.charType) && !type.equals(bool_type)) {
			report_error("Greska [StatementRead]: promenljiva: " + statement_read.getDesignator().obj.getName() + " nije int/char/bool.", statement_read);
		}
	}
	
	@Override
	public void visit(StatementPrintExpr statement_print_expr) {
		Struct type = statement_print_expr.getExpr().struct;
		if (!type.equals(Tab.intType) && !type.equals(Tab.charType) && !type.equals(bool_type) && !type.equals(set_type)) {
			report_error("Greska [StatementPrintExpr]: izraz koji se printuje nije int/char/bool/set tipa.", statement_print_expr);
		}
	}
	
	@Override
	public void visit(StatementPrintExprNumber statement_print_expr_num) {
		Struct type = statement_print_expr_num.getExpr().struct;
		if (!type.equals(Tab.intType) && !type.equals(Tab.charType) && !type.equals(bool_type) && !type.equals(set_type)) {
			report_error("Greska [StatementPrintExprNumber]: izraz koji se printuje nije int/char/bool/set tipa.", statement_print_expr_num);
		}
	}
	
	@Override
	public void visit(StatementReturnEmpty statement_return_empty) { // This is for void functions
		return_check = true;
		if (curr_method.getType() != Tab.noType) {
			report_error("Greska [StatementReturnEmpty]: return vrednost nije ispravna u metodi: " + curr_method.getName(), statement_return_empty);
		}
	}
	
	@Override
	public void visit(StatementReturnExpr statement_return_expr) {
		return_check = true;
		if (!curr_method.getType().equals(statement_return_expr.getExpr().struct)) {
			report_error("Greska [StatementReturnEmpty]: return vrednost nije ispravna u metodi: " + curr_method.getName(), statement_return_expr);
		}
	}
	
	@Override
	public void visit(StatementBreak statement_break) {
		if (while_loop_counter == 0) {
			report_error("Greska [StatementBreak]: Break naredba je van tela petlje.", statement_break);
		}
	}
	
	@Override
	public void visit(StatementContinue statement_continue) {
		if (while_loop_counter == 0) {
			report_error("Greska [StatementContinue]: Continue naredba je van unutar tela petlje.", statement_continue);
		}
	}
	
	//------------------------ Do-While loops ------------------------
	@Override
	public void visit(DoHelper do_helper) { // Start of do-while loop
		while_loop_counter++;
	}
	
	@Override
	public void visit(StatementDoWhileCond statement_do_while) { // End of do-while loop
		while_loop_counter--;
	}
	
	@Override
	public void visit(StatementDoWhileEmpty statement_do_while) { // End of do-while loop
		while_loop_counter--;
	}
	
	@Override
	public void visit(StatementDoWhileCondDesStatement statement_do_while) { // End of do-while loop
		while_loop_counter--;
	}
	
	//------------------------ Condition declarations ------------------------
	@Override
	public void visit(Condition cond) {
		cond.struct = cond.getConditionCondTermList().struct;
		if (!cond.struct.equals(bool_type)) {
			report_error("Greska [Condition]: tip uslova nije bool.", cond);
		}
	}
	
	@Override
	public void visit(CondFactExpr cond_fact_expr) {
		if (!cond_fact_expr.getExpr().struct.equals(bool_type)) {
			report_error("Greska [CondFactExpr]: tip operanda nije bool.", cond_fact_expr);
			cond_fact_expr.struct = Tab.noType;
		}
		else {
			cond_fact_expr.struct = bool_type;
		}
	}
	
	@Override
	public void visit(CondFactRelopExpr cond_fact_relop_expr) {
		Struct right_expr = cond_fact_relop_expr.getExpr1().struct;
		Struct left_expr = cond_fact_relop_expr.getExpr().struct;
		if (left_expr.compatibleWith(right_expr)) { // example: a < b, we have to check if a is compatible with b
			if (left_expr.isRefType() || right_expr.isRefType()) { // isRefType checks whether variable is class or array
				// Only = and != are allowed
				if (cond_fact_relop_expr.getRelop() instanceof NotEqualOp || cond_fact_relop_expr.getRelop() instanceof EqualOp) {
					cond_fact_relop_expr.struct = bool_type;
				}	
				else {
					report_error("Greska [CondFactRelopExpr]: nije koriscen operator poredjenja.", cond_fact_relop_expr);
					cond_fact_relop_expr.struct = Tab.noType;
				}
			}
			else {
				cond_fact_relop_expr.struct = bool_type;
			}
		}
		else {
			report_error("Greska [CondFactRelopExpr]: operandi nisu kompatibilnih tipova.", cond_fact_relop_expr);
			cond_fact_relop_expr.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(CondTerm cond_term) {
		cond_term.struct = cond_term.getCondTermCondFactList().struct;
	}
	
	@Override
	public void visit(CondTermCondFactListAnd condterm_condfact_list_and) {
		Struct right = condterm_condfact_list_and.getCondFact().struct;
		Struct left = condterm_condfact_list_and.getCondTermCondFactList().struct;
		if (right.equals(bool_type) && left.equals(bool_type)) {
			condterm_condfact_list_and.struct = bool_type;
		}
		else {
			report_error("Greska [CondTermCondFactListAnd]: operandi nisu bool vrednosti.", condterm_condfact_list_and);
			condterm_condfact_list_and.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(CondTermCondFactListCondFact condterm_condfact_list_condfact) {
		condterm_condfact_list_condfact.struct = condterm_condfact_list_condfact.getCondFact().struct;
	}
	
	@Override
	public void visit(ConditionCondTermListOr cond_condterm_list_or) {
		Struct right = cond_condterm_list_or.getCondTerm().struct;
		Struct left = cond_condterm_list_or.getConditionCondTermList().struct;
		if (right.equals(bool_type) && left.equals(bool_type)) {
			cond_condterm_list_or.struct = bool_type;
		}
		else {
			report_error("Greska [ConditionCondTermListOr]: operandi nisu bool vrednosti.", cond_condterm_list_or);
			cond_condterm_list_or.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(ConditionCondTermListCondTerm cond_condterm_list_condterm) {
		cond_condterm_list_condterm.struct = cond_condterm_list_condterm.getCondTerm().struct;
	}
}

