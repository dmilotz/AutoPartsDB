package proj;

public enum ScreenState {
	
	/*
	 * These are the two screens for one choice from the initial screen (where the user chooses to look at part makers)
	 */
	BROWSE_PART_MAKERS, //Lists the four makers and allows the user to choose to look at parts from them
	BROWSE_PARTS_FROM_MAKER, //The screen that shows all of the parts from a specific part maker
	
	/*
	 * These are the 5 screens for the other choice from the initial screen (where the user chooses to select car manufacturer)
	 */
	BROWSE_CAR_MAKERS, //Shows the several car makers. After one is selected, models are chosen
	BROWSE_MODELS, //Shows the models of cars from a specific car manufacturer
	BROWSE_YEARS,  //Shows the years from a specific car model
	BROWSE_ENGINE_TYPES, //Shows engine types from a specific car model and year
	BROWSE_PARTS_FOR_ENGINE, // Shows parts available for a specific engine type on a car model and year
	
}
