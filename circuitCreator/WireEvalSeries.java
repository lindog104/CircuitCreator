package circuitCreator;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class WireEvalSeries extends WireEvaluator {

	@Override
	protected boolean isInvalidWire(Wire wireA, Wire wireB) {
		boolean result = false;
		boolean[] matches = evaluateMatches(wireA, wireB);

		// If aOnebOne & aTwobTwo are true OR aOnebTwo & aTwobOne are true
		if ((matches[0] && matches[1]) || (matches[2] && matches[3])) {
			result = true;
		}

		return result;
	}

	@Override
	protected boolean isCircuitComplete() {
		boolean result = true;

		if (wires.size() >= 2) {
			sortForSource();

			if (wires.get(0).getNode(0) instanceof Source && wires.get(wires.size() - 1).getNode(1) instanceof Ground) {
				int count = 1;
				for (int i = 0; i < wires.size() - 1; i++) {
					if (!(wires.get(i).getNode(1).equals(wires.get(count).getNode(0)))) {
						result = false;
					}
					count++;
				}
			} else {
				result = false;
			}
		} else {
			result = false;
		}

		return result;
	}

	@Override
	protected void assignPotentials() {
		assignWireVoltages();
		assignPartials();
		
		for (int i = 0; i < wires.size(); i++) {
			if (wires.get(i).getVoltage() > 0) {
				wires.get(i).getLine().setStroke(Color.RED);
			} else if (wires.get(i).getVoltage() == 0) {
				wires.get(i).getLine().setStroke(Color.GREEN);
			} else {
				wires.get(i).getLine().setStroke(Color.BLACK);
			}
		}
	}

	private void sortForSource() {
		ArrayList<Wire> sortedWires = new ArrayList<>();
		int size = wires.size();

		for (int i = size - 1; i > -1; i--) {
			if (wires.get(i).containsSource()) {
				if (wires.get(i).getNode(1) instanceof Source) {
					wires.get(i).swapNodes();
				}
				sortedWires.add(0, wires.get(i));
				wires.remove(i);

			} else if (wires.get(i).containsGround()) {
				if (wires.get(i).getNode(0) instanceof Ground) {
					wires.get(i).swapNodes();
				}
				sortedWires.add(wires.get(i));
				wires.remove(i);
			}
		}

		for (int j = 0; j < wires.size(); j++) {
			sortedWires = sortAndAdd(sortedWires, wires.get(j));

		}

		System.out.println("After sorting");
		for (int k = 0; k < sortedWires.size(); k++) {
			System.out.println(sortedWires.get(k).printComponent());
		}

		wires = sortedWires;
	}

	private void assignWireVoltages() {
		for (int i = 0; i < wires.size(); i++) {
			wires.get(i).setVoltage(-1);
			wires.get(i).getLine().getStrokeDashArray().setAll(100.0);
		}
		assignNegative();
		assignPositive();
	}

	private void assignNegative() {
		boolean foundOpenSwitch = false;
		int backCount = wires.size() - 2;

		wires.get(wires.size() - 1).setVoltage(0);

		while (!foundOpenSwitch && backCount > 0) {
			if (!(wires.get(backCount).getNode(1) instanceof Switch)
					|| (!((Switch) wires.get(backCount).getNode(1)).isOpen())) {

				wires.get(backCount).setVoltage(0);
			} else if ((wires.get(backCount).getNode(1) instanceof Switch)
					&& (((Switch) wires.get(backCount).getNode(1)).isOpen())) {

				foundOpenSwitch = true;
			}
			backCount--;
		}

	}

	private void assignPositive() {
		boolean foundOpenSwitch = false;
		double totalVolts = wires.get(0).getNode(0).getVoltage();
		int count = 1;

		wires.get(0).setVoltage(totalVolts);
		while (!foundOpenSwitch && count < wires.size()) {
			if (!(wires.get(count).getNode(0) instanceof Switch)
					|| (!((Switch) wires.get(count).getNode(0)).isOpen())) {

				totalVolts -= wires.get(count).getNode(0).getVoltage();
				wires.get(count).setVoltage(totalVolts);
			} else if ((wires.get(count).getNode(0) instanceof Switch)
					&& (((Switch) wires.get(count).getNode(0)).isOpen())) {

				foundOpenSwitch = true;
			}
			count++;
		}

	}
	
	private void assignPartials() {
		
		for (int i = 1; i < wires.size(); i++) {
			if ((wires.get(i).getVoltage() > 0) && (wires.get(i).getVoltage() < wires.get(0).getVoltage())) {
				wires.get(i).getLine().getStrokeDashArray().setAll(10.0);
			}
		}
	}

}
