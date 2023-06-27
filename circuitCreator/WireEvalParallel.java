package circuitCreator;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class WireEvalParallel extends WireEvaluator {
	private ArrayList<Wire> loops = new ArrayList<>();

	@Override
	protected boolean addWire(Wire wire) {

		newWireSaved = true;
		wires = sortAndAdd(wires, wire);
		System.out.println("\tWires: ");
		for (int j = 0; j < wires.size(); j++) {
			System.out.println(wires.get(j).printComponent());
		}

		return newWireSaved;
	}

	@Override
	protected boolean isInvalidWire(Wire wireA, Wire wireB) {
		boolean result = false;
//		boolean[] matches = evaluateMatches(wireA, wireB);

		if (!wireB.containsSource() && !wireB.containsGround()) {
			result = true;
		}

		if (wireB.containsSource() && wireB.containsGround()) {
			result = true;
		}

		return result;
	}

	@Override
	protected boolean isCircuitComplete() {
		boolean result = false;
		
		if (wires.size() >= 2) {
			sortForLoops();
			
			if (!loops.isEmpty()) {
				result = true;
			}
		}

		return result;
	}

	@Override
	protected ArrayList<Wire> sortAndAdd(ArrayList<Wire> sortingWires, Wire wire) {

		if (isDuplicate(wire) || isInvalidWire(wire, wire)) {
			newWireSaved = false;
		} else if (wire.getNode(0) instanceof Source) {
			sortingWires.add(0, wire);
		} else if (wire.getNode(1) instanceof Source) {
			wire.swapNodes();
			sortingWires.add(0, wire);
		} else if (wire.getNode(0) instanceof Ground) {
			wire.swapNodes();
			sortingWires.add(wire);
		} else {
			sortingWires.add(wire);
		}

		return sortingWires;
	}
	
	@Override
	protected void assignPotentials() {
		for (int i = 0; i < wires.size(); i++) {
			if (wires.get(i).containsSource()) {
				wires.get(i).getLine().setStroke(Color.RED);
			} else {
				wires.get(i).getLine().setStroke(Color.GREEN);
			}
		}
	}

	private void sortForLoops() {
		ArrayList<Wire> loopedWires = new ArrayList<>();
		ArrayList<Wire> checkedHeads = new ArrayList<>();

		System.out.println("\nLoops");
		while (!wires.isEmpty() && wires.get(0).getNode(0) instanceof Source) {
			Wire head = wires.get(0);
			wires.remove(0);

			for (int i = wires.size() - 1; i > -1; i--) {
				if (wires.get(i).getNode(0).equals(head.getNode(1))) {
					loopedWires.add(head);
					loopedWires.add(wires.get(i));
				}
			}

			checkedHeads.add(head);
		}

		for (int k = 0; k < loopedWires.size(); k++) {
			System.out.println(loopedWires.get(k).printComponent());
		}

		wires.addAll(0, checkedHeads);

		System.out.println("New Wires Array");
		for (int k = 0; k < wires.size(); k++) {
			System.out.println(wires.get(k).printComponent());
		}

		loops = loopedWires;
	}

	private boolean isDuplicate(Wire wire) {
		boolean result = false;

		for (int i = 0; i < wires.size(); i++) {
			boolean[] matches = evaluateMatches(wires.get(i), wire);
			if (matches[0] && matches[1]) {
				result = true;
			}
		}

		return result;
	}
	
	//Setters and Getters
	public ArrayList<Wire> getLoops() {
		return loops;
	}

}