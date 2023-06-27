package circuitCreator;

import java.util.ArrayList;

public class ParallelCircuit extends Circuit {

	public ParallelCircuit() {
		circuit[(size - 1) / 2][0] = new Source(sourceVoltage);
		circuit[(size - 1) / 2][size - 1] = new Ground();
		we = new WireEvalParallel();
	}

	@Override
	protected boolean computeCircuit() {
		boolean result = false;
		
		if(we.isCircuitComplete()) {
			ArrayList<Wire> loops = ((WireEvalParallel) we).getLoops();
			double voltage = loops.get(0).getNode(0).getVoltage();
			double totalCurrent = 0.0;
			int numOfLoops = loops.size() / 2;
			
			System.out.println(""+numOfLoops);
			for (int k = 0; k < loops.size(); k++) {
				System.out.println(loops.get(k).printComponent());
			}
			
			for (int i = 0; i < loops.size(); i+=2) {
				loops.get(i).getNode(1).setVoltage(voltage);
				loops.get(i).getNode(1).calcCurrent();
				loops.get(i).getNode(1).isActive();
				totalCurrent += loops.get(i).getNode(1).getCurrent();
			}
			
			
			loops.get(0).getNode(0).setCurrent(totalCurrent);
			loops.get(0).getNode(0).calcResistance();
			
			result = true;
		}
		
		return result;
	}
}
