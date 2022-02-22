package ac.soton.scxml;

public class ScxmlModelDerivedFeatures {
	
	public static boolean isCompoundState(ScxmlStateType scxmlState) {
		return scxmlState.eContents().stream().anyMatch(it ->
			it instanceof ScxmlStateType ||
			it instanceof ScxmlParallelType ||
			it instanceof ScxmlFinalType
		);
	}
	
}
