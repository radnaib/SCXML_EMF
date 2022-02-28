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
		return scxmlElement instanceof ScxmlScxmlType
				|| isStateNode(scxmlElement);
	}
	
	public static boolean isCompoundState(ScxmlStateType state) {
		return state.eContents().stream().anyMatch(it ->
			isStateNode(it)
		);
	}
	
	public static List<EObject> getStateNodes(EObject scxmlElement) {
		if (!isScxmlOrStateNode(scxmlElement)) {
			throw new IllegalArgumentException(
					"EObject " + scxmlElement + " is not an Scxml state node.");
		}
		return scxmlElement.eContents().stream().filter(it ->
			isStateNode(it)
		).collect(Collectors.toList());
	}
	
	public static String getParentStateNodeId(ScxmlTransitionType scxmlTransition) {
		var parent = scxmlTransition.eContainer();
		if (parent instanceof ScxmlStateType) {
			var parentState = (ScxmlStateType)parent;
			var id = parentState.getId();
			return id;
		}
		else {
			return null;	// TODO Must work for ScxmlScxmlType, ScxmlParallelType and ScxmlInitialType, too.
		}
	}
	
	public static List<ScxmlTransitionType> getAllTransitions(ScxmlScxmlType scxmlRoot) {
		List<ScxmlTransitionType> allTransitions = new ArrayList<ScxmlTransitionType>();
		var iterator = scxmlRoot.eAllContents();
		while (iterator.hasNext()) {
			var element = iterator.next();
			if (element instanceof ScxmlTransitionType) {
				allTransitions.add((ScxmlTransitionType)element);
			}
		}
		return allTransitions;
	}
	
}
