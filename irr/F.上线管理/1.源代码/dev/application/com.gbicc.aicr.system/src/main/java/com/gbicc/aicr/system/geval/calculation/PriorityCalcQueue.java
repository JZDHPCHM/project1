package com.gbicc.aicr.system.geval.calculation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gbicc.aicr.system.geval.EvaluationException;
import com.gbicc.aicr.system.geval.Evaluator;


public class PriorityCalcQueue implements Cloneable {
	private static final Logger log =LoggerFactory.getLogger(PriorityCalcQueue.class);
	
	private IrsPara top;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void putObject(IrsPara vnode) {
		IrsPara node = vnode;
		if (top == null || top.level() >= vnode.getLevel()) {
			node.next(top);
			top = node;
		} else {
			IrsPara temp = top.next();
			IrsPara previous = top;
			while (temp != null) {
				if (temp.level() >= vnode.getLevel())
					break;
				previous = temp;
				temp = temp.next();
			}
			node.next(previous.next());
			previous.next(node);
		}
	}

	public IrsPara getIrsPara() {
		assert top != null;
		IrsPara nnode = top;
		// int result = top.value();
		top = top.next();
		return nnode;
	}

	public boolean isEmpty() {
		return top == null;
	}
	
	public static void main(String[] args) throws EvaluationException {
		 Evaluator evaluator = new Evaluator();
		 evaluator.putVariable("a", "-1.71027206");
		 evaluator.putVariable("b", "-6.67831215");
		 evaluator.putVariable("c", "-20.9818145");
		 evaluator.putVariable("d", "-16.28651887");
		 evaluator.putVariable("e", "-0.47686945");
		 evaluator.putVariable("f", "-2.6631818");
		 String result = evaluator.evaluate("#{a}+#{b}+#{c}+#{d}+#{e}+#{f}");
		 System.out.println(evaluator.getVariables().get("a"));
		 
		 
		 
		 
		 
		 System.out.println(result);
	}

}
