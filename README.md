# OuchWorkout

## Introduction
OuchWorkout is an Android application to pratice sport workouts. One workout contains exercises like push ups, burpees, etc.
One exercise contains multiple global periods and one global period is a sequence of one action period (for example, 
for doing push ups) and one rest period (for recovering before the next global period). So, you do not have to touch your phone 
or tablet after the start of the selected workout.

## Screenshots
![Select Your Workout](/screenshots/workout_menu.jpg "Select Your Workout")
![Execute The Exercise](/screenshots/action_period.jpg "Execute The Exercise")
![Get Rest](/screenshots/rest_period.jpg "Get Rest")
![Keep Hydrated](/screenshots/completed_workout.jpg "Keep Hydrated")

## Workout Definition
Workouts are defined from JSON files in the application (ouchworkout/app/src/main/res/raw/). When the application starts,
every JSON files ending with '_wo.json' are parsed. A simple menu allows to select the workout to execute and then,
you follow the instructions. One workout is described in one JSON file as follows:
```json
{
  "name": "Static Workout",
  "workout": [{
    "name": "Push Up",
    "img": "push_up",
    "nb_exercise": 2,
    "nb_rep": 10,
    "action": 20,
    "rest": 30,
    "after": 60
  }, {
    "name": "L-Sit",
    "img": "l_sit",
    "nb_exercise": 4,
    "nb_rep": 1,
    "action": 12,
    "rest": 90,
    "after": 60
  }]
}
```
The name and the image are used to describe the exercise to execute. The number of global periods are defined by
the 'nb_exercise' field. In the above example, you do 10 push ups during 20 seconds, and then, you get to rest for 30 seconds.
After, you do 10 push ups again. You are executed the 2 exercises required to continue to the next exercise (L-sit).
Before starting the L-sit exercise, you get to rest for 60 seconds (the 'after' field).

## Install the application with Android Studio
NOTE: The application does not exist on the Playstore :'(

* To install the application and add or customize workouts, you can download 
Android Studio and install it: https://developer.android.com/studio/index.html
* Download the OuchWorkout project and open it in Android studio
* Then, connect your Android device and 'Run the app' to install the application on the device:
https://developer.android.com/training/basics/firstapp/running-app.html
