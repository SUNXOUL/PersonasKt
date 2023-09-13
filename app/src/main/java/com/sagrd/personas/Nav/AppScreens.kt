package com.sagrd.personas.Nav

sealed class AppScreens(val route : String)
{
    object ConsultScreen: AppScreens("consult_Screen")
    object FormScreen: AppScreens("form_Screen")
    object  OccupationsScreen : AppScreens("occupations_Screen")
}
