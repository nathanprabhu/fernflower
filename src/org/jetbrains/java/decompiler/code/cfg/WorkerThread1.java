package org.jetbrains.java.decompiler.code.cfg;

import java.util.List;
import java.util.Map;

import org.jetbrains.java.decompiler.code.CodeConstants;
import org.jetbrains.java.decompiler.code.Instruction;
import org.jetbrains.java.decompiler.code.JumpInstruction;
import org.jetbrains.java.decompiler.code.SwitchInstruction;
public class WorkerThread1 implements Runnable,CodeConstants {
	  private int i;
	  private List<BasicBlock> lstbb;
	  private Map<Integer,BasicBlock> mapInstrBlocks;
	  
	  public WorkerThread1(int i,List<BasicBlock> lstbb,Map<Integer,BasicBlock> mapInstrBlocks){
		  System.out.println("Worker received thread"+i);
		  this.i=i;
		  this.lstbb=lstbb;
		  this.mapInstrBlocks=mapInstrBlocks;
	  }
	  
	  public void run(){
		  BasicBlock block = lstbb.get(i);
		  
	      Instruction instr = block.getLastInstruction();

	      boolean fallthrough = instr.canFallthrough();
	      BasicBlock bTemp;

	      switch (instr.group) {
	        case GROUP_JUMP:
	          int dest = ((JumpInstruction)instr).destination;
	          bTemp = mapInstrBlocks.get(dest);
	          block.addSuccessor(bTemp);

	          break;
	        case GROUP_SWITCH:
	          SwitchInstruction sinstr = (SwitchInstruction)instr;
	          int[] dests = sinstr.getDestinations();

	          bTemp = mapInstrBlocks.get(((SwitchInstruction)instr).getDefaultdest());
	          block.addSuccessor(bTemp);
	          for (int j = 0; j < dests.length; j++) {
	            bTemp = mapInstrBlocks.get(dests[j]);
	            block.addSuccessor(bTemp);
	          }
	      }

	      if (fallthrough && i < lstbb.size() - 1) {
	        BasicBlock defaultBlock = lstbb.get(i + 1);
	        block.addSuccessor(defaultBlock);
	      }
		  
	  }

}
