package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;
import org.apache.log4j.Logger;

public class CodeGenerator extends VisitorAdaptor {
	Logger log = Logger.getLogger(getClass());
	private int main_pc;
	private Struct set_type = Tab.find("set").getType();
	
	private Stack<Integer> skip_cond_fact = new Stack<>(); // Storing every address where cond fact is not correct
	private Stack<Integer> skip_condition = new Stack<>();
	private Stack<Integer> skip_then = new Stack<>();
	private Stack<Integer> skip_else = new Stack<>(); // Storing every address that should skip Else branch
	private Stack<Integer> do_start = new Stack<>(); // Used for nested do while loops
	private Stack<List<Integer>> break_jumps = new Stack<>();
	private Stack<List<Integer>> continue_jumps = new Stack<>();
	
	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	CodeGenerator() {
		initialize();
	}
	
	public int getMainPc() {
		return this.main_pc;
	}
	
	private void initialize() { // Used to initialize ord,char and len methods
		Obj ord_meth = Tab.find("ord");
		Obj chr_meth = Tab.find("chr");
		ord_meth.setAdr(Code.pc);
		chr_meth.setAdr(Code.pc);
		// ord and char methods have the same code
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n); // same as load_0
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Obj len_meth = Tab.find("len");
		len_meth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n); // same as load_0
		Code.put(Code.arraylength); // Place the length of an array on expr stack
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		// ----------------------------------ADD----------------------------------
		Obj add_meth = Tab.find("add");
		add_meth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(3);
		Code.put(3);
		
		Collection<Obj> formal_pars_add = add_meth.getLocalSymbols(); // Only formal parameters exist in this function (there are no local)
		List<Obj> formal_pars_list_add = new ArrayList<>(formal_pars_add); // Converting to list for easier access
		
		Obj set_obj = formal_pars_list_add.get(0);
		Obj val_obj = formal_pars_list_add.get(1);
		Obj size_obj = formal_pars_list_add.get(2);
		
		// Load set address on the stack
		Code.load(set_obj);
		// Stack: [array_addr]
		
		// Load last element (size counter), size_obj = set[len - 1]
		Code.put(Code.dup); // Stack: [array_addr, array_addr]
		Code.put(Code.dup); // Stack: [array_addr, array_addr, array_addr]
		Code.put(Code.arraylength); // Stack: [array_addr, array_addr, len]
		Code.loadConst(1); 
		Code.put(Code.sub); // Get last element from set (size counter)
		Code.put(Code.aload); // Stack: [array_addr, size]
		Code.store(size_obj); // variable used to store size of set
		// Stack: [array_addr]

		Code.loadConst(0); // Stack: [array_addr, index]
		
		int loop_start_addr = Code.pc;
		
		// Loop check: if (index == size), exit loop
		Code.put(Code.dup); // Stack: [array_addr, index, index]
		Code.load(size_obj); // Stack: [array_addr, index, index, size]
		Code.putFalseJump(Code.ne, 0); // if (index != size)
		int exit_loop_addr = Code.pc - 2; //TODO
		// Stack: [array_addr, index]
		
		
		// Check if set[index] == val
		Code.put(Code.dup2); // Stack: [array_addr, index, array_addr, index]
		Code.put(Code.aload); // Stack: [array_addr, index, val_at_index]
		Code.load(val_obj); // Stack: [array_addr, index, val_at_index, val]
		Code.putFalseJump(Code.ne, 0);
		int found_elem_addr = Code.pc - 2; // If val is found, jump to exit
		// Stack: [array_addr, index]
		
		
		// Increment index and loop again
		Code.loadConst(1);
		Code.put(Code.add); // Stack: [array_addr, index + 1]
		Code.putJump(loop_start_addr);
		
		Code.fixup(exit_loop_addr); // Update exit condition address
		// Stack: [array_addr, index]
		
		
		// Check if set is full (size == array.length - 1)
		Code.load(size_obj); // Stack: [array_addr, index, size]
		Code.load(set_obj); // Stack: [array_addr, index, size, array_addr]
		Code.put(Code.arraylength); // Stack: [array_addr, index, size, len]
		Code.loadConst(1);
		Code.put(Code.sub); // Stack: [array_addr, index, size, len - 1]
		Code.putFalseJump(Code.lt, 0); // If (size < len - 1)
		int full_set_addr = Code.pc - 2;
		// Stack: [array_addr, index]
		
		// If we reach here, element wasn't found and we have space to insert it
		// Add element at index size
		Code.put(Code.pop);
		Code.put(Code.pop); // Stack: []
		Code.load(set_obj); // Stack: [array_addr]
		Code.load(size_obj); // Stack: [array_addr, size]
		Code.load(val_obj); // Stack: [array_addr, size, val]
		Code.put(Code.astore); // Stack: []

		// Increment size counter
		Code.load(set_obj); // Stack: [array_addr]
		Code.put(Code.dup); // Stack: [array_addr, array_addr]
		Code.put(Code.arraylength); // Stack: [array_addr, len]
		Code.loadConst(1);
		Code.put(Code.sub); // Stack: [array_addr, len - 1]
		Code.load(size_obj); // Stack: [array_addr, len - 1, size]
		Code.loadConst(1);
		Code.put(Code.add); // Stack: [array_addr, len - 1, size + 1]
		Code.put(Code.astore); // Stack: []

		// Exit
		Code.fixup(found_elem_addr);
		Code.fixup(full_set_addr);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		// ----------------------------------ADD ALL----------------------------------
		Obj addAll_meth = Tab.find("addAll");
		addAll_meth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(5);
		Code.put(5);
		
		Collection<Obj> formal_pars_add_all = addAll_meth.getLocalSymbols(); // Only formal parameters exist in this function (there are no local)
		List<Obj> formal_pars_list_add_all = new ArrayList<>(formal_pars_add_all); // Converting to list for easier access
		
		Obj set_all_obj = formal_pars_list_add_all.get(0); // Starting address of set
		Obj arr_obj = formal_pars_list_add_all.get(1); // Starting address of array
		Obj arr_ind_obj = formal_pars_list_add_all.get(2); // Temporary variable for iterating array
		Obj set_ind_obj = formal_pars_list_add_all.get(3); // Temporary variable for iterating set
		Obj set_size_obj = formal_pars_list_add_all.get(4); // Temporary variable for storing size of set
		
		// Load set size (stored at set[len - 1])
		Code.load(set_obj); // Stack: [set_addr]
		Code.put(Code.dup); // Stack: [set_addr, set_addr]
		Code.put(Code.arraylength); // Stack: [set_addr, set_len]
		Code.loadConst(1); 
		Code.put(Code.sub); // Stack: [set_addr, set_len - 1]
		Code.put(Code.aload); // Stack: [set_size]
		Code.store(set_size_obj); // set_size_obj = set_size
		// Stack: []
		
		// Initialize set index to 0
		Code.loadConst(0);
		Code.store(set_ind_obj); // set_ind_obj = set_index = 0
		// Stack: []
		
		// Initialize array index to 0
		Code.loadConst(0);
		Code.store(arr_ind_obj); // arr_ind_obj = arr_index = 0
		// Stack: []
		
		
		// Start array iteration
		int arrLoopStart = Code.pc;

		// Check if we finished iterating over the array
		Code.load(arr_ind_obj); // Stack: [arr_index]
		Code.load(arr_obj); // Stack: [arr_index, arr_addr]
		Code.put(Code.arraylength); // Stack: [arr_index, arr_len]
		Code.putFalseJump(Code.lt, 0); // if (arr_index >= arr_length), exit array loop
		int arrLoopExit = Code.pc - 2;
		// Stack: []
		
		
		// Reset set index
		Code.loadConst(0);
		Code.store(set_ind_obj);
		// Stack: []
		
		
		// Start iterating through set in order to find or add array element
		int setLoopStart = Code.pc;
		
		// Check if we went over all elements in set
		Code.load(set_ind_obj);
		Code.load(set_size_obj); // Stack: [arr[arr_index], set_index, set_size]
		Code.putFalseJump(Code.lt, 0); // if (set_index >= set_size), exit set loop and try adding that element
		int setLoopExit = Code.pc - 2;
		// Stack: []
		

		// Compare arr[arr_index] with set[set_index]
		Code.load(arr_obj);
		Code.load(arr_ind_obj);
		Code.put(Code.aload); // Stack: [arr[arr_index]]
		Code.load(set_obj);
		Code.load(set_ind_obj); // Stack: [arr[arr_index], set_addr, set_index]
		Code.put(Code.aload); // Stack: [arr[arr_index], set[set_index]]
		Code.putFalseJump(Code.ne, 0); // if (set[set_index] == arr[arr_index]), element exists, continue to next array element
		int inc_arr_ind = Code.pc - 2; // We have to increment arr_index first
		// Stack: []
		
		// Increment set index
		Code.load(set_ind_obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(set_ind_obj);
		// Stack: []

		// Keep iterating over the set to check if that element already exists
		Code.putJump(setLoopStart);
		
		
		Code.fixup(setLoopExit);
		//Code.put(Code.pop); // arr[arr_index] is on the stack, we don't need it anymore
		// Stack: []
		
		// If set is full, exit
		Code.load(set_size_obj); // Stack: [set_size]
		Code.load(set_obj); // Stack: [set_size, set_addr]
		Code.put(Code.arraylength); // Stack: [set_size, set_len]
		Code.loadConst(1);
		Code.put(Code.sub); // Stack: [set_size, set_len - 1]
		Code.putFalseJump(Code.lt, 0); // if (set_size >= set_len - 1), exit loop
		int arrLoopExit2 = Code.pc - 2;
		// Stack: [arr[arr_index]]
		
		// Set is not full, add new element
		Code.load(set_obj);
		Code.load(set_size_obj);
		Code.load(arr_obj);
		Code.load(arr_ind_obj); // Stack: [set_addr, set_size, arr_addr, arr_index]
		Code.put(Code.aload); // Stack: [set_addr, set_size, arr[arr_index]]
		Code.put(Code.astore); // set[set_size] = arr[arr_index]
		// Stack: []
		
		// Increment set size
		Code.load(set_size_obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(set_size_obj);

		
		Code.fixup(inc_arr_ind);
		// Increment array index
		Code.load(arr_ind_obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(arr_ind_obj);
		Code.putJump(arrLoopStart); // Jump to next iteration of array loop
		
		
		Code.fixup(arrLoopExit);
		Code.fixup(arrLoopExit2);
		
		// Store set size value as last element of set
		Code.load(set_obj);
		Code.load(set_obj); // Stack: [set_addr, set_addr]
		Code.put(Code.arraylength); // Stack: [set_addr, set_len]
		Code.loadConst(1);
		Code.put(Code.sub); // Stack: [set_addr, set_len - 1]
		Code.load(set_size_obj); // Stack: [set_addr, set_len - 1, set_size]
		Code.put(Code.astore);
		// Stack: []
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		// ----------------------------------UNION----------------------------------
		Obj union_meth = Tab.find("union");
		union_meth.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(6);
		Code.put(6);
		
		Collection<Obj> formal_pars_union = union_meth.getLocalSymbols(); // Only formal parameters exist in this function (there are no local)
		List<Obj> formal_pars_list_union = new ArrayList<>(formal_pars_union); // Converting to list for easier access
		
		Obj set1_obj = formal_pars_list_union.get(0); // Starting address of set1
		Obj set2_obj = formal_pars_list_union.get(1); // Starting address of set2
		Obj set3_obj = formal_pars_list_union.get(2); // Starting address of set3
		Obj temp1_obj = formal_pars_list_union.get(3);
		Obj temp2_obj = formal_pars_list_union.get(4);
		Obj set3_size_obj = formal_pars_list_union.get(5); // Size of set3
				
		// Initialize set3_size_obj = 0
		Code.loadConst(0);
		Code.store(set3_size_obj);
		
		// ---------Initialization for set1---------
		
		// temp1 will be index for set1
		Code.loadConst(0);
		Code.store(temp1_obj);
		
		// temp2 will be size for set1
		Code.load(set1_obj); // Stack: [set1_addr]
		Code.put(Code.dup); // Stack: [set1_addr, set1_addr]
		Code.put(Code.arraylength); // Stack: [set1_addr, set1_len]
		Code.loadConst(1);
		Code.put(Code.sub); // Stack: [set1_addr, set1_len - 1]
		Code.put(Code.aload); // Stack: [set1_size]
		Code.store(temp2_obj);
		// Stack: []
		
		
		// ---------Set1 loop---------
		
		// Copy all elements from set1 to set3
		int set1_loop_start = Code.pc;
		
		// Check if we went over all elements in set1
		Code.load(temp1_obj);
		Code.load(temp2_obj); // Stack: [set1_index, set1_size]
		Code.putFalseJump(Code.lt, 0); // if (set1_index >= set1_size), exit set1 loop
		int set1_loop_exit = Code.pc - 2;
		// Stack: []
		
		
		// ---------Add set1[set1_index] to set3---------
		
		// Loop through set3 and see if element already exists
		Code.loadConst(0); // Stack: [set3_index]
		int set3_loop_start1 = Code.pc;
		
		// Get set1[set1_index]
		Code.load(set1_obj);
		Code.load(temp1_obj);
		Code.put(Code.aload); // Stack: [set3_index, set1[set1_index]]
		
		// Reverse stack order
		Code.put(Code.dup_x1); // Stack: [set1[set1_index], set3_index, set1[set1_index]]
		Code.put(Code.pop); // Stack: [set1[set1_index], set3_index]
		
		// If set3 is full, exit function
		Code.load(set3_size_obj); // Stack: [set3_size]
		Code.load(set3_obj); // Stack: [set3_size, set3_addr]
		Code.put(Code.arraylength); // Stack: [set3_size, set3_len]
		Code.loadConst(1);
		Code.put(Code.sub); // Stack: [set3_size, set3_len - 1]
		Code.putFalseJump(Code.lt, 0); // if (set3_size >= set3_len - 1), exit loop
		int exit_union1 = Code.pc - 2;
		// Stack: [set1[set1_index], set3_index]
		
		// Check if set3_index == set3_size, if it is, insert element there
		Code.put(Code.dup); // Stack: [set1[set1_index], set3_index, set3_index]
		Code.load(set3_size_obj); // Stack: [set1[set1_index], set3_index, set3_index, set3_size]
		Code.putFalseJump(Code.eq, 0); // if (set3_index != set3_size), don't insert element
		int inc_set3_index1 = Code.pc - 2;
		// Stack: [set1[set1_index], set3_index]
		
		// Remove unnecessary value from stack
		Code.put(Code.dup_x1); // Stack: [set3_index, set1[set1_index], set3_index]
		Code.put(Code.pop);
		Code.put(Code.pop); // Stack: [set3_index]
		
		// Insert element to set3
		Code.load(set3_obj); // Stack: [set3_index, set3_addr]
		Code.put(Code.dup_x1); // Stack: [set3_addr, set3_index, set3_addr]
		Code.put(Code.pop); // Stack: [set3_addr, set3_index]
		Code.put(Code.dup_x1); // Stack: [set3_index, set3_addr, set3_index]
		Code.load(set1_obj); // Stack: [set3_index, set3_addr, set3_index, set1_addr]
		Code.load(temp1_obj); // Stack: [set3_index, set3_addr, set3_index, set1_addr, set1_index]
		Code.put(Code.aload); // Stack: [set3_index, set3_addr, set3_index, set1[set1_index]]
		Code.put(Code.astore);
		// Stack: [set3_index]
		
		// Increment set3_size
		Code.load(set3_size_obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(set3_size_obj);
		// Stack: [set3_index]
		
		// Element is added, go to next set1 element
		Code.putJump(0);
		int set3_loop_end1 = Code.pc - 2;
		
		Code.fixup(inc_set3_index1);
		// Stack: [set1[set1_index], set3_index]
		
		// Check if that element is on current set3_index
		Code.load(set3_obj); // Stack: [set1[set1_index], set3_index, set3_addr]
		Code.put(Code.dup_x1); // Stack: [set1[set1_index], set3_addr, set3_index, set3_addr]
		Code.put(Code.pop); // Stack: [set1[set1_index], set3_addr, set3_index]
		Code.put(Code.dup_x2); // Stack: [set3_index, set1[set1_index], set3_addr, set3_index]
		Code.put(Code.aload); // Stack: [set3_index, set1[set1_index], set3[set3_index]]
		Code.putFalseJump(Code.ne, 0); // if (set1[set1_index] == set3[set3_index]), element exists, continue set1 loop
		int inc_set1_index = Code.pc - 2;
		// Stack: [set3_index]
		
		// Increment set3 index
		Code.loadConst(1);
		Code.put(Code.add);
		// Stack: [set3_index + 1]
		
		// Jump to next loop iteration
		Code.putJump(set3_loop_start1);
		
		Code.fixup(set3_loop_end1);
		Code.fixup(inc_set1_index);
		
		// Emptying the stack
		Code.put(Code.pop); // Stack: []
		
		
		// Increment set1 index
		Code.load(temp1_obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(temp1_obj);
		// Stack: []

		// Keep iterating over the set1
		Code.putJump(set1_loop_start);
		
		Code.fixup(set1_loop_exit);
		// Stack: []
		
		
		
		// ---------Initialization for set2---------
		
		// temp1 will be index for set2
		Code.loadConst(0);
		Code.store(temp1_obj);
		
		// temp2 will be size for set2
		Code.load(set2_obj); // Stack: [set2_addr]
		Code.put(Code.dup); // Stack: [set2_addr, set2_addr]
		Code.put(Code.arraylength); // Stack: [set2_addr, set2_len]
		Code.loadConst(1);
		Code.put(Code.sub); // Stack: [set2_addr, set2_len - 1]
		Code.put(Code.aload); // Stack: [set2_size]
		Code.store(temp2_obj);
		// Stack: []
		
		// ---------Set2 loop---------
		
		// Copy all elements from set2 to set3
		int set2_loop_start = Code.pc;
		
		// Check if we went over all elements in set2
		Code.load(temp1_obj);
		Code.load(temp2_obj); // Stack: [set2_index, set2_size]
		Code.putFalseJump(Code.lt, 0); // if (set2_index >= set2_size), exit set2 loop
		int set2_loop_exit = Code.pc - 2;
		// Stack: []
		
		
		// ---------Add set2[set2_index] to set3---------
		
		// Loop through set3 and see if element already exists
		Code.loadConst(0); // Stack: [set3_index]
		int set3_loop_start2 = Code.pc;
		
		// Get set2[set2_index]
		Code.load(set2_obj);
		Code.load(temp1_obj);
		Code.put(Code.aload); // Stack: [set3_index, set2[set2_index]]
		
		// Reverse stack order
		Code.put(Code.dup_x1); // Stack: [set2[set2_index], set3_index, set2[set2_index]]
		Code.put(Code.pop); // Stack: [set2[set2_index], set3_index]
		
		// If set3 is full, exit function
		Code.load(set3_size_obj); // Stack: [set3_size]
		Code.load(set3_obj); // Stack: [set3_size, set3_addr]
		Code.put(Code.arraylength); // Stack: [set3_size, set3_len]
		Code.loadConst(1);
		Code.put(Code.sub); // Stack: [set3_size, set3_len - 1]
		Code.putFalseJump(Code.lt, 0); // if (set3_size >= set3_len - 1), exit loop
		int exit_union2 = Code.pc - 2;
		// Stack: [set2[set2_index], set3_index]
		
		// Check if set3_index == set3_size, if it is, insert element there
		Code.put(Code.dup); // Stack: [set2[set2_index], set3_index, set3_index]
		Code.load(set3_size_obj); // Stack: [set2[set2_index], set3_index, set3_index, set3_size]
		Code.putFalseJump(Code.eq, 0); // if (set3_index != set3_size), don't insert element
		int inc_set3_index2 = Code.pc - 2;
		// Stack: [set2[set2_index], set3_index]
		
		// Remove unnecessary value from stack
		Code.put(Code.dup_x1); // Stack: [set3_index, set2[set2_index], set3_index]
		Code.put(Code.pop);
		Code.put(Code.pop); // Stack: [set3_index]
		
		// Insert element to set3
		Code.load(set3_obj); // Stack: [set3_index, set3_addr]
		Code.put(Code.dup_x1); // Stack: [set3_addr, set3_index, set3_addr]
		Code.put(Code.pop); // Stack: [set3_addr, set3_index]
		Code.put(Code.dup_x1); // Stack: [set3_index, set3_addr, set3_index]
		Code.load(set2_obj); // Stack: [set3_index, set3_addr, set3_index, set2_addr]
		Code.load(temp1_obj); // Stack: [set3_index, set3_addr, set3_index, set2_addr, set2_index]
		Code.put(Code.aload); // Stack: [set3_index, set3_addr, set3_index, set2[set2_index]]
		Code.put(Code.astore);
		// Stack: [set3_index]
		
		// Increment set3_size
		Code.load(set3_size_obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(set3_size_obj);
		// Stack: [set3_index]
		
		// Element is added, go to next set2 element
		Code.putJump(0);
		int set3_loop_end2 = Code.pc - 2;
		
		Code.fixup(inc_set3_index2);
		
		// Check if that element is on current set3_index
		Code.load(set3_obj); // Stack: [set2[set2_index], set3_index, set3_addr]
		Code.put(Code.dup_x1); // Stack: [set2[set2_index], set3_addr, set3_index, set3_addr]
		Code.put(Code.pop); // Stack: [set2[set2_index], set3_addr, set3_index]
		Code.put(Code.dup_x2); // Stack: [set3_index, set2[set2_index], set3_addr, set3_index]
		Code.put(Code.aload); // Stack: [set3_index, set2[set2_index], set3[set3_index]]
		Code.putFalseJump(Code.ne, 0); // if (set2[set2_index] == set3[set3_index]), element exists, continue set2 loop
		int inc_set2_index = Code.pc - 2;
		// Stack: [set3_index]
		
		// Increment set3 index
		Code.loadConst(1);
		Code.put(Code.add);
		// Stack: [set3_index + 1]
		
		// Jump to next loop iteration
		Code.putJump(set3_loop_start2);
		
		Code.fixup(set3_loop_end2);
		Code.fixup(inc_set2_index);
		
		// Emptying the stack
		Code.put(Code.pop); // Stack: []
		
		
		// Increment set2 index
		Code.load(temp1_obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(temp1_obj);
		// Stack: []

		// Keep iterating over the set2
		Code.putJump(set2_loop_start);
		
		Code.fixup(exit_union1);
		Code.fixup(exit_union2);
		
		// Emptying the stack
		Code.put(Code.pop);
		Code.put(Code.pop);
		// Stack: []
		
		Code.fixup(set2_loop_exit); // It's here to avoid popping from empty stack
		
		// Store set3_size value as last element of set3
		Code.load(set3_obj);
		Code.load(set3_obj); // Stack: [set3_addr, set3_addr]
		Code.put(Code.arraylength); // Stack: [set3_addr, set3_len]
		Code.loadConst(1);
		Code.put(Code.sub); // Stack: [set3_addr, set3_len - 1]
		Code.load(set3_size_obj); // Stack: [set3_addr, set3_len - 1, set3_size]
		Code.put(Code.astore);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	//------------------------ Function(Method) declarations ------------------------
	@Override
	public void visit(MethodSignatureTypeNameVoid method_void) {
		method_void.obj.setAdr(Code.pc); // Save the starting address of function
		if (method_void.getI1().equalsIgnoreCase("main")) {
			this.main_pc = Code.pc;
		}
		
		Code.put(Code.enter);
		Code.put(method_void.obj.getLevel()); // b1 - Level field saves number of formal parameters
		Code.put(method_void.obj.getLocalSymbols().size()); // b2 - number of formal + local parameters
	}
	
	@Override
	public void visit(MethodSignatureTypeNameType method_type) {
		method_type.obj.setAdr(Code.pc); // Save the starting address of function
		if (method_type.getI2().equalsIgnoreCase("main")) {
			this.main_pc = Code.pc;
		}
		
		Code.put(Code.enter);
		Code.put(method_type.obj.getLevel()); //b1 - Level field saves number of formal parameters
		Code.put(method_type.obj.getLocalSymbols().size()); // b2 - number of formal + local parameters
	}
	
	@Override
	public void visit(MethodDecl method_decl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	//------------------------ Factor declarations ------------------------	
	@Override
	public void visit(FactorNumConst factor_num_const) {
		Code.loadConst(factor_num_const.getN1()); // Push on the expr stack
	}
	
	@Override
	public void visit(FactorCharConst factor_char_const) {
		Code.loadConst(factor_char_const.getC1()); // Push on the expr stack
	}
	
	@Override
	public void visit(FactorBoolConst factor_bool_const) {
		Code.loadConst(factor_bool_const.getB1()); // Push on the expr stack
	}
	
	@Override
	public void visit(FactorDesignator factor_designator) { // Only in this case we push designator to expr stack (it's on the right side of "=")
		Code.load(factor_designator.getDesignator().obj);
	}
	
	@Override
	public void visit(FactorDesignatorMinus factor_designator) { // Only in this case we push designator to expr stack (it's on the right side of "=")
		Code.load(factor_designator.getDesignator().obj);
		Code.put(Code.neg); // Negate top of the stack
	}
	
	@Override
	public void visit(FactorNumConstMinus factor_num_const) {
		Code.loadConst(factor_num_const.getN1()); // Push on the expr stack
		Code.put(Code.neg); // Negate top of the stack
	}
	
	@Override
	public void visit(FactorNewExpr factor_new_expr) {
		// If we are allocating new set, than its size needs 1 more field for storing current size while adding to set
		if (factor_new_expr.getType().struct.equals(set_type)) {
			// Size is already on the stack, we just increment it
			Code.loadConst(1);
			Code.put(Code.add);
		}

		Code.put(Code.newarray);
		if (factor_new_expr.getType().struct.equals(Tab.charType)) {
			Code.put(0);
		}
		else {
			Code.put(1);
		}
	}
	
	@Override
	public void visit(FactorDesignatorActPars factor_act_pars) {
		// relative offset from where we are now and where the function is
		int offset = factor_act_pars.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	//------------------------ Designator declarations ------------------------
	@Override
	public void visit(DesignatorArrayName designator_arr_name) {
		Code.load(designator_arr_name.obj); // Pushing the address of an array to the expr stack
	}
	
	@Override
	public void visit(DesignatorAssignOperation designator_assign) {
		Code.store(designator_assign.getDesignator().obj); // Popping from stack into designator (variable)
	}
	
	//------------------------ Term declarations ------------------------
	@Override
	public void visit(TermMulopFactorListMul term_mulop_factor_list_mul) {
		if (term_mulop_factor_list_mul.getMulop() instanceof MultiplyOp) {
			Code.put(Code.mul);
		}
		else if (term_mulop_factor_list_mul.getMulop() instanceof DivideOp) {
			Code.put(Code.div);
		}
		else if (term_mulop_factor_list_mul.getMulop() instanceof ModuoOp) {
			Code.put(Code.rem);
		}
	}
	
	//------------------------ Expression declarations ------------------------
	@Override
	public void visit(ExprMap expr) {
		//Obj func_obj = expr.getDesignator().obj.getAdr(); // Starting address of function
		Obj func_obj = new Obj(Obj.Con, expr.getDesignator().obj.getName(), Tab.intType, expr.getDesignator().obj.getAdr(), 0);
		Obj arr_obj_map = expr.getDesignator1().obj; // Starting address of array

		// Initialize sum to 0
		Code.loadConst(0);
		
		// arr_ind = 0
		Code.loadConst(0);
		// Stack: [sum, arr_index]
		
		// Iterate over the array
		int arr_loop_start_map = Code.pc;

		// Check if we finished iterating over the array
		Code.put(Code.dup); // Stack: [sum, arr_index, arr_index]
		Code.load(arr_obj_map); // Stack: [sum, arr_index, arr_index, arr_addr]
		Code.put(Code.arraylength); // Stack: [sum, arr_index, arr_index, arr_len]
		Code.putFalseJump(Code.lt, 0); // if (arr_index >= arr_length), exit array loop
		int arr_loop_end_map = Code.pc - 2;
		// Stack: [sum, arr_index]
		
		// Take element from array
		Code.put(Code.dup); // Stack: [sum, arr_index, arr_index]
		Code.load(arr_obj_map); // Stack: [sum, arr_index, arr_index, arr_addr]
		Code.put(Code.dup_x1); // Stack: [sum, arr_index, arr_addr, arr_index, arr_addr]
		Code.put(Code.pop); // Stack: [sum, arr_index, arr_addr, arr_index]
		Code.put(Code.aload); // Stack: [sum, arr_index, arr[arr_index]]
		
		// Call function using that element as parameter
		int offset = func_obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset); // // offset = func - pc
		
		// Function returns value, it's on the stack
		// Stack: [sum, arr_index, ret_val]
		
		// Add it to current sum variable
		Code.put(Code.dup_x2); // Stack: [ret_val, sum, arr_index, ret_val]
		Code.put(Code.pop); // Stack: [ret_val, sum, arr_index]
		Code.put(Code.dup_x2); // Stack: [arr_index, ret_val, sum, arr_index]
		Code.put(Code.pop); // Stack: [arr_index, ret_val, sum]
		Code.put(Code.add); // Stack: [arr_index, new_sum]
		
		// Reversing the stack
		Code.put(Code.dup_x1); // Stack: [new_sum, arr_index, new_sum]
		Code.put(Code.pop); // Stack: [new_sum, arr_index]
		
		// Increment array index
		Code.loadConst(1);
		Code.put(Code.add); // Stack: [new_sum, arr_index + 1]
		
		// Jump back to loop
		Code.putJump(arr_loop_start_map);
		
		Code.fixup(arr_loop_end_map);

		// Stack: [sum, arr_index]
		Code.put(Code.pop); // Calculated value is left on the stack
		// Stack: [sum]
	}
	
	@Override
	public void visit(ExprAddopTermListAdd expr_addop_term_list_add) {
		if (expr_addop_term_list_add.getAddop() instanceof PlusOp) {
			Code.put(Code.add);
		}
		else if (expr_addop_term_list_add.getAddop() instanceof MinusOp) {
			Code.put(Code.sub);
		}
	}
	
	//------------------------ Designator Statement declarations ------------------------
	
	@Override
	public void visit(DesignatorPostIncrement designator_post_inc) {
		if (designator_post_inc.getDesignator().obj.getKind() == Obj.Elem) { // Use case: for arr[i]++
			Code.put(Code.dup2); // aload will use first 2 values, and astore will use second 2 values
		}
		else if (designator_post_inc.getDesignator().obj.getKind() == Obj.Fld) {
			Code.put(Code.dup);
		}
		Code.load(designator_post_inc.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designator_post_inc.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorPostDecrement designator_post_dec) {
		if (designator_post_dec.getDesignator().obj.getKind() == Obj.Elem) { // Use case: for arr[i]--
			Code.put(Code.dup2); // aload will use first 2 values, and astore will use second 2 values
		}
		else if (designator_post_dec.getDesignator().obj.getKind() == Obj.Fld) {
			Code.put(Code.dup);
		}
		Code.load(designator_post_dec.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(designator_post_dec.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorFunctionCallActPars designator_func_call_pars) {
		// Formal pars are pushed on the stack, so if we want to add another formal par just before function call
		// we can add it here. For add and addAll we need additional formal parameters.
		if (designator_func_call_pars.getDesignator().obj.getName().equals("add")) {
            // Passing size as a third parameter
            Code.load(new Obj(Obj.Var, "size", Tab.intType, 0, 0));
        }
		else if (designator_func_call_pars.getDesignator().obj.getName().equals("addAll")) {
            Code.load(new Obj(Obj.Var, "temp1", Tab.intType, 0, 0));
            Code.load(new Obj(Obj.Var, "temp2", Tab.intType, 0, 0));
            Code.load(new Obj(Obj.Var, "temp3", Tab.intType, 0, 0));
		}
//		else if (designator_func_call_pars.getDesignator().obj.getName().equals("union")) {
//            Code.load(new Obj(Obj.Var, "temp1", Tab.intType, 0, 0));
//            Code.load(new Obj(Obj.Var, "temp2", Tab.intType, 0, 0));
//            Code.load(new Obj(Obj.Var, "temp3", Tab.intType, 0, 0));
//		}
		
		// relative offset from where we are now and where the function is
		int offset = designator_func_call_pars.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		
		if (designator_func_call_pars.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}
	
	@Override
	public void visit(DesignatorAssignopSetop designator_assign_setop) {
		// First pass parameters on the stack
		Code.load(designator_assign_setop.getDesignator1().obj);
		Code.load(designator_assign_setop.getDesignator2().obj);
		Code.load(designator_assign_setop.getDesignator().obj);
		Code.load(new Obj(Obj.Var, "temp1", Tab.intType, 0, 0));
        Code.load(new Obj(Obj.Var, "temp2", Tab.intType, 0, 0));
        Code.load(new Obj(Obj.Var, "temp3", Tab.intType, 0, 0));
        
        // Then call union function
        Obj union_meth = Tab.find("union");

        // Relative offset from where we are now and where the function is
 		int offset = union_meth.getAdr() - Code.pc;
 		Code.put(Code.call);
 		Code.put2(offset);
	}
	
	//------------------------ Statement declarations ------------------------
	@Override
	public void visit(StatementPrintExpr statement_print_expr) {
		Struct expr_type = statement_print_expr.getExpr().struct;
	
		if (expr_type.equals(set_type)) {
	        // Stack: [array_addr], address of an array is already on the stack
	        
	        Code.put(Code.dup); // Stack: [array_addr, array_addr]
	        Code.put(Code.dup); // Stack: [array_addr, array_addr, array_addr]
	        Code.put(Code.arraylength); // Stack: [array_addr, array_addr, len]
	        // Decrement length by 1, to get size element from set
	        Code.loadConst(1);
	        Code.put(Code.sub); // Stack: [array_addr, array_addr, len - 1]
	        Code.put(Code.aload); // Stack: [array_addr, size]

	        Code.loadConst(0); // Start index at 0
	        // Stack: [array_addr, len, index]

	        int loop_start = Code.pc; // Mark start of loop

	        // Check if len > index
	        Code.put(Code.dup2); // Keep both values
	        Code.putFalseJump(Code.gt, 0); // Jump to exit if len <= index
	        int exit_jump_addr = Code.pc - 2; // Store for later backpatching
	        // Stack: [array_addr, len, index]

	     // Correct stack manipulation for `aload`
	        Code.put(Code.dup_x1); // Stack: [array_addr, index, len, index]
	        Code.put(Code.pop);    // Stack: [array_addr, index, len]
	        Code.put(Code.dup_x2); // Stack: [len, array_addr, index, len]
	        Code.put(Code.pop);    // Stack: [len, array_addr, index]
	        Code.put(Code.dup2);   // Stack: [len, array_addr, index, array_addr, index]
	        
	        Code.put(Code.aload); // Load element
	        // Stack: [len, array_addr, index, val]

	        Code.loadConst(0); // Print width field
	        Code.put(Code.print);
	        // Stack: [len, array_addr, index]

	        // Print space if not last element
	        Code.put(Code.dup_x1); // Stack: [len, index, array_addr, index]
	        Code.put(Code.pop);    // Stack: [len, index, array_addr]
	        Code.put(Code.dup_x2); // Stack: [array_addr, len, index, array_addr]
	        Code.put(Code.pop); // Stack: [array_addr, len, index]
	        Code.put(Code.dup_x1); // Stack: [array_addr, index, len, index]
	        Code.put(Code.dup2); // Stack: [array_addr, index, len, index, len, index]
	        Code.put(Code.sub); // len - index
	        // Stack: [array_addr, index, len, index, sub_result]
	        Code.loadConst(1); // Stack: [array_addr, index, len, index, sub_result, 1]
	        Code.putFalseJump(Code.gt, 0); // Skip printing space if last element (if (len - index) > 1 {print space})
	        int skip_space_addr = Code.pc - 2;
	        // Stack: [array_addr, index, len, index]
	        
	        Code.loadConst(' '); // Ascii code for space
	        Code.loadConst(0); // width field
	        Code.put(Code.bprint);

	        Code.fixup(skip_space_addr); // Skip space printing for last element
	        // Stack: [array_addr, index, len, index]

	        // Increment index
	        Code.loadConst(1);
	        Code.put(Code.add);
	        // Stack: [array_addr, index, len, index + 1]
	        Code.put(Code.dup_x1); // Stack: [array_addr, index, index + 1, len, index + 1]
	        Code.put(Code.pop); // Stack: [array_addr, index, index + 1, len]
	        Code.put(Code.dup_x2); // Stack: [array_addr, len, index, index + 1, len]
	        Code.put(Code.pop); // Stack: [array_addr, len, index, index + 1]
	        Code.put(Code.dup_x1); // Stack: [array_addr, len, index + 1, index, index + 1]
	        Code.put(Code.pop); // Stack: [array_addr, len, index + 1, index]
	        Code.put(Code.pop); // Stack: [array_addr, len, index + 1]

	        // Jump to loop start
	        Code.putJump(loop_start);

	        // Fix exit jump
	        Code.fixup(exit_jump_addr);
	        
	        // Pop the leftover values from stack (emptying the stack)
	        Code.put(Code.pop);
	        Code.put(Code.pop);
	        Code.put(Code.pop);
		}
		else if (expr_type.equals(Tab.charType)) {
			Code.loadConst(0); // width field of print
			Code.put(Code.bprint);
		}
		else {
			Code.loadConst(0); // width field of print
			Code.put(Code.print);
		}
	}
	
	@Override
	public void visit(StatementPrintExprNumber statement_print_expr_num) {
		// Same code as for standard print except it has more spaces depending on second parameter
		Struct expr_type = statement_print_expr_num.getExpr().struct;
		
		if (expr_type.equals(set_type)) {
	        // Stack: [array_addr], address of an array is already on the stack
	        
	        Code.put(Code.dup); // Stack: [array_addr, array_addr]
	        Code.put(Code.dup); // Stack: [array_addr, array_addr, array_addr]
	        Code.put(Code.arraylength); // Stack: [array_addr, array_addr, len]
	        // Decrement length by 1, to get size element from set
	        Code.loadConst(1);
	        Code.put(Code.sub); // Stack: [array_addr, array_addr, len - 1]
	        Code.put(Code.aload); // Stack: [array_addr, size]

	        Code.loadConst(0); // Start index at 0
	        // Stack: [array_addr, len, index]

	        int loop_start = Code.pc; // Mark start of loop

	        // Check if len > index
	        Code.put(Code.dup2); // Keep both values
	        Code.putFalseJump(Code.gt, 0); // Jump to exit if len <= index
	        int exit_jump_addr = Code.pc - 2; // Store for later backpatching
	        // Stack: [array_addr, len, index]

	     // Correct stack manipulation for `aload`
	        Code.put(Code.dup_x1); // Stack: [array_addr, index, len, index]
	        Code.put(Code.pop);    // Stack: [array_addr, index, len]
	        Code.put(Code.dup_x2); // Stack: [len, array_addr, index, len]
	        Code.put(Code.pop);    // Stack: [len, array_addr, index]
	        Code.put(Code.dup2);   // Stack: [len, array_addr, index, array_addr, index]
	        
	        Code.put(Code.aload); // Load element
	        // Stack: [len, array_addr, index, val]

	        Code.loadConst(statement_print_expr_num.getN2()); // Print width field
	        Code.put(Code.print);
	        // Stack: [len, array_addr, index]

	        // Print space if not last element
	        Code.put(Code.dup_x1); // Stack: [len, index, array_addr, index]
	        Code.put(Code.pop);    // Stack: [len, index, array_addr]
	        Code.put(Code.dup_x2); // Stack: [array_addr, len, index, array_addr]
	        Code.put(Code.pop); // Stack: [array_addr, len, index]
	        Code.put(Code.dup_x1); // Stack: [array_addr, index, len, index]
	        Code.put(Code.dup2); // Stack: [array_addr, index, len, index, len, index]
	        Code.put(Code.sub); // len - index
	        // Stack: [array_addr, index, len, index, sub_result]
	        Code.loadConst(1); // Stack: [array_addr, index, len, index, sub_result, 1]
	        Code.putFalseJump(Code.gt, 0); // Skip printing space if last element (if (len - index) > 1 {print space})
	        int skip_space_addr = Code.pc - 2;
	        // Stack: [array_addr, index, len, index]
	        
	        Code.loadConst(' '); // Ascii code for space
	        Code.loadConst(0); // width field
	        Code.put(Code.bprint);

	        Code.fixup(skip_space_addr); // Skip space printing for last element
	        // Stack: [array_addr, index, len, index]

	        // Increment index
	        Code.loadConst(1);
	        Code.put(Code.add);
	        // Stack: [array_addr, index, len, index + 1]
	        Code.put(Code.dup_x1); // Stack: [array_addr, index, index + 1, len, index + 1]
	        Code.put(Code.pop); // Stack: [array_addr, index, index + 1, len]
	        Code.put(Code.dup_x2); // Stack: [array_addr, len, index, index + 1, len]
	        Code.put(Code.pop); // Stack: [array_addr, len, index, index + 1]
	        Code.put(Code.dup_x1); // Stack: [array_addr, len, index + 1, index, index + 1]
	        Code.put(Code.pop); // Stack: [array_addr, len, index + 1, index]
	        Code.put(Code.pop); // Stack: [array_addr, len, index + 1]

	        // Jump to loop start
	        Code.putJump(loop_start);

	        // Fix exit jump
	        Code.fixup(exit_jump_addr);
	        
	        // Pop the leftover values from stack (emptying the stack)
	        Code.put(Code.pop);
	        Code.put(Code.pop);
	        Code.put(Code.pop);
		}
		else if (expr_type.equals(Tab.charType)) {
			Code.loadConst(statement_print_expr_num.getN2()); // width field of print
			Code.put(Code.bprint);
		}
		else {
			Code.loadConst(statement_print_expr_num.getN2()); // width field of print
			Code.put(Code.print);
		}
	}
	
	@Override
	public void visit(StatementReturnEmpty statement_return_empty) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(StatementReturnExpr statement_return_expr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(StatementRead statement_read) {
		// Depending on designator type we call bread or read
		if (statement_read.getDesignator().obj.getType().equals(Tab.charType)) {
			Code.put(Code.bread);
		}
		else {
			Code.put(Code.read);
		}
		Code.store(statement_read.getDesignator().obj);
	}
	
	@Override
	public void visit(EmptyStatementArrayBrackets1 else_not_exist) { // Else branch doesn't even exist
		Code.fixup(skip_then.pop());
	}
	
	@Override
	public void visit(StatementArrayBrackets1 else_exist) { // Else branch exists
		// All those conditions that are true need to skip this, and those that are not true need to go inside this
		Code.fixup(skip_else.pop()); // fixing the addresses of conditions that are true and skipped "else"
	}
	
	@Override
	public void visit(ElseHelper else_helper) {
		// ALl conditions that are true are skipping ELSE
		Code.putJump(0);
		// All conditions that are not true are continuing execution here
		skip_else.push(Code.pc - 2);
		Code.fixup(skip_then.pop());
	}
	
	@Override
	public void visit(StatementBreak statement_break) {
		Code.putJump(0); // Unconditional jump
		break_jumps.peek().add(Code.pc - 2); // Get the list from the top of the stack and put the address of the jump
	}
	
	@Override
	public void visit(StatementContinue statement_continue) {
		Code.putJump(0); // Unconditional jump
		continue_jumps.peek().add(Code.pc - 2); // Get the list from the top of the stack and put the address of the jump
	}
	
	//------------------------ Do-While loops ------------------------
	@Override
	public void visit(WhileHelper while_helper) {
		while (continue_jumps.peek().isEmpty() == false) {
			Code.fixup(continue_jumps.peek().remove(0));
		}
			
		continue_jumps.pop();
	}
	
	@Override
	public void visit(DoHelper do_helper) {
		do_start.push(Code.pc);
		break_jumps.push(new ArrayList<Integer>());
		continue_jumps.push(new ArrayList<Integer>());
	}
	
	@Override
	public void visit(StatementDoWhileCond statement_do_while) {
		Code.putJump(do_start.pop());
		Code.fixup(skip_then.pop());
		
		while (break_jumps.peek().isEmpty() == false) {
			Code.fixup(break_jumps.peek().remove(0));
		}

		break_jumps.pop();
	}
	
	@Override
	public void visit(StatementDoWhileEmpty statement_do_while) {
		Code.putJump(do_start.pop());

		while(!break_jumps.peek().isEmpty()) {
			Code.fixup(break_jumps.peek().remove(0));
		}
		break_jumps.pop();
	}
	
	@Override
	public void visit(StatementDoWhileCondDesStatement statement_do_while) { // example usage: do{} while(x<3, x++)
		Code.putJump(do_start.pop());
		Code.fixup(skip_then.pop());
		
		while(!break_jumps.peek().isEmpty()) {
			Code.fixup(break_jumps.peek().remove(0));
		}
		break_jumps.pop();
	}
	
	//------------------------ Condition declarations ------------------------
	@Override
	public void visit(CondFactExpr cond_fact_expr) {
		// We have to put something to compare with (and that is 0 in our case)
		Code.loadConst(0);
		// if this condition is not true, than we need to go to the next "or" if it exists
		Code.putFalseJump(Code.ne, 0);
		// Condition was true, we continue until the next "and"
		skip_cond_fact.push(Code.pc - 2);
	}
	
	@Override
	public void visit(CondFactRelopExpr cond_fact_relop_expr) {
		Relop relop = cond_fact_relop_expr.getRelop();
		int relop_code;
		if (relop instanceof EqualOp) {
			relop_code = Code.eq;
		}
		else if(relop instanceof NotEqualOp) {
			relop_code = Code.ne;
		}
		else if(relop instanceof GreaterOp) {
			relop_code = Code.gt;
		}
		else if(relop instanceof GreaterEqualOp) {
			relop_code = Code.ge;
		}
		else if(relop instanceof LessOp) {
			relop_code = Code.lt;
		}
		else if(relop instanceof LessEqualOp) {
			relop_code = Code.le;
		}
		else {
			relop_code = 0; // Error, won't happen
		}
		// if this condition is not true, than we need to go to the next "or" if it exists
		Code.putFalseJump(relop_code, 0);
		// Condition was true, we continue until the next "and"
		skip_cond_fact.push(Code.pc - 2);
	}
	
	@Override
	public void visit(CondTerm cond_term) { // This is end of one "or" and start of the next one also
		// All "and" are true (we visited 1 whole "or") so we don't need to check further since 1 "or" is true, and that means whole "if" is true
		Code.putJump(0); // jump to "then" (address is 0 because we still don't know the exact address (will be patched later))
		skip_condition.push(Code.pc - 2); // we put addresses of conditions that are true, and they need to go to "then"
		// All those conditions (cond fact (and)) that were not true, now need to continue checking next "or", so we patch addresses here
		while (skip_cond_fact.empty() == false) {
			Code.fixup(skip_cond_fact.pop());
		}
	}
	
	@Override
	public void visit(Condition condition) {
		// We visited last "or"
		// All those conditions that were not true need to go to the else branch
		Code.putJump(0); // jump to "else" (address is 0 because we still don't know the exact address (will be patched later))
		skip_then.push(Code.pc - 2);
		// All those conditions that were true, now fixing their addresses to go to "then"
		while (skip_condition.empty() == false) {
			Code.fixup(skip_condition.pop());
		}	
	}
}