package circuitCreator;

import java.util.ArrayList;

import javafx.scene.shape.Line;

public class WireEvaluator {
	protected ArrayList<Wire> wires = new ArrayList<>();
	protected boolean newWireSaved;

	protected boolean addWire(Wire wire) {
		if (wires.isEmpty()) {

			wires.add(wire);
			System.out.println(wire.printComponent());
			newWireSaved = true;
		} else {
			newWireSaved = true;
			wires = sortAndAdd(wires, wire);
			System.out.println("\tWires: ");
			for (int j = 0; j < wires.size(); j++) {
				System.out.println(wires.get(j).printComponent());
			}
		}

		return newWireSaved;
	}

	protected boolean isCircuitComplete() {
		
		return false;
	}

	protected ArrayList<Wire> sortAndAdd(ArrayList<Wire> sortingWires, Wire wire) {
		int wiresSize = sortingWires.size();
		int count = 0;
		boolean[] matches;
		boolean wireUnsaved = true;

		while ((count < wiresSize) && wireUnsaved) {
			matches = evaluateMatches(sortingWires.get(count), wire);

			if (isInvalidWire(sortingWires.get(count), wire)) {
				wireUnsaved = false;
				newWireSaved = false;
				// If aOnebOne is true exclusively, swap the position of new wire's nodes and
				// save it in front of the matching wire
			} else if (matches[0] && !(matches[1] || matches[2] || matches[3])) {
				wire.swapNodes();
				sortingWires.add(count, wire);
				wireUnsaved = false;

				// If aTwobTwo is true exclusively, swap the position of new wire's nodes and
				// save it behind the matching wire
			} else if (matches[1] && !(matches[0] || matches[2] || matches[3])) {
				wire.swapNodes();
				sortingWires.add(count + 1, wire);
				wireUnsaved = false;

				// If aOnebTwo is true exclusively, save the new wire in front of the matching
				// wire
			} else if (matches[2] && !(matches[1] || matches[0] || matches[3])) {
				sortingWires.add(count, wire);
				wireUnsaved = false;

				// If aTwobOne is true exclusively, save the new wire behind the matching wire
			} else if (matches[3] && !(matches[1] || matches[2] || matches[0])) {
				sortingWires.add(count + 1, wire);
				wireUnsaved = false;

			}
			count++;
		}
		if (wireUnsaved) {
			sortingWires.add(wire);

		}
		return sortingWires;
	}

	protected void assignPotentials() {
		
	}
	
	protected boolean isInvalidWire(Wire wireA, Wire wireB) {

		return false;
	}

	protected boolean[] evaluateMatches(Wire wireA, Wire wireB) {
		// matches = {aOne&bOne, aTwo&bTwo, aOne&bTwo, aTwo&bOne};
		boolean[] matches = { false, false, false, false };

		// aOne&bOne
		if (wireA.getNode(0).equals(wireB.getNode(0))) {
			matches[0] = true;
		}

		// aTwo&bTwo
		if (wireA.getNode(1).equals(wireB.getNode(1))) {
			matches[1] = true;
		}

		// aOne&bTwo
		if (wireA.getNode(0).equals(wireB.getNode(1))) {
			matches[2] = true;
		}

		// aTwo&bOne
		if (wireA.getNode(1).equals(wireB.getNode(0))) {
			matches[3] = true;
		}

		return matches;
	}

	protected ArrayList<Line> removeWires(Component component) {
		ArrayList<Line> linesToRemove = new ArrayList<>();

		for (int i = wires.size() - 1; i > -1; i--) {
			Wire wire = wires.get(i);
			if (wire.getNode(0).equals(component) || wire.getNode(1).equals(component)) {
				linesToRemove.add(wire.getLine());
				wires.remove(i);
			}
		}

		return linesToRemove;
	}

	// Setters and Getters
	protected int getSize() {
		return wires.size();
	}

	protected Wire getWire(int index) {
		return wires.get(index);
	}

	protected ArrayList<Wire> getWires() {
		return wires;
	}

}
