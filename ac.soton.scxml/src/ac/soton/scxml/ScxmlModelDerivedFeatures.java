package ac.soton.scxml;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;

public class ScxmlModelDerivedFeatures {

	public static boolean isParallel(EObject scxmlElement) {
		return scxmlElement instanceof ScxmlParallelType;
	}

	public static boolean isState(EObject scxmlElement) {
		return scxmlElement instanceof ScxmlStateType;
	}

	public static boolean isFinal(EObject scxmlElement) {
		return scxmlElement instanceof ScxmlFinalType;
	}

	public static boolean isStateNode(EObject scxmlElement) {
		return scxmlElement instanceof ScxmlStateType
				|| scxmlElement instanceof ScxmlParallelType
				|| scxmlElement instanceof ScxmlFinalType;
	}

	public static boolean isScxmlOrStateNode(EObject scxmlElement) {
		return scxmlElement instanceof ScxmlScxmlType || isStateNode(scxmlElement);
	}

	public static boolean isCompoundState(ScxmlStateType state) {
		return state.eContents().stream().anyMatch(it -> isStateNode(it));
	}
	
	public static boolean isTransitionSource(EObject scxmlElement) {
		return scxmlElement instanceof ScxmlStateType
				|| scxmlElement instanceof ScxmlParallelType
				|| scxmlElement instanceof ScxmlInitialType
				|| scxmlElement instanceof ScxmlHistoryType;
	}

	public static List<EObject> getStateNodes(EObject scxmlElement) {
		if (!isScxmlOrStateNode(scxmlElement)) {
			throw new IllegalArgumentException("EObject " + scxmlElement + " is not an Scxml state node.");
		}

		return scxmlElement.eContents().stream().filter(it -> isStateNode(it)).collect(Collectors.toList());
	}

	public static EObject getFirstChildStateNode(EObject scxmlElement) {
		if (!(scxmlElement instanceof ScxmlScxmlType
				|| (scxmlElement instanceof ScxmlStateType && isCompoundState((ScxmlStateType) scxmlElement)))) {
			throw new IllegalArgumentException(
					"EObject " + scxmlElement + " cannot contain an initial state element or attribute.");
		}

		return scxmlElement.eContents().stream().filter(it -> isStateNode(it)).findFirst().get();
	}

	public static ScxmlStateType getParentState(EObject scxmlElement) {
		var parent = scxmlElement.eContainer();
		if (!(parent instanceof ScxmlStateType)) {
			throw new IllegalArgumentException(
					"The parent element of EObject " + scxmlElement + " is not an Scxml state: " + parent);
		}

		return (ScxmlStateType) parent;
	}
	
	public static EObject getTransitionSource(ScxmlTransitionType scxmlTransition) {
		var parent = scxmlTransition.eContainer();
		if (!isTransitionSource(parent)) {
			throw new IllegalArgumentException(
					"The parent element of transition " + scxmlTransition
					+ " is not a valid Scxml transition source type: " + parent);
		}

		return parent;
	}

	public static List<ScxmlTransitionType> getAllTransitions(ScxmlScxmlType scxmlRoot) {
		List<ScxmlTransitionType> allTransitions = new ArrayList<ScxmlTransitionType>();
		var iterator = scxmlRoot.eAllContents();
		while (iterator.hasNext()) {
			var element = iterator.next();
			if (element instanceof ScxmlTransitionType) {
				allTransitions.add((ScxmlTransitionType) element);
			}
		}
		return allTransitions;
	}

}
