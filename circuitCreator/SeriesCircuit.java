package circuitCreator;

public class SeriesCircuit extends Circuit {
	
	public SeriesCircuit() {
		circuit[(size - 1) / 2][0] = new Source(sourceVoltage);
		we = new WireEvalSeries();
	}
	
	
	
	@Override
	public boolean computeCircuit() {
		if (we.isCircuitComplete()) {
			double totalResistance = 0.0;
			double circuitCurrent = 0.0;

			for (int i = 1; i < we.getSize(); i++) {
				totalResistance += we.getWire(i).getNode(0).getResistance();
			}
			System.out.println(totalResistance);

			we.getWire(0).getNode(0).setResistance(totalResistance);
			we.getWire(0).getNode(0).calcCurrent();
			circuitCurrent = we.getWire(0).getNode(0).getCurrent();

			for (int i = 1; i < we.getSize(); i++) {
				we.getWire(i).getNode(0).setCurrent(circuitCurrent);
				we.getWire(i).getNode(0).calcVoltage();
				we.getWire(i).getNode(0).isActive();
			}
			return true;
		} else {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (circuit[i][j] != null) {
						resetComponent(circuit[i][j]);
						circuit[i][j].isActive();
					}
				}
			}
			return false;
		}
	}

}
